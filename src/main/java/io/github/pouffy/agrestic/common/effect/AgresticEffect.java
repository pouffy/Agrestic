package io.github.pouffy.agrestic.common.effect;

import com.pouffydev.krystal_core.content.effect.ExtendedMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class AgresticEffect extends ExtendedMobEffect {
    public AgresticEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
}
