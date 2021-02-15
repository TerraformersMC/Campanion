package com.terraformersmc.campanion.compat;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import net.guavy.gravestones.api.GravestonesApi;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.Iterator;
import java.util.List;

public class CampanionGravestonesCompat implements GravestonesApi {

	@Override
	public List<ItemStack> getInventory(PlayerEntity playerEntity) {
		return ((BackpackStorePlayer) playerEntity).getBackpackStacks();
	}

	@Override
	public void setInventory(List<ItemStack> list, PlayerEntity playerEntity) {
		ItemStack chestItem = playerEntity.getEquippedStack(EquipmentSlot.CHEST);
		int capacity = BackpackStorePlayer.getStackCapacity(chestItem);
		DefaultedList<ItemStack> backpackContents = ((BackpackStorePlayer) playerEntity).getBackpackStacks();

		while (backpackContents.size() < capacity) {
			backpackContents.add(ItemStack.EMPTY);
		}

		Iterator<ItemStack> iter = list.iterator();
		list.removeIf(ItemStack::isEmpty);
		for (int i = 0; i < capacity && iter.hasNext(); i++) {
			if (backpackContents.get(i).isEmpty()) {
				backpackContents.set(i, iter.next());
				iter.remove();
			}
		}
		((BackpackStorePlayer) playerEntity).setBackpackStacks(backpackContents);
		for (ItemStack stack : list) {
			if (!playerEntity.giveItemStack(stack)) {
				playerEntity.dropStack(stack);
			}
		}
	}

	@Override
	public int getInventorySize(PlayerEntity playerEntity) {
		return BackpackStorePlayer.getStackCapacity(playerEntity.getEquippedStack(EquipmentSlot.CHEST));
	}
}
