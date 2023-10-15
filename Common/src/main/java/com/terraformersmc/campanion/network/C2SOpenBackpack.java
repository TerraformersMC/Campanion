package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.backpack.BackpackContainerFactory;
import com.terraformersmc.campanion.item.BackpackItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class C2SOpenBackpack {
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

}
