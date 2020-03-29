package com.campanion.mixin;

import com.campanion.item.SpearItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class MixinEnchantment {
	@Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
	private void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		//Todo: Fix piercing enchant and re-enable this
		if ((/*(Object) this == Enchantments.PIERCING ||*/ (Object) this == Enchantments.IMPALING) && stack.getItem() instanceof SpearItem) {
			info.setReturnValue(true);
		}
	}
}
