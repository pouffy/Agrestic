package io.github.pouffy.agrestic.common.effect;

import com.pouffydev.krystal_core.core.KCTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class WitherWardEffect extends AgresticEffect {

    public WitherWardEffect() {
        super(MobEffectCategory.BENEFICIAL, 11842760);
    }

    @Override
    public float modifyIncomingAttackDamage(LivingEntity entity, MobEffectInstance effectInstance, DamageSource source, float baseAmount) {
        if (source.is(DamageTypes.WITHER)) {
            return baseAmount / (2F * (effectInstance.getAmplifier() + 1F));
        }
        return baseAmount;
    }
}
