package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.SpearItem;
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
		//Note by default, all trident enchantments will be enabled.
		if(stack.getItem() instanceof SpearItem) {
			//Non trident enchantments we want to have
			if (((Object) this == Enchantments.PIERCING)) {
				info.setReturnValue(true);
			}

			//Trident enchants we don't want to have
			if((Object) this == Enchantments.LOYALTY || (Object) this == Enchantments.CHANNELING) {
				info.setReturnValue(false);
			}
		}
	}
}
