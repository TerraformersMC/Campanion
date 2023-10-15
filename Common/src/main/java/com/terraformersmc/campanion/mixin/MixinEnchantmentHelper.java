package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.SpearItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {
	@Inject(method = "getAvailableEnchantmentResults(ILnet/minecraft/world/item/ItemStack;Z)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
	private static void getAvailableEnchantmentResults(int power, ItemStack stack, boolean hasTreasure, CallbackInfoReturnable<List<EnchantmentInstance>> info) {
		if (stack.getItem() instanceof SpearItem) {
			List<EnchantmentInstance> currentEnchantments = info.getReturnValue();
			List<EnchantmentInstance> enchantments = new ArrayList<>();
			currentEnchantments.forEach(enchantment -> {
				if (!(enchantment.enchantment.category == EnchantmentCategory.TRIDENT) || enchantment.enchantment == Enchantments.IMPALING) {
					enchantments.add(enchantment);
				}
			});
			Enchantment piercing = Enchantments.PIERCING;
			for (int level = piercing.getMaxLevel(); level > piercing.getMinLevel() - 1; --level) {
				if (power >= piercing.getMinCost(level) && power <= piercing.getMaxCost(level)) {
					enchantments.add(new EnchantmentInstance(piercing, level));
					break;
				}
			}
			info.setReturnValue(enchantments);
		}
	}
}
