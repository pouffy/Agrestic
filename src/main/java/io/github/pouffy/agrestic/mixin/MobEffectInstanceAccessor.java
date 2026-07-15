package io.github.pouffy.agrestic.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(MobEffectInstance.class)
public interface MobEffectInstanceAccessor {

    @Accessor
    void setCures(Set<EffectCure> cures);
}
