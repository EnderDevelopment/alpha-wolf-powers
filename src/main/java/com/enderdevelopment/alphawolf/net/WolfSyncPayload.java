package com.enderdevelopment.alphawolf.net;

import com.enderdevelopment.alphawolf.AlphaWolfMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Server-to-client payload that tells the client whether the player is currently
 * a wolf (used to keep the HUD overlay in sync).
 */
public record WolfSyncPayload(boolean active) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WolfSyncPayload> TYPE =
        new CustomPacketPayload.Type<>(AlphaWolfMod.id("wolf_sync"));

    public static final StreamCodec<FriendlyByteBuf, WolfSyncPayload> STREAM_CODEC =
        StreamCodec.ofMember(
            WolfSyncPayload::write,
            WolfSyncPayload::new
        );

    private WolfSyncPayload(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeBoolean(active);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
