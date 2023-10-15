package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.item.SleepingBagItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InBedChatScreen.class)
public abstract class MixinScreen extends ChatScreen {
	public MixinScreen(String $$0) {
		super($$0);
	}

	@Override
	protected <T extends GuiEventListener & Renderable & NarratableEntry> @NotNull T addRenderableWidget(@NotNull T button) {
		if (SleepingBagItem.getUsingStack(Minecraft.getInstance().player).isPresent()) {
			if (button instanceof AbstractWidget) {
				((AbstractWidget) button).setMessage(Component.translatable("item.campanion.sleeping_bag.stop_sleeping"));
			}
		}
		return super.addRenderableWidget(button);
	}
}
