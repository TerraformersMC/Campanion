package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.item.SleepingBagItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.TransferQueue;

@Mixin(InBedChatScreen.class)
public abstract class MixinScreen extends ChatScreen {
	public MixinScreen(String $$0) {
		super($$0);
	}

	@Override
	protected <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T button) {
		if (SleepingBagItem.getUsingStack(Minecraft.getInstance().player).isPresent()) {
			if (button instanceof AbstractWidget) {
				((AbstractWidget) button).setMessage(Component.translatable("item.campanion.sleeping_bag.stop_sleeping"));
			}
		}
		return super.addRenderableWidget(button);
	}
}
