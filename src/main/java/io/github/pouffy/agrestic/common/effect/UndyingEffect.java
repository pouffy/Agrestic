package io.github.pouffy.agrestic.common.effect;

import io.github.pouffy.agrestic.init.AgresticEffects;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber
public class UndyingEffect extends AgresticEffect {
    public UndyingEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xEADB84);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return;
        if (!(event.getEntity() instanceof Player)) return;

        LivingEntity entity = event.getEntity();

        MobEffectInstance effect = entity.getEffect(AgresticEffects.UNDYING);
        if (effect == null) return;

        entity.removeEffect(AgresticEffects.UNDYING);

        entity.setHealth(1.0F);
        entity.removeEffectsCuredBy(net.neoforged.neoforge.common.EffectCures.PROTECTED_BY_TOTEM);
        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
        entity.level().broadcastEntityEvent(entity, (byte)35);

        if (effect.getAmplifier() > 0) {
            entity.addEffect(new MobEffectInstance(
                    AgresticEffects.UNDYING,
                    effect.getDuration(),
                    effect.getAmplifier() - 1,
                    effect.isAmbient(),
                    effect.isVisible()
            ));
        }

        event.setCanceled(true);
    }
}
