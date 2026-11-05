package com.enderdevelopment.alphawolf.net;

import com.enderdevelopment.alphawolf.AlphaWolfMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Client-to-server payload that asks the server to toggle the wolf transformation.
 */
public record WolfTogglePayload(boolean active) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WolfTogglePayload> TYPE =
        new CustomPacketPayload.Type<>(AlphaWolfMod.id("wolf_toggle"));

    public static final StreamCodec<FriendlyByteBuf, WolfTogglePayload> STREAM_CODEC =
        StreamCodec.ofMember(
            WolfTogglePayload::write,
            WolfTogglePayload::new
        );

    private WolfTogglePayload(FriendlyByteBuf buf) {
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
