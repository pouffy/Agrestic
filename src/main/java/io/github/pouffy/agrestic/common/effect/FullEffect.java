package io.github.pouffy.agrestic.common.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class FullEffect extends AgresticEffect {

    public FullEffect() {
        super(MobEffectCategory.NEUTRAL, 6563840);
    }

    @Override
    public float modifyIncomingAttackDamage(LivingEntity entity, MobEffectInstance effectInstance, DamageSource source, float baseAmount) {
        if (source.is(DamageTypes.STARVE)) {
            return Math.max(baseAmount - (effectInstance.getAmplifier() + 1), 0);
        }
        return baseAmount;
    }
}
