package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.item.SleepingBagItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class MixinScreen {
	@Inject(method = "addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", at = @At("HEAD"))
	private void onAddDrawableChild(GuiEventListener button, CallbackInfoReturnable<GuiEventListener> info) {
		if ((Object) this instanceof InBedChatScreen && SleepingBagItem.getUsingStack(Minecraft.getInstance().player).isPresent()) {
			if (button instanceof AbstractWidget) {
				((AbstractWidget) button).setMessage(new TranslatableComponent("item.campanion.sleeping_bag.stop_sleeping"));
			}
		}
	}
}
