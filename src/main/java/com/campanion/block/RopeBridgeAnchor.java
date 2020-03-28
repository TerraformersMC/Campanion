package com.campanion.block;

import com.campanion.blockentity.PlankBlockEntity;
import com.campanion.item.CampanionItems;
import com.campanion.ropebridge.RopeBridge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RopeBridgeAnchor extends Block implements BlockEntityProvider {

    private static final String CLICKED_POSITION_KEY = "ClickedPosition";

    public RopeBridgeAnchor(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if(stack.getItem() == CampanionItems.ROPE && !world.isClient) {
            CompoundTag tag = stack.getOrCreateTag();
            if(tag.contains(CLICKED_POSITION_KEY, 4)) {
                BlockPos clickedPos = BlockPos.fromLong(tag.getLong(CLICKED_POSITION_KEY));
                tag.remove(CLICKED_POSITION_KEY);
                new RopeBridge(clickedPos, pos).generateBlocks(world);
            } else {
                tag.putLong(CLICKED_POSITION_KEY, pos.asLong());
            }
        }
        return ActionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new PlankBlockEntity();
    }
}
