package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.List;

public class BackpackItem extends Item implements Equipable {

	private static final Component BACKPACK_HAS_ITEMS1 =
		Component.translatable("message.campanion.backpack.hasitems1")
			.setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	private static final Component BACKPACK_HAS_ITEMS2 =
		Component.translatable("message.campanion.backpack.hasitems2")
			.setStyle(Style.EMPTY.withColor(ChatFormatting.RED));

	public final Type type;

	public BackpackItem(Type type, Item.Properties settings) {
		super(settings.stacksTo(1));
		this.type = type;
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		return this.swapWithEquipmentSlot(this, world, user, hand);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
		LocalPlayer player = Minecraft.getInstance().player;
		if(player != null && player.getItemBySlot(EquipmentSlot.CHEST) == stack) {
			for (ItemStack itemStack : ((BackpackStorePlayer) player).getBackpackStacks()) {
				if(!itemStack.isEmpty()) {
					tooltip.add(BACKPACK_HAS_ITEMS1);
					tooltip.add(BACKPACK_HAS_ITEMS2);
					break;
				}
			}
		}
		super.appendHoverText(stack, pContext, tooltip, tooltipFlag);
	}

	private static int MAX_SLOTS = 0;
	public static int getMaxSlots() {
		return MAX_SLOTS;
	}

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.CHEST;
	}

	public enum Type {
		DAY_PACK(1, MenuType.GENERIC_9x1),
		CAMPING_PACK(2, MenuType.GENERIC_9x2),
		HIKING_PACK(3, MenuType.GENERIC_9x3);

		private final int rows;
		private final int slots;
		private final MenuType<ChestMenu> containerType;

		Type(int rows, MenuType<ChestMenu> containerType) {
			this.rows = rows;
			this.slots = rows * 9;
			MAX_SLOTS = Math.max(MAX_SLOTS, this.slots);
			this.containerType = containerType;
		}

		public MenuType<ChestMenu> getContainerType() {
			return containerType;
		}

		public int getRows() {
			return rows;
		}

		public int getSlots() {
			return slots;
		}
	}
}
