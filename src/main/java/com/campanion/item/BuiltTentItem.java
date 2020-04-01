package com.campanion.item;

import com.campanion.blockentity.TentPartBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class BuiltTentItem extends Item {
    public BuiltTentItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        BlockPos base = context.getBlockPos().up();
        if(stack.hasTag() && stack.getOrCreateTag().contains("Blocks", 9)) {
            for (Tag block : stack.getOrCreateTag().getList("Blocks", 10)) {
                CompoundTag tag = (CompoundTag) block;
                BlockPos off = base.add(tag.getByte("PosX"), tag.getByte("PosY"), tag.getByte("PosZ"));
                context.getWorld().setBlockState(off, NbtHelper.toBlockState(tag.getCompound("BlockState")));
                BlockEntity entity = context.getWorld().getBlockEntity(off);
                if(entity != null && tag.contains("BlockEntityData", 10)) {
                    entity.fromTag(tag.getCompound("BlockEntityData"));
                }
                if(entity instanceof TentPartBlockEntity) {
                    ((TentPartBlockEntity) entity).setLinkedPos(base);
                }
            }
            return ActionResult.CONSUME;
        }
        return super.useOnBlock(context);
    }
}
