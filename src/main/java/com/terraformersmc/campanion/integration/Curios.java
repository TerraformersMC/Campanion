package com.terraformersmc.campanion.integration;

import com.terraformersmc.campanion.item.BackpackItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeInfo;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.component.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Optional;

/**
 * This is an OPTIONAL integration. The rest of this project should only use
 * this wrapper to access the curios API. Also the methods here must careful to
 * check for the existence of the curios api before using it.
 */
public class Curios {
	private static final String BACKPACK_SLOT_ID = "back";
	private static boolean enabled = false;

	/**
	 * Should be called <i>only once</i> as part of ModInitializer.onInitialize()
	 */
	public static void register() {
		enabled = FabricLoader.getInstance().isModLoaded("curios");
		if (enabled) {
			SlotTypeInfo bpSlot = SlotTypePreset.findPreset(BACKPACK_SLOT_ID).get().getInfoBuilder().build();
			CuriosApi.enqueueSlotType(SlotTypeInfo.BuildScheme.REGISTER, bpSlot);
		}
	}

	public static boolean isEnabled() {
		return enabled;
	}

	/**
	 * @return the first equipped backpack found on the entity or ItemStack.EMPTY if not found
	 */
	public static ItemStack getBackpackStack(LivingEntity entity) {
		if (!enabled) {
			return ItemStack.EMPTY;
		}
		Optional<ICuriosItemHandler> itemHandler = CuriosApi.getCuriosHelper().getCuriosHandler(entity);
		if (itemHandler.isPresent()) {
			Optional<ICurioStacksHandler> stacksHandler =
					itemHandler.get().getStacksHandler(BACKPACK_SLOT_ID);
			if (stacksHandler.isPresent()) {
				ItemStack stack = stacksHandler.get().getStacks().getStack(0);
				if (stack.getItem() instanceof BackpackItem) {
					return stack;
				}
			}
		}
		return ItemStack.EMPTY;
	}
}
