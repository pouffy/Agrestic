package io.github.pouffy.agrestic.common.effect;

import io.github.pouffy.agrestic.init.AgresticEffects;
import io.github.pouffy.agrestic.mixin.MobEffectInstanceAccessor;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.EffectCure;

import javax.annotation.Nullable;
import java.util.Set;

public class TipsyEffect extends AgresticEffect {
    public TipsyEffect() {
        super(MobEffectCategory.HARMFUL, 7900290);
    }

    @Override
    public boolean tick(LivingEntity entity, @Nullable MobEffectInstance effectInstance, int amplifier) {
        if (entity != null && !entity.level().isClientSide() && amplifier > 0) {
            if (amplifier > 2) {
                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 400, 1, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 1, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400, 0, false, false));
            } else {
                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 400, 0, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 0, false, false));
            }
            Set<EffectCure> cures = Set.of();
            MobEffectInstance nausea = entity.getEffect(MobEffects.CONFUSION);
            MobEffectInstance slowness = entity.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
            MobEffectInstance blindness = entity.getEffect(MobEffects.BLINDNESS);
            MobEffectInstance tipsy = entity.getEffect(AgresticEffects.TIPSY);
            if (nausea != null) ((MobEffectInstanceAccessor) nausea).setCures(cures);
            if (slowness != null) ((MobEffectInstanceAccessor) slowness).setCures(cures);
            if (blindness != null) ((MobEffectInstanceAccessor) blindness).setCures(cures);
            if (tipsy != null) ((MobEffectInstanceAccessor) tipsy).setCures(cures);
        }
        return true;
    }

    public boolean shouldTickEffect(@Nullable MobEffectInstance effectInstance, @Nullable LivingEntity entity, int ticksRemaining, int amplifier) {
        return (amplifier > 1) && ((ticksRemaining % 100) == 0);
    }

    @Override
    public boolean shouldCureEffect(MobEffectInstance effectInstance, ItemStack stack, LivingEntity entity) {
        return false;
    }
}
