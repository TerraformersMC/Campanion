package com.campanion.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BackpackItem extends Item {
	public final Type type;

	public BackpackItem(Type type, Item.Settings settings) {
		super(settings);
		this.type = type;
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stackInHand = user.getStackInHand(hand);
		EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(stackInHand);
		ItemStack equippedStack = user.getEquippedStack(equipmentSlot);
		if (equippedStack.isEmpty()) {
			user.equipStack(equipmentSlot, stackInHand.copy());
			stackInHand.setCount(0);
			return TypedActionResult.success(stackInHand);
		} else {
			return TypedActionResult.fail(stackInHand);
		}
	}

	public enum Type {
		DAY_PACK(9),
		CAMPING_PACK(18),
		HIKING_PACK(27);

		private int slots;

		Type(int slots) {
			this.slots = slots;
		}

		public int getSlots() {
			return slots;
		}
	}
}
