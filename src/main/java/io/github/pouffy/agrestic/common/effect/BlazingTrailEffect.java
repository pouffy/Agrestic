package io.github.pouffy.agrestic.common.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;

public class BlazingTrailEffect extends AgresticEffect {
    public BlazingTrailEffect() {
        super(MobEffectCategory.BENEFICIAL, 16738816);
    }

    @Override
    public boolean tick(LivingEntity entity, @Nullable MobEffectInstance effectInstance, int amplifier) {
        if (!entity.level().isClientSide() && entity.onGround()) {
            Level world = entity.level();
            BlockPos pos = entity.blockPosition();
            if (world.getBlockState(pos).isAir() && world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), Direction.UP)) {
                world.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
            }
        }
        return true;
    }

    public boolean shouldTickEffect(@Nullable MobEffectInstance effectInstance, @Nullable LivingEntity entity, int ticksRemaining, int amplifier) {
        return (ticksRemaining % 10) == 0;
    }
}
