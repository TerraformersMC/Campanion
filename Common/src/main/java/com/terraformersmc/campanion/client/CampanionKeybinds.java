package com.terraformersmc.campanion.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.network.C2SOpenBackpack;
import com.terraformersmc.campanion.platform.Services;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class CampanionKeybinds {
    private static final KeyMapping OPEN_BACKPACK_KEY = new KeyMapping(
        String.format("%s.%s", Campanion.MOD_ID, "open_backpack"),
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_B,
        "key.categories.gameplay"
    );

    public static void initialize() {
        KeyBindingRegistry.INSTANCE.register(OPEN_BACKPACK_KEY);
        ClientTickCallback.EVENT.register(e -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if(player != null && OPEN_BACKPACK_KEY.isDown()) {
				Services.NETWORK.sendToServer(new C2SOpenBackpack());
            }
        });
    }
}
