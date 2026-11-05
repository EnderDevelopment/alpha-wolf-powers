package com.enderdevelopment.alphawolf.client;

import com.enderdevelopment.alphawolf.net.WolfSyncPayload;
import com.enderdevelopment.alphawolf.net.WolfTogglePayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

/**
 * Client entrypoint that registers the transformation keybind, forwards toggle
 * requests to the server, and draws the red/black wolf HUD overlay.
 */
public class AlphaWolfClient implements ClientModInitializer {
    private static KeyMapping wolfKey;

    private static boolean wolfActive = false;

    public static boolean isWolf() {
        return wolfActive;
    }

    public static void setWolf(boolean active) {
        wolfActive = active;
    }

    @Override
    public void onInitializeClient() {
        wolfKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.alphawolf.transform",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "category.alphawolf.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                return;
            }
            while (wolfKey.consumeClick()) {
                ClientPlayNetworking.send(new WolfTogglePayload(!wolfActive));
            }
        });

        // Receive wolf state updates from the server to keep the HUD in sync.
        ClientPlayNetworking.registerGlobalReceiver(WolfSyncPayload.TYPE, (payload, context) ->
            context.client().execute(() -> setWolf(payload.active()))
        );

        // Draw the wolf HUD overlay while transformed.
        HudRenderCallback.EVENT.register(ClientHud::render);
    }
}
