package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.platform.Services;
import com.terraformersmc.campanion.tag.CampanionItemTags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class MixinEnchantment {
	@Shadow
	@Final
	private Component description;

	@Inject(method = {"canEnchant", "isSupportedItem"}, at = @At("HEAD"), cancellable = true)
	private void canEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		//Note by default, all trident enchantments will be enabled.
		if(stack.is(CampanionItemTags.SPEARS)) {
			if(Services.PLATFORM.isDevelopmentEnvironment()){
				Campanion.LOG.info("Checking enchantment permissions for {}", stack);
			}
			//Non trident enchantments we want to have
			String descriptionString = this.description.getContents() instanceof TranslatableContents translatableContents ? translatableContents.getKey() : "";
			if (descriptionString.contains("minecraft.piercing")) {
				info.setReturnValue(true);
				if(Services.PLATFORM.isDevelopmentEnvironment()){
					Campanion.LOG.info("Allowing {} to be enchanted with {}", stack, this.description.getString());
				}
			}

			//Trident enchants we don't want to have
			if(descriptionString.contains("minecraft.loyalty") || descriptionString.contains("minecraft.channeling")) {
				info.setReturnValue(false);
				if(Services.PLATFORM.isDevelopmentEnvironment()){
					Campanion.LOG.info("Disallowing {} to be enchanted with {}", stack, this.description.getString());
				}
			}
		}
	}
}
