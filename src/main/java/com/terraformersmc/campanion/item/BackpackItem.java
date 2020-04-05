package com.terraformersmc.campanion.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.container.GenericContainer;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BackpackItem extends Item {

	public final Type type;

	public BackpackItem(Type type, Item.Settings settings) {
		super(settings.maxCount(1));
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
		DAY_PACK(1, ContainerType.GENERIC_9X1),
		CAMPING_PACK(2, ContainerType.GENERIC_9X2),
		HIKING_PACK(3, ContainerType.GENERIC_9X3);

		private final int rows;
		private final int slots;
		private final ContainerType<GenericContainer> containerType;

		Type(int rows, ContainerType<GenericContainer> containerType) {
			this.rows = rows;
			this.slots = rows*9;
			this.containerType = containerType;
		}

		public ContainerFactory createFactory(ItemStack stack) {
			return new ContainerFactory(this, stack);
		}
	}

	public static class ContainerFactory implements NameableContainerFactory {

		private final Type type;
		private final ItemStack stack;

		public ContainerFactory(Type type, ItemStack stack) {
			this.type = type;
			this.stack = stack;
		}

		@Override
		public Text getDisplayName() {
			return new TranslatableText("container.campanion." + this.type.name().toLowerCase());
		}

		@Override
		public Container createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
			BasicInventory inventory = new BasicInventory(getItems(this.stack).toArray(new ItemStack[0]));
			inventory.addListener(newinv -> {
				DefaultedList<ItemStack> invList = DefaultedList.ofSize(newinv.getInvSize(), ItemStack.EMPTY);
				for (int slot = 0; slot < newinv.getInvSize(); slot++) {
					invList.set(slot, newinv.getInvStack(slot));
				}
				this.stack.getOrCreateTag().put("Inventory", Inventories.toTag(new CompoundTag(), invList));
			});

			return new GenericContainer(this.type.containerType, syncId, inv, inventory, this.type.rows);
		}
	}

	public static DefaultedList<ItemStack> getItems(ItemStack stack) {
		DefaultedList<ItemStack> list = DefaultedList.ofSize(((BackpackItem) stack.getItem()).type.slots, ItemStack.EMPTY);
		Inventories.fromTag(stack.getOrCreateTag().getCompound("Inventory"), list);
		return list;
	}


}
