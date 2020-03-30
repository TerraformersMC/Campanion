package com.campanion.mixin;

import com.campanion.item.CampanionItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public class MixinCampfireBlock {

	@Shadow
	@Final
	public static BooleanProperty LIT;

	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		if ((Boolean)state.get(LIT)) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CampfireBlockEntity) {
				ItemStack itemStack = player.getStackInHand(hand);
				if (itemStack.getItem().equals(CampanionItems.MARSHMALLOW_ON_A_STICK)) {
					player.setStackInHand(hand, new ItemStack(CampanionItems.COOKED_MARSHMALLOW_ON_A_STICK));
					cir.setReturnValue(ActionResult.SUCCESS);
				} else if (itemStack.getItem().equals(CampanionItems.COOKED_MARSHMALLOW_ON_A_STICK)) {
					player.setStackInHand(hand, new ItemStack(CampanionItems.BLACKENED_MARSHMALLOW_ON_A_STICK));
					cir.setReturnValue(ActionResult.SUCCESS);
				}
			}
		}
	}

}
