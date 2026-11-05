package com.enderdevelopment.alphawolf.mixin;

import com.enderdevelopment.alphawolf.WolfManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Reduces the damage taken by a transformed Alpha Wolf (wolf takes less damage).
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {

    /** Percentage of incoming damage that is negated while transformed. */
    private static final float DAMAGE_REDUCTION = 0.5f;

    @ModifyVariable(
        method = "damage",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 0
    )
    private float alphawolf$reduceWolfDamage(float amount, ServerLevel level, DamageSource source, float original) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player && WolfManager.getWolf(player)) {
            return amount * (1.0f - DAMAGE_REDUCTION);
        }
        return amount;
    }
}
