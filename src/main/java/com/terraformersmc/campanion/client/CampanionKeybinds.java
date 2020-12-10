package com.terraformersmc.campanion.client;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.network.C2SOpenBackpack;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class CampanionKeybinds {
    private static final FabricKeyBinding OPEN_BACKPACK_KEY = FabricKeyBinding.Builder.create(
        new Identifier(Campanion.MOD_ID, "open_backpack"),
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_B,
        "key.categories.gameplay"
    ).build();

    public static void initialize() {
        KeyBindingRegistry.INSTANCE.register(OPEN_BACKPACK_KEY);
        ClientTickCallback.EVENT.register(e -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if(player != null && OPEN_BACKPACK_KEY.isPressed()) {
                player.networkHandler.sendPacket(C2SOpenBackpack.createPacket());
            }
        });
    }
}
