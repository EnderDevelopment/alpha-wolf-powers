package com.enderdevelopment.alphawolf;

import com.enderdevelopment.alphawolf.net.WolfSyncPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks which players are currently transformed into the Alpha Wolf and applies
 * the wolf powers (extra hearts, night vision, red/black particles and a red glow).
 */
public final class WolfManager {
    /** Extra max health while transformed: 12 HP = +6 hearts, so 16 hearts total. */
    public static final double EXTRA_HEALTH = 12.0;
    private static final UUID BOOST_ID = UUID.fromString("1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d");

    private static final Map<UUID, Boolean> WOLVES = new ConcurrentHashMap<>();
    private static int particleTick = 0;

    private WolfManager() {
    }

    public static boolean getWolf(Player player) {
        return WOLVES.getOrDefault(player.getUUID(), false);
    }

    public static void setWolf(ServerPlayer player, boolean active) {
        WOLVES.put(player.getUUID(), active);

        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.removeModifier(BOOST_ID);
            if (active) {
                maxHealth.addPermanentModifier(new AttributeModifier(
                    BOOST_ID,
                    "alphawolf.extra_health",
                    EXTRA_HEALTH,
                    AttributeModifier.Operation.ADDITION
                ));
                player.setHealth(player.getMaxHealth());
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 0, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 0, false, false));
                player.setGlowing(true);
            } else {
                player.removeEffect(MobEffects.NIGHT_VISION);
                player.removeEffect(MobEffects.MOVEMENT_SPEED);
                player.setGlowing(false);
                if (player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
            }
        }

        sync(player);
    }

    public static void tickWolf(ServerPlayer player) {
        particleTick++;
        if (particleTick % 10 == 0) {
            player.serverLevel().sendParticles(
                ParticleTypes.SMOKE, // black smoke for the black coat
                player.getX(), player.getY() + 0.4, player.getZ(),
                2, 0.4, 0.6, 0.4, 0.01
            );
            player.serverLevel().sendParticles(
                ParticleTypes.FLAME, // red flame for the red coat
                player.getX(), player.getY() + 1.1, player.getZ(),
                1, 0.3, 0.3, 0.3, 0.01
            );
        }
        player.setGlowing(true);
    }

    private static void sync(ServerPlayer player) {
        ServerPlayNetworking.send(player, new WolfSyncPayload(getWolf(player)));
    }
}
