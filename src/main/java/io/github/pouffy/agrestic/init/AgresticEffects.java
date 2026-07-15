package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.effect.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AgresticEffects {
    public static final DeferredRegister<MobEffect> HELPER = Agrestic.getRegistryHelper().createRegister(Registries.MOB_EFFECT);

    public static final DeferredHolder<MobEffect, IronskinEffect> IRONSKIN = register("ironskin", IronskinEffect::new);
    public static final DeferredHolder<MobEffect, BlazingTrailEffect> BLAZING_TRAIL = register("blazing_trail", BlazingTrailEffect::new);
    public static final DeferredHolder<MobEffect, ShameEffect> SHAME = register("shame", ShameEffect::new);
    public static final DeferredHolder<MobEffect, FullmetalEffect> FULLMETAL = register("fullmetal", FullmetalEffect::new);
    public static final DeferredHolder<MobEffect, FirePowerEffect> FIRE_POWER = register("fire_power", FirePowerEffect::new);

    public static final DeferredHolder<MobEffect, FullEffect> FULL = register("full", FullEffect::new);
    public static final DeferredHolder<MobEffect, MagicResistanceEffect> MAGIC_RESISTANCE = register("magic_resistance", MagicResistanceEffect::new);
    public static final DeferredHolder<MobEffect, WitherWardEffect> WITHER_WARD = register("wither_ward", WitherWardEffect::new);
    public static final DeferredHolder<MobEffect, UndyingEffect> UNDYING = register("undying", UndyingEffect::new);

    public static final DeferredHolder<MobEffect, TipsyEffect> TIPSY = register("tipsy", TipsyEffect::new);

    private static <P extends MobEffect> DeferredHolder<MobEffect, P> register(String name, Supplier<P> supplier) {
        return HELPER.register(name, supplier);
    }

    public static void staticInit() {}
}
