package io.github.pouffy.agrestic.common.effect;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticAttachmentTypes;
import io.github.pouffy.agrestic.init.AgresticEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;

import javax.annotation.Nullable;

@EventBusSubscriber
public class FullmetalEffect extends AgresticEffect {
    public FullmetalEffect() {
        super(MobEffectCategory.BENEFICIAL, 8220521);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, Agrestic.location("fullmetal/movement_speed"), -1F, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(NeoForgeMod.SWIM_SPEED, Agrestic.location("fullmetal/swim_speed"), -1F, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(Attributes.FLYING_SPEED, Agrestic.location("fullmetal/flying_speed"), -1F, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(Attributes.ATTACK_SPEED, Agrestic.location("fullmetal/attack_speed"), -0.5F, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, Agrestic.location("fullmetal/knockback_resistance"), 9001F, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public boolean tick(LivingEntity entity, @Nullable MobEffectInstance effectInstance, int amplifier) {
        entity.setJumping(false);
        entity.setSprinting(false);
        if ((entity.getVehicle() != null) && (entity.getVehicle() instanceof LivingEntity)) {
            entity.stopRiding();
        }
        if (!entity.onGround() && !entity.isNoGravity()) {
            if ((entity.isInWater() || entity.isInLava())) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.27 / 4, 0));
            } else if (entity.isFallFlying()) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.32 / 4, 0));
            } else {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.07, 0));
            }
        }
        if (entity instanceof Player player) {
            player.getAbilities().flying = false;
        }
        return true;
    }

    @Override
    public void onApplication(@Nullable MobEffectInstance effectInstance, @Nullable Entity source, LivingEntity entity, int amplifier) {
        entity.setData(AgresticAttachmentTypes.IRON_SKIN, new IronSkin(true));
    }

    @Override
    public void onExpiry(MobEffectInstance effectInstance, LivingEntity entity) {
        entity.removeData(AgresticAttachmentTypes.IRON_SKIN);
    }

    @SubscribeEvent
    public static void onJumpEvent(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getEffect(AgresticEffects.FULLMETAL) != null) {
            entity.setDeltaMovement(entity.getDeltaMovement().x(), 0, entity.getDeltaMovement().z());
        }
    }

    @SubscribeEvent
    public static void onMountEvent(EntityMountEvent event) {
        if (!event.isMounting()) return;
        if (!(event.getEntityMounting() instanceof LivingEntity top)) return;
        if (!(event.getEntityBeingMounted() instanceof LivingEntity)) return;
        if (top.getEffect(AgresticEffects.FULLMETAL) != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onSleepEvent(CanPlayerSleepEvent event) {
        if (event.getEntity().getEffect(AgresticEffects.FULLMETAL) != null) {
            event.setProblem(Player.BedSleepingProblem.OTHER_PROBLEM);
        }
    }

    @SubscribeEvent
    public static void onFallEvent(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        float d = event.getDistance();
        if (entity.getEffect(AgresticEffects.FULLMETAL) != null && (d >= 0.6F) && !entity.isInWater() && !entity.isInLava()) {
            entity.playSound(SoundEvents.ANVIL_LAND, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onEntityDamage(LivingIncomingDamageEvent event) {
        DamageSource source = event.getSource();
        LivingEntity entity = event.getEntity();

        if (entity.hasEffect(AgresticEffects.FULLMETAL) && !source.is(DamageTypeTags.BYPASSES_EFFECTS) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            event.setAmount(0);
            return;
        }
    }
}
