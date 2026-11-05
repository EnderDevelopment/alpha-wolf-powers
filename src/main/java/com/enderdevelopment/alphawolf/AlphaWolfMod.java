package com.enderdevelopment.alphawolf;

import com.enderdevelopment.alphawolf.net.WolfSyncPayload;
import com.enderdevelopment.alphawolf.net.WolfTogglePayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.commands.Commands.literal;

public class AlphaWolfMod implements ModInitializer {
    public static final String MOD_ID = "alphawolf";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Alpha Wolf mod loaded - become the red and black Alpha Wolf!");

        // Register network payloads for the wolf toggle (client -> server) and sync (server -> client).
        PayloadTypeRegistry.playC2S().register(WolfTogglePayload.TYPE, WolfTogglePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(WolfSyncPayload.TYPE, WolfSyncPayload.STREAM_CODEC);

        // Handle the toggle request coming from a client.
        ServerPlayNetworking.registerGlobalReceiver(WolfTogglePayload.TYPE, (payload, context) -> {
            context.player().server.execute(() -> {
                ServerPlayer player = context.player();
                if (WolfManager.getWolf(player) != payload.active()) {
                    WolfManager.setWolf(player, payload.active());
                }
            });
        });

        // Sync the current wolf state to a player when they join.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            sender.sendPacket(new WolfSyncPayload(WolfManager.getWolf(player)));
        });

        // Command: /alpha toggles the transformation.
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("alpha")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    boolean current = WolfManager.getWolf(player);
                    WolfManager.setWolf(player, !current);
                    ctx.getSource().sendSuccess(() -> Component.literal(
                        current
                            ? "You returned to your human form."
                            : "You transform into the red and black Alpha Wolf!"
                    ), false);
                    return 1;
                }));
        });

        // Apply the per-tick wolf effects (particles, glowing) to all transformed players.
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                if (WolfManager.getWolf(player)) {
                    WolfManager.tickWolf(player);
                }
            }
        });
    }
}
