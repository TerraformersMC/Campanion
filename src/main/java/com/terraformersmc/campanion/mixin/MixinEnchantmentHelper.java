package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.SpearItem;
import net.minecraft.enchantment.*;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {
	@Inject(method = "getPossibleEntries(ILnet/minecraft/item/ItemStack;Z)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
	private static void onGetPossibleEntries(int power, ItemStack stack, boolean hasTreasure, CallbackInfoReturnable<List<EnchantmentLevelEntry>> info) {
		if (stack.getItem() instanceof SpearItem) {
			List<EnchantmentLevelEntry> currentEnchantments = info.getReturnValue();
			List<EnchantmentLevelEntry> enchantments = new ArrayList<>();
			currentEnchantments.forEach(enchantment -> {
				if (!(enchantment.enchantment.type == EnchantmentTarget.TRIDENT) || enchantment.enchantment == Enchantments.IMPALING) {
					enchantments.add(enchantment);
				}
			});
			Enchantment piercing = Enchantments.PIERCING;
			for (int level = piercing.getMaximumLevel(); level > piercing.getMinimumLevel() - 1; --level) {
				if (power >= piercing.getMinimumPower(level) && power <= piercing.getMaximumPower(level)) {
					enchantments.add(new EnchantmentLevelEntry(piercing, level));
					break;
				}
			}
			info.setReturnValue(enchantments);
		}
	}
}
