package io.github.pouffy.agrestic.common.effect;

import io.github.pouffy.agrestic.network.ShameFXPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;

public class ShameEffect extends AgresticEffect {
    public ShameEffect() {
        super(MobEffectCategory.HARMFUL, 16409650);
    }

    @Override
    public boolean tick(LivingEntity entity, @Nullable MobEffectInstance effectInstance, int amplifier) {
        if (entity instanceof ServerPlayer serverPlayer && !entity.level().isClientSide) {
            PacketDistributor.sendToPlayer(serverPlayer, new ShameFXPayload(entity.level().random.nextInt(6) + 6, entity.position(), entity.getDeltaMovement(), entity.getBbWidth(), entity.getBbHeight()));
        }
        return true;
    }

    public boolean shouldTickEffect(@Nullable MobEffectInstance effectInstance, @Nullable LivingEntity entity, int ticksRemaining, int amplifier) {
        return (ticksRemaining % 5) == 0;
    }
}
