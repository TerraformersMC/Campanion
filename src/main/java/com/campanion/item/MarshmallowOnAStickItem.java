package com.campanion.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class MarshmallowOnAStickItem extends Item {

	private int type;

	public static final int UNCOOKED = 0;
	public static final int COOKED = 1;
	public static final int BLACKENED = 2;

	public MarshmallowOnAStickItem(Settings settings, int type) {
		super(settings);
		this.type = type;
	}

	public int getType() {
		return type;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		MarshmallowOnAStickItem item = (MarshmallowOnAStickItem)user.getStackInHand(hand).getItem();
		user.setStackInHand(hand, new ItemStack(Items.STICK));
		switch (item.getType()) {
			case UNCOOKED:
				user.giveItemStack(new ItemStack(CampanionItems.MARSHMALLOW));
				return TypedActionResult.consume(user.getStackInHand(hand));
			case COOKED:
				user.giveItemStack(new ItemStack(CampanionItems.COOKED_MARSHMALLOW));
				return TypedActionResult.consume(user.getStackInHand(hand));
			case BLACKENED:
				user.giveItemStack(new ItemStack(CampanionItems.BLACKENED_MARSHMALLOW));
				return TypedActionResult.consume(user.getStackInHand(hand));
		}
		return TypedActionResult.pass(user.getStackInHand(hand));
	}
}
