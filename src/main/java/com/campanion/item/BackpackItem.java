package com.campanion.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class BackpackItem extends ArmorItem {
	public final Type type;
	public static final ArmorMaterial MATERIAL = new BackpackItem.ArmorMaterial();

	public BackpackItem(Type type, Item.Settings settings) {
		super(MATERIAL, EquipmentSlot.CHEST, settings);
		this.type = type;
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

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return false;
	}

	public static class ArmorMaterial implements net.minecraft.item.ArmorMaterial {
		@Override
		public int getDurability(EquipmentSlot slot) {
			return 0;
		}

		@Override
		public int getProtectionAmount(EquipmentSlot slot) {
			return 0;
		}

		@Override
		public int getEnchantability() {
			return 0;
		}

		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.EMPTY;
		}

		@Override
		public String getName() {
			return "backpack";
		}

		@Override
		public float getToughness() {
			return 0;
		}
	}
}
