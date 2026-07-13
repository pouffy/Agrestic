package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.fluid.AgresticFluidType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class AgresticFluids {

    public static FlowingFluid OLIVE_OIL;
    public static FlowingFluid FLOWING_OLIVE_OIL;
    public static FlowingFluid IRONBERRY_JUICE;
    public static FlowingFluid FLOWING_IRONBERRY_JUICE;
    public static FlowingFluid WILDBERRY_JUICE;
    public static FlowingFluid FLOWING_WILDBERRY_JUICE;
    public static FlowingFluid GRAPE_JUICE;
    public static FlowingFluid FLOWING_GRAPE_JUICE;
    public static FlowingFluid APPLE_JUICE;
    public static FlowingFluid FLOWING_APPLE_JUICE;
    public static FlowingFluid ALE_WORT;
    public static FlowingFluid FLOWING_ALE_WORT;
    public static FlowingFluid GOLDEN_APPLE_JUICE;
    public static FlowingFluid FLOWING_GOLDEN_APPLE_JUICE;
    public static FlowingFluid VANTA_OIL;
    public static FlowingFluid FLOWING_VANTA_OIL;
    public static FlowingFluid ALE;
    public static FlowingFluid FLOWING_ALE;
    public static FlowingFluid CIDER;
    public static FlowingFluid FLOWING_CIDER;
    public static FlowingFluid IRON_WINE;
    public static FlowingFluid FLOWING_IRON_WINE;
    public static FlowingFluid MEAD;
    public static FlowingFluid FLOWING_MEAD;
    public static FlowingFluid WILDBERRY_WINE;
    public static FlowingFluid FLOWING_WILDBERRY_WINE;
    public static FlowingFluid WINE;
    public static FlowingFluid FLOWING_WINE;
    public static FlowingFluid AMBROSIA;
    public static FlowingFluid FLOWING_AMBROSIA;

    public static void staticInit() {
        OLIVE_OIL = new BaseFlowingFluid.Flowing(Properties.OLIVE_OIL);
        FLOWING_OLIVE_OIL = new BaseFlowingFluid.Flowing(Properties.OLIVE_OIL);
        IRONBERRY_JUICE = new BaseFlowingFluid.Source(Properties.IRONBERRY_JUICE);
        FLOWING_IRONBERRY_JUICE = new BaseFlowingFluid.Flowing(Properties.IRONBERRY_JUICE);
        WILDBERRY_JUICE = new BaseFlowingFluid.Source(Properties.WILDBERRY_JUICE);
        FLOWING_WILDBERRY_JUICE = new BaseFlowingFluid.Flowing(Properties.WILDBERRY_JUICE);
        GRAPE_JUICE = new BaseFlowingFluid.Source(Properties.GRAPE_JUICE);
        FLOWING_GRAPE_JUICE = new BaseFlowingFluid.Flowing(Properties.GRAPE_JUICE);
        APPLE_JUICE = new BaseFlowingFluid.Source(Properties.APPLE_JUICE);
        FLOWING_APPLE_JUICE = new BaseFlowingFluid.Flowing(Properties.APPLE_JUICE);
        ALE_WORT = new BaseFlowingFluid.Source(Properties.ALE_WORT);
        FLOWING_ALE_WORT = new BaseFlowingFluid.Flowing(Properties.ALE_WORT);
        GOLDEN_APPLE_JUICE = new BaseFlowingFluid.Source(Properties.GOLDEN_APPLE_JUICE);
        FLOWING_GOLDEN_APPLE_JUICE = new BaseFlowingFluid.Flowing(Properties.GOLDEN_APPLE_JUICE);
        VANTA_OIL = new BaseFlowingFluid.Source(Properties.VANTA_OIL);
        FLOWING_VANTA_OIL = new BaseFlowingFluid.Flowing(Properties.VANTA_OIL);
        ALE = new BaseFlowingFluid.Source(Properties.ALE);
        FLOWING_ALE = new BaseFlowingFluid.Flowing(Properties.ALE);
        CIDER = new BaseFlowingFluid.Source(Properties.CIDER);
        FLOWING_CIDER = new BaseFlowingFluid.Flowing(Properties.CIDER);
        IRON_WINE = new BaseFlowingFluid.Source(Properties.IRON_WINE);
        FLOWING_IRON_WINE = new BaseFlowingFluid.Flowing(Properties.IRON_WINE);
        MEAD = new BaseFlowingFluid.Source(Properties.MEAD);
        FLOWING_MEAD = new BaseFlowingFluid.Flowing(Properties.MEAD);
        WILDBERRY_WINE = new BaseFlowingFluid.Source(Properties.WILDBERRY_WINE);
        FLOWING_WILDBERRY_WINE = new BaseFlowingFluid.Flowing(Properties.WILDBERRY_WINE);
        WINE = new BaseFlowingFluid.Source(Properties.WINE);
        FLOWING_WINE = new BaseFlowingFluid.Flowing(Properties.WINE);
        AMBROSIA = new BaseFlowingFluid.Source(Properties.AMBROSIA);
        FLOWING_AMBROSIA = new BaseFlowingFluid.Flowing(Properties.AMBROSIA);
    }

    public static void registerAll() {
        staticInit();
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("olive_oil"), OLIVE_OIL);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_olive_oil"), FLOWING_OLIVE_OIL);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("ironberry_juice"), IRONBERRY_JUICE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_ironberry_juice"), FLOWING_IRONBERRY_JUICE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("wildberry_juice"), WILDBERRY_JUICE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_wildberry_juice"), FLOWING_WILDBERRY_JUICE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("grape_juice"), GRAPE_JUICE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_grape_juice"), FLOWING_GRAPE_JUICE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("apple_juice"), APPLE_JUICE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_apple_juice"), FLOWING_APPLE_JUICE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("ale_wort"), ALE_WORT);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_ale_wort"), FLOWING_ALE_WORT);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("golden_apple_juice"), GOLDEN_APPLE_JUICE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_golden_apple_juice"), FLOWING_GOLDEN_APPLE_JUICE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("vanta_oil"), VANTA_OIL);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_vanta_oil"), FLOWING_VANTA_OIL);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("ale"), ALE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_ale"), FLOWING_ALE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("cider"), CIDER);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_cider"), FLOWING_CIDER);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("iron_wine"), IRON_WINE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_iron_wine"), FLOWING_IRON_WINE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("mead"), MEAD);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_mead"), FLOWING_MEAD);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("wildberry_wine"), WILDBERRY_WINE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_wildberry_wine"), FLOWING_WILDBERRY_WINE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("wine"), WINE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_wine"), FLOWING_WINE);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("ambrosia"), AMBROSIA);
        Registry.register(BuiltInRegistries.FLUID, Agrestic.location("flowing_ambrosia"), FLOWING_AMBROSIA);
    }

    public static class Types {
        public static final FluidType OLIVE_OIL = new AgresticFluidType();
        public static final FluidType IRONBERRY_JUICE = new AgresticFluidType();
        public static final FluidType WILDBERRY_JUICE = new AgresticFluidType();
        public static final FluidType GRAPE_JUICE = new AgresticFluidType();
        public static final FluidType APPLE_JUICE = new AgresticFluidType();
        public static final FluidType ALE_WORT = new AgresticFluidType();
        public static final FluidType GOLDEN_APPLE_JUICE = new AgresticFluidType();

        public static final FluidType VANTA_OIL = new AgresticFluidType();

        public static final FluidType ALE = new AgresticFluidType();
        public static final FluidType CIDER = new AgresticFluidType();
        public static final FluidType IRON_WINE = new AgresticFluidType();
        public static final FluidType MEAD = new AgresticFluidType();
        public static final FluidType WILDBERRY_WINE = new AgresticFluidType();
        public static final FluidType WINE = new AgresticFluidType();
        public static final FluidType AMBROSIA = new AgresticFluidType();

        public static void registerAll() {
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("olive_oil"), OLIVE_OIL);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("ironberry_juice"), IRONBERRY_JUICE);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("wildberry_juice"), WILDBERRY_JUICE);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("grape_juice"), GRAPE_JUICE);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("apple_juice"), APPLE_JUICE);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("ale_wort"), ALE_WORT);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("golden_apple_juice"), GOLDEN_APPLE_JUICE);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("vanta_oil"), VANTA_OIL);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("ale"), ALE);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("cider"), CIDER);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("iron_wine"), IRON_WINE);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("mead"), MEAD);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("wildberry_wine"), WILDBERRY_WINE);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("wine"), WINE);
            Registry.register(NeoForgeRegistries.FLUID_TYPES, Agrestic.location("ambrosia"), AMBROSIA);
        }
    }

    public static class Properties {
        public static final BaseFlowingFluid.Properties OLIVE_OIL = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.OLIVE_OIL, () -> AgresticFluids.OLIVE_OIL, () -> AgresticFluids.FLOWING_OLIVE_OIL);
        public static final BaseFlowingFluid.Properties IRONBERRY_JUICE = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.IRONBERRY_JUICE, () -> AgresticFluids.IRONBERRY_JUICE, () -> AgresticFluids.FLOWING_IRONBERRY_JUICE);
        public static final BaseFlowingFluid.Properties WILDBERRY_JUICE = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.WILDBERRY_JUICE, () -> AgresticFluids.WILDBERRY_JUICE, () -> AgresticFluids.FLOWING_WILDBERRY_JUICE);
        public static final BaseFlowingFluid.Properties GRAPE_JUICE = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.GRAPE_JUICE, () -> AgresticFluids.GRAPE_JUICE, () -> AgresticFluids.FLOWING_GRAPE_JUICE);
        public static final BaseFlowingFluid.Properties APPLE_JUICE = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.APPLE_JUICE, () -> AgresticFluids.APPLE_JUICE, () -> AgresticFluids.FLOWING_APPLE_JUICE);
        public static final BaseFlowingFluid.Properties ALE_WORT = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.ALE_WORT, () -> AgresticFluids.ALE_WORT, () -> AgresticFluids.FLOWING_ALE_WORT);
        public static final BaseFlowingFluid.Properties GOLDEN_APPLE_JUICE = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.GOLDEN_APPLE_JUICE, () -> AgresticFluids.GOLDEN_APPLE_JUICE, () -> AgresticFluids.FLOWING_GOLDEN_APPLE_JUICE);
        public static final BaseFlowingFluid.Properties VANTA_OIL = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.VANTA_OIL, () -> AgresticFluids.VANTA_OIL, () -> AgresticFluids.FLOWING_VANTA_OIL);
        public static final BaseFlowingFluid.Properties ALE = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.ALE, () -> AgresticFluids.ALE, () -> AgresticFluids.FLOWING_ALE);
        public static final BaseFlowingFluid.Properties CIDER = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.CIDER, () -> AgresticFluids.CIDER, () -> AgresticFluids.FLOWING_CIDER);
        public static final BaseFlowingFluid.Properties IRON_WINE = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.IRON_WINE, () -> AgresticFluids.IRON_WINE, () -> AgresticFluids.FLOWING_IRON_WINE);
        public static final BaseFlowingFluid.Properties MEAD = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.MEAD, () -> AgresticFluids.MEAD, () -> AgresticFluids.FLOWING_MEAD);
        public static final BaseFlowingFluid.Properties WILDBERRY_WINE = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.WILDBERRY_WINE, () -> AgresticFluids.WILDBERRY_WINE, () -> AgresticFluids.FLOWING_WILDBERRY_WINE);
        public static final BaseFlowingFluid.Properties WINE = new BaseFlowingFluid.Properties(() -> AgresticFluids.Types.WINE, () -> AgresticFluids.WINE, () -> AgresticFluids.FLOWING_WINE);
        public static final BaseFlowingFluid.Properties AMBROSIA = new BaseFlowingFluid.Properties(() ->AgresticFluids.Types.AMBROSIA ,() -> AgresticFluids.AMBROSIA ,() -> AgresticFluids.FLOWING_AMBROSIA);
    }
}
