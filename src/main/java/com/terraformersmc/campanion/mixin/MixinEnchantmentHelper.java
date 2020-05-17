package com.terraformersmc.campanion.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.item.ItemStack;

import com.terraformersmc.campanion.item.SpearItem;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {
	@Inject(method = "getHighestApplicableEnchantmentsAtPower(ILnet/minecraft/item/ItemStack;Z)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
	private static void onGetHighestApplicableEnchantmentsAtPower(int power, ItemStack stack, boolean hasTreasure, CallbackInfoReturnable<List<InfoEnchantment>> info) {
		if (stack.getItem() instanceof SpearItem) {
			List<InfoEnchantment> currentEnchantments = info.getReturnValue();
			List<InfoEnchantment> enchantments = new ArrayList<>();
			currentEnchantments.forEach(enchantment -> {
				if (!(enchantment.enchantment.type == EnchantmentTarget.TRIDENT) || enchantment.enchantment == Enchantments.IMPALING) {
					enchantments.add(enchantment);
				}
			});
			Enchantment piercing = Enchantments.PIERCING;
			for (int level = piercing.getMaximumLevel(); level > piercing.getMinimumLevel() - 1; --level) {
				if (power >= piercing.getMinimumPower(level) && power <= piercing.getMaximumPower(level)) {
					enchantments.add(new InfoEnchantment(piercing, level));
					break;
				}
			}
			info.setReturnValue(enchantments);
		}
	}
}
