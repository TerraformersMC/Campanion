package com.campanion.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MarshmallowOnAStickItem extends Item {

	private final Item marshmallow;

	public MarshmallowOnAStickItem(Settings settings, Item marshmallow) {
		super(settings);
		this.marshmallow = marshmallow;
	}

	public Item getMarshmallow() {
		return marshmallow;
	}


	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!user.isCreative()) {
			user.getStackInHand(hand).decrement(1);
		}
		user.giveItemStack(new ItemStack(Items.STICK));
		user.giveItemStack(new ItemStack(this.getMarshmallow()));
		return TypedActionResult.success(user.getStackInHand(hand));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockState state = context.getWorld().getBlockState(context.getBlockPos());
		if (state.getBlock() instanceof CampfireBlock && state.get(CampfireBlock.LIT)) {
			ItemStack stack = context.getStack();
			PlayerEntity player = context.getPlayer();
			if (player != null) {
				Hand hand = context.getHand();
				if (stack.getItem().equals(CampanionItems.MARSHMALLOW_ON_A_STICK)) {
					if (!player.isCreative()) {
						player.getStackInHand(hand).decrement(1);
					}
					player.giveItemStack(new ItemStack(CampanionItems.COOKED_MARSHMALLOW_ON_A_STICK));
					return ActionResult.SUCCESS;
				} else if (stack.getItem().equals(CampanionItems.COOKED_MARSHMALLOW_ON_A_STICK)) {
					if (!player.isCreative()) {
						player.getStackInHand(hand).decrement(1);
					}
					player.giveItemStack(new ItemStack(CampanionItems.BLACKENED_MARSHMALLOW_ON_A_STICK));
					return ActionResult.SUCCESS;
				}
			}
		}
		return ActionResult.PASS;
	}
}
