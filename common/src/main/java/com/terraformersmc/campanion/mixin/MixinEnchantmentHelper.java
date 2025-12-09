package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.SpearItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {
	@Inject(method = "getAvailableEnchantmentResults", at = @At("RETURN"), cancellable = true)
	private static void getAvailableEnchantmentResults(int power, ItemStack stack, Stream<Holder<Enchantment>> pPossibleEnchantments, CallbackInfoReturnable<List<EnchantmentInstance>> info) {
		if (stack.getItem() instanceof SpearItem) {
			List<EnchantmentInstance> currentEnchantments = info.getReturnValue();
			List<EnchantmentInstance> enchantments = new ArrayList<>();
			currentEnchantments.forEach(enchantment -> {
				if (!(enchantment.enchantment.value().definition().supportedItems().contains(Items.TRIDENT.builtInRegistryHolder()))
					|| enchantment.enchantment.is(Enchantments.IMPALING)) {
					enchantments.add(enchantment);
				}
			});
			Holder<Enchantment> piercingHolder = pPossibleEnchantments.filter(ench -> ench.is(Enchantments.PIERCING)).findFirst().get();
			Enchantment piercing = piercingHolder.value();
			for (int level = piercing.getMaxLevel(); level > piercing.getMinLevel() - 1; --level) {
				if (power >= piercing.getMinCost(level) && power <= piercing.getMaxCost(level)) {
					enchantments.add(new EnchantmentInstance(piercingHolder, level));
					break;
				}
			}
			info.setReturnValue(enchantments);
		}
	}
}
