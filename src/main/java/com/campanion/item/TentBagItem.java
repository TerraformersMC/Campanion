package com.campanion.item;

import com.campanion.Campanion;
import com.campanion.blockentity.TentPartBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.util.TriConsumer;

public class TentBagItem extends Item {
    public TentBagItem(Settings settings) {
        super(settings);
        this.addPropertyGetter(new Identifier(Campanion.MOD_ID, "open"), (stack, world, entity) -> !stack.hasTag() || !stack.getOrCreateTag().contains("Blocks", 9) ? 1 : 0);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        BlockPos base = context.getBlockPos().up();

        if(traverseBlocks(stack, (pos, state, tag) -> {
            BlockPos off = base.add(pos);
            context.getWorld().setBlockState(off, state);
            BlockEntity entity = context.getWorld().getBlockEntity(off);
            if(entity != null && !tag.isEmpty()) {
                entity.fromTag(tag);
            }
            if(entity instanceof TentPartBlockEntity) {
                ((TentPartBlockEntity) entity).setLinkedPos(base);
            }
        })) {
            stack.getOrCreateTag().remove("Blocks");
            return ActionResult.CONSUME;
        }

        return super.useOnBlock(context);
    }

    public static boolean traverseBlocks(ItemStack stack, TriConsumer<BlockPos, BlockState, CompoundTag> consumer) {
        if(stack.hasTag() && stack.getOrCreateTag().contains("Blocks", 9)) {
            for (Tag block : stack.getOrCreateTag().getList("Blocks", 10)) {
                CompoundTag tag = (CompoundTag) block;
                BlockPos off = new BlockPos(tag.getByte("PosX"), tag.getByte("PosY"), tag.getByte("PosZ"));
                BlockState state = NbtHelper.toBlockState(tag.getCompound("BlockState"));
                CompoundTag data = tag.getCompound("BlockEntityData");
                consumer.accept(off, state, data);
            }
            return true;
        }
        return false;
    }
}
