package com.campanion.item;

import com.campanion.Campanion;
import com.campanion.blockentity.TentPartBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.apache.logging.log4j.util.TriConsumer;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class TentBagItem extends Item {
    public TentBagItem(Settings settings) {
        super(settings);
        this.addPropertyGetter(new Identifier(Campanion.MOD_ID, "open"), (stack, world, entity) -> hasBlocks(stack) ? 0 : 1);
    }

    public static boolean hasBlocks(ItemStack stack) {
        return stack.getItem() == CampanionItems.TENT_BAG && stack.hasTag() && stack.getOrCreateTag().contains("Blocks", 9);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        HitResult result = user.rayTrace(10, 0, true);
        if (result instanceof BlockHitResult && result.getType() == HitResult.Type.BLOCK) {
            BlockPos base = ((BlockHitResult) result).getBlockPos().up();
            if(!world.isClient && getErrorPosition(world, base, stack).isEmpty()) {
                traverseBlocks(stack, (pos, state, tag) -> {
                    BlockPos off = base.add(pos);

                    world.setBlockState(off, state);
                    BlockEntity entity = world.getBlockEntity(off);
                    if(entity != null && !tag.isEmpty()) {
                        tag.putInt("x", off.getX());
                        tag.putInt("y", off.getY());
                        tag.putInt("z", off.getZ());
                        entity.fromTag(tag);
                        entity.markDirty();
                    }
                    if(entity instanceof TentPartBlockEntity) {
                        ((TentPartBlockEntity) entity).setLinkedPos(base);
                        entity.markDirty();
                    }

                });
                stack.getOrCreateTag().remove("Blocks");
            }

            return new TypedActionResult<>(ActionResult.CONSUME, stack);
        }

        return super.use(world, user, hand);
    }

    public static List<BlockPos> getErrorPosition(WorldView world, BlockPos pos, ItemStack stack) {
        List<BlockPos> list = new ArrayList<>();
        if(hasBlocks(stack)) {
            Vec3d changeSize = new Vec3d(NbtHelper.toBlockPos(stack.getOrCreateTag().getCompound("TentSize"))).add(-1, -1, -1).multiply(1/2F);
            for (int x = MathHelper.floor(-changeSize.x); x <= MathHelper.floor(changeSize.x); x++) {
                for (int y = -1; y <= 2 * changeSize.getY(); y++) {
                    for (int z = MathHelper.floor(-changeSize.z); z <= MathHelper.floor(changeSize.z); z++) {
                        BlockPos blockPos = new BlockPos(pos.add(x, y, z));
                        if(y != -1 == !world.getBlockState(blockPos).getMaterial().isReplaceable()) {
                            list.add(blockPos);
                        }
                    }
                }
            }
        }
        return list;
    }

    public static void traverseBlocks(ItemStack stack, TriConsumer<BlockPos, BlockState, CompoundTag> consumer) {
        if(hasBlocks(stack)) {
            for (Tag block : stack.getOrCreateTag().getList("Blocks", 10)) {
                CompoundTag tag = (CompoundTag) block;
                BlockPos off = NbtHelper.toBlockPos(tag.getCompound("Pos"));
                BlockState state = NbtHelper.toBlockState(tag.getCompound("BlockState"));
                CompoundTag data = tag.getCompound("BlockEntityData");
                data.remove("x");
                data.remove("y");
                data.remove("z");
                consumer.accept(off, state, data);
            }
        }
    }
}
