package io.github.pouffy.agrestic.common.effect;

import io.github.pouffy.agrestic.init.AgresticEffects;
import io.github.pouffy.agrestic.network.FirePowerAttackPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class FirePowerEffect extends AgresticEffect {
    public FirePowerEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFCE6D);
    }

    public static void doFirePowerAttack(LivingEntity entity) {
        if (entity == null) return;
        if (entity.level().isClientSide()) return;
        MobEffectInstance effect = entity.getEffect(AgresticEffects.FIRE_POWER);
        if (effect == null) return;

        if (entity.isInWaterOrRain()) {
            return;
        }
        double f = 0.005;

        double d1 = entity.getLookAngle().x * 40d;
        double d2 = entity.getLookAngle().y * 40d;
        double d3 = entity.getLookAngle().z * 40d;
        Fireball fb;
        if (effect.getAmplifier() > 0) {
            fb = new LargeFireball(entity.level(), entity, new Vec3(d1 + entity.getRandom().nextGaussian() * f, d2, d3 + entity.getRandom().nextGaussian() * f), effect.getAmplifier());
        } else {
            fb = new SmallFireball(entity.level(), entity, new Vec3(d1 + entity.getRandom().nextGaussian() * f, d2, d3 + entity.getRandom().nextGaussian() * f));
        }
        fb.setPos(fb.getX(), entity.getY() + (double) (entity.getEyeHeight()), fb.getZ());
        if (entity.level().collidesWithSuffocatingBlock(fb, fb.getBoundingBox().inflate(0.001, 0.001, 0.001))) {
            return;
        }
        entity.level().playSound(null, entity.getX() + entity.getLookAngle().x, entity.getY() + entity.getEyeHeight() + entity.getLookAngle().y, entity.getZ() + entity.getLookAngle().z,
                SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 1.0F,
                0.4F / (entity.getRandom().nextFloat() * 0.4F + 1.2F));
        entity.level().addFreshEntity(fb);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerAttack(PlayerInteractEvent.LeftClickEmpty event) {
        if (!event.getEntity().hasEffect(AgresticEffects.FIRE_POWER)) return;
        Player player = event.getEntity();
        if (!player.swinging && !player.isShiftKeyDown()) {
            PacketDistributor.sendToServer(new FirePowerAttackPayload());
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerAttack(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.getEntity().hasEffect(AgresticEffects.FIRE_POWER)) return;
        Player player = event.getEntity();
        if (!player.swinging && !player.isShiftKeyDown()) {
            PacketDistributor.sendToServer(new FirePowerAttackPayload());
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerAttack(AttackEntityEvent event) {
        if (!event.getEntity().hasEffect(AgresticEffects.FIRE_POWER)) return;
        Player player = event.getEntity();
        if (!player.swinging && !player.isShiftKeyDown()) {
            PacketDistributor.sendToServer(new FirePowerAttackPayload());
        }
    }
}
