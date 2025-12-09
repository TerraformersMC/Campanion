package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.backpack.BackpackContainerFactory;
import com.terraformersmc.campanion.item.BackpackItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class C2SOpenBackpack implements CustomPacketPayload {
	public static final C2SOpenBackpack INSTANCE = new C2SOpenBackpack();
	public static final Type<C2SOpenBackpack> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Campanion.MOD_ID, "open_backpack"));
	public static final StreamCodec<RegistryFriendlyByteBuf, C2SOpenBackpack> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	private C2SOpenBackpack(){
	}

	public static void handle(Supplier<MinecraftServer> server, ServerPlayer player, C2SOpenBackpack packet) {
		ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);

//		if (CampanionConfigManager.getConfig().isTrinketsBackpacksEnabled() &&
//			FabricLoader.getInstance().isModLoaded("trinkets")) {
//			TrinketComponent component = TrinketsApi.getTrinketComponent(player).orElse(null);
//
//			if (component != null && component.isEquipped(itemStack -> itemStack.getItem() instanceof BackpackItem)) {
//				stack = component.getEquipped(itemStack -> itemStack.getItem() instanceof BackpackItem).get(0).getB();
//			}
//		}

		if (stack.getItem() instanceof BackpackItem) {
			BackpackItem.Type type = ((BackpackItem) stack.getItem()).type;
			player.openMenu(new BackpackContainerFactory(type));
		}

	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
