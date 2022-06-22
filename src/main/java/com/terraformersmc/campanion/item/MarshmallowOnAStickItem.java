package com.terraformersmc.campanion.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MarshmallowOnAStickItem extends Item {

	private final Item marshmallow;

	public MarshmallowOnAStickItem(Properties settings, Item marshmallow) {
		super(settings);
		this.marshmallow = marshmallow;
	}

	public Item getMarshmallow() {
		return marshmallow;
	}


	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (!user.isCreative()) {
			user.getItemInHand(hand).shrink(1);
		}
		user.addItem(new ItemStack(Items.STICK));
		user.addItem(new ItemStack(this.getMarshmallow()));
		return InteractionResultHolder.success(user.getItemInHand(hand));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		if (state.getBlock() instanceof CampfireBlock && state.getValue(CampfireBlock.LIT)) {
			ItemStack stack = context.getItemInHand();
			Player player = context.getPlayer();
			if (player != null) {
				InteractionHand hand = context.getHand();
				if (stack.getItem().equals(CampanionItems.MARSHMALLOW_ON_A_STICK)) {
					if (!player.isCreative()) {
						player.getItemInHand(hand).shrink(1);
					}
					player.addItem(new ItemStack(CampanionItems.COOKED_MARSHMALLOW_ON_A_STICK));
					return InteractionResult.SUCCESS;
				} else if (stack.getItem().equals(CampanionItems.COOKED_MARSHMALLOW_ON_A_STICK)) {
					if (!player.isCreative()) {
						player.getItemInHand(hand).shrink(1);
					}
					player.addItem(new ItemStack(CampanionItems.BLACKENED_MARSHMALLOW_ON_A_STICK));
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}
}
