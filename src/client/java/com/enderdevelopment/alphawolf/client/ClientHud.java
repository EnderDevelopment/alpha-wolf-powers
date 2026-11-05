package com.enderdevelopment.alphawolf.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * Draws the red and black Alpha Wolf HUD overlay while the player is transformed.
 */
public final class ClientHud {
    private static final int RED = 0xFFFF0000;
    private static final int BLACK = 0xFF000000;

    private ClientHud() {
    }

    public static void render(GuiGraphics guiGraphics, float tickDelta) {
        Minecraft client = Minecraft.getInstance();
        int width = guiGraphics.guiWidth();
        int margin = 4;

        // Top red bar and left black accent as a frame.
        guiGraphics.fill(margin, margin, width - margin, margin + 3, RED);
        guiGraphics.fill(margin, margin + 3, margin + 3, margin + 22, BLACK);

        // Alpha wolf title in red with a black shadow.
        guiGraphics.drawCenteredString(
            client.font,
            Component.literal("ALPHA WOLF"),
            width / 2,
            margin + 6,
            RED
        );

        // Small caption under the title.
        guiGraphics.drawCenteredString(
            client.font,
            Component.literal("Red and Black"),
            width / 2,
            margin + 17,
            0xFFFFFFFF
        );
    }
}
