package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.SpearItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class MixinEnchantment {
	@Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
	private void canEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
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
