package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.item.SleepingBagItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class MixinScreen {
	@Inject(method = "addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", at = @At("HEAD"))
	private void onAddDrawableChild(Element button, CallbackInfoReturnable<Element> info) {
		if ((Object) this instanceof SleepingChatScreen && SleepingBagItem.getUsingStack(MinecraftClient.getInstance().player).isPresent()) {
			if (button instanceof ClickableWidget) {
				((ClickableWidget) button).setMessage(new TranslatableText("item.campanion.sleeping_bag.stop_sleeping"));
			}
		}
	}
}
