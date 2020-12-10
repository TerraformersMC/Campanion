package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BackpackItem extends Item {

	private static final Text BACKPACK_HAS_ITEMS1 =
		new TranslatableText("message.campanion.backpack.hasitems1")
			.setStyle(Style.EMPTY.withColor(Formatting.RED));
	private static final Text BACKPACK_HAS_ITEMS2 =
		new TranslatableText("message.campanion.backpack.hasitems2")
			.setStyle(Style.EMPTY.withColor(Formatting.RED));

	public final Type type;

	public BackpackItem(Type type, Item.Settings settings) {
		super(settings.maxCount(1));
		this.type = type;
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
	}

	@Override
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

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if(player != null && player.getEquippedStack(EquipmentSlot.CHEST) == stack) {
			for (ItemStack itemStack : ((BackpackStorePlayer) player).getBackpackStacks()) {
				if(!itemStack.isEmpty()) {
					tooltip.add(BACKPACK_HAS_ITEMS1);
					tooltip.add(BACKPACK_HAS_ITEMS2);
					break;
				}
			}
		}
		super.appendTooltip(stack, world, tooltip, context);
	}

	private static int MAX_SLOTS = 0;
	public static int getMaxSlots() {
		return MAX_SLOTS;
	}

	public enum Type {
		DAY_PACK(1, ScreenHandlerType.GENERIC_9X1),
		CAMPING_PACK(2, ScreenHandlerType.GENERIC_9X2),
		HIKING_PACK(3, ScreenHandlerType.GENERIC_9X3);

		private final int rows;
		private final int slots;
		private final ScreenHandlerType<GenericContainerScreenHandler> containerType;

		Type(int rows, ScreenHandlerType<GenericContainerScreenHandler> containerType) {
			this.rows = rows;
			this.slots = rows * 9;
			MAX_SLOTS = Math.max(MAX_SLOTS, this.slots);
			this.containerType = containerType;
		}

		public ScreenHandlerType<GenericContainerScreenHandler> getContainerType() {
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
