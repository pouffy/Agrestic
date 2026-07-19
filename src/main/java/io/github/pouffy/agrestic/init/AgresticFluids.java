package io.github.pouffy.agrestic.init;

import com.pouffydev.krystal_core.KrystalCore;
import com.pouffydev.krystal_core.content.item.HoneyBucketItem;
import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemDefinition;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.fluid.AgresticBucketWrapper;
import io.github.pouffy.agrestic.core.fluid.AgresticFluidType;
import io.github.pouffy.agrestic.core.fluid.VirtualFluid;
import io.github.pouffy.agrestic.core.item.AgresticBucketItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static io.github.pouffy.agrestic.init.AgresticItems.registerBucket;

@EventBusSubscriber
public class AgresticFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Agrestic.MODID);

    public static final Supplier<VirtualFluid> OLIVE_OIL = FLUIDS.register("olive_oil", () -> new VirtualFluid(Properties.OLIVE_OIL, true));
    public static final Supplier<VirtualFluid> FLOWING_OLIVE_OIL = FLUIDS.register("flowing_olive_oil", () -> new VirtualFluid(Properties.OLIVE_OIL, false));
    public static final Supplier<VirtualFluid> IRONBERRY_JUICE = FLUIDS.register("ironberry_juice", () -> new VirtualFluid(Properties.IRONBERRY_JUICE, true));
    public static final Supplier<VirtualFluid> FLOWING_IRONBERRY_JUICE = FLUIDS.register("flowing_ironberry_juice", () -> new VirtualFluid(Properties.IRONBERRY_JUICE, false));
    public static final Supplier<VirtualFluid> SWEET_BERRY_JUICE = FLUIDS.register("sweet_berry_juice", () -> new VirtualFluid(Properties.SWEET_BERRY_JUICE, true));
    public static final Supplier<VirtualFluid> FLOWING_SWEET_BERRY_JUICE = FLUIDS.register("flowing_sweet_berry_juice", () -> new VirtualFluid(Properties.SWEET_BERRY_JUICE, false));
    public static final Supplier<VirtualFluid> GRAPE_JUICE = FLUIDS.register("grape_juice", () -> new VirtualFluid(Properties.GRAPE_JUICE, true));
    public static final Supplier<VirtualFluid> FLOWING_GRAPE_JUICE = FLUIDS.register("flowing_grape_juice", () -> new VirtualFluid(Properties.GRAPE_JUICE, false));
    public static final Supplier<VirtualFluid> APPLE_JUICE = FLUIDS.register("apple_juice", () -> new VirtualFluid(Properties.APPLE_JUICE, true));
    public static final Supplier<VirtualFluid> FLOWING_APPLE_JUICE = FLUIDS.register("flowing_apple_juice", () -> new VirtualFluid(Properties.APPLE_JUICE, false));
    public static final Supplier<VirtualFluid> ALE_WORT = FLUIDS.register("ale_wort", () -> new VirtualFluid(Properties.ALE_WORT, true));
    public static final Supplier<VirtualFluid> FLOWING_ALE_WORT = FLUIDS.register("flowing_ale_wort", () -> new VirtualFluid(Properties.ALE_WORT, false));
    public static final Supplier<VirtualFluid> GOLDEN_APPLE_JUICE = FLUIDS.register("golden_apple_juice", () -> new VirtualFluid(Properties.GOLDEN_APPLE_JUICE, true));
    public static final Supplier<VirtualFluid> FLOWING_GOLDEN_APPLE_JUICE = FLUIDS.register("flowing_golden_apple_juice", () -> new VirtualFluid(Properties.GOLDEN_APPLE_JUICE, false));
    public static final Supplier<VirtualFluid> VANTA_OIL = FLUIDS.register("vanta_oil", () -> new VirtualFluid(Properties.VANTA_OIL, true));
    public static final Supplier<VirtualFluid> FLOWING_VANTA_OIL = FLUIDS.register("flowing_vanta_oil", () -> new VirtualFluid(Properties.VANTA_OIL, false));
    public static final Supplier<VirtualFluid> ALE = FLUIDS.register("ale", () -> new VirtualFluid(Properties.ALE, true));
    public static final Supplier<VirtualFluid> FLOWING_ALE = FLUIDS.register("flowing_ale", () -> new VirtualFluid(Properties.ALE, false));
    public static final Supplier<VirtualFluid> CIDER = FLUIDS.register("cider", () -> new VirtualFluid(Properties.CIDER, true));
    public static final Supplier<VirtualFluid> FLOWING_CIDER = FLUIDS.register("flowing_cider", () -> new VirtualFluid(Properties.CIDER, false));
    public static final Supplier<VirtualFluid> IRON_WINE = FLUIDS.register("iron_wine", () -> new VirtualFluid(Properties.IRON_WINE, true));
    public static final Supplier<VirtualFluid> FLOWING_IRON_WINE = FLUIDS.register("flowing_iron_wine", () -> new VirtualFluid(Properties.IRON_WINE, false));
    public static final Supplier<VirtualFluid> MEAD = FLUIDS.register("mead", () -> new VirtualFluid(Properties.MEAD, true));
    public static final Supplier<VirtualFluid> FLOWING_MEAD = FLUIDS.register("flowing_mead", () -> new VirtualFluid(Properties.MEAD, false));
    public static final Supplier<VirtualFluid> SWEET_BERRY_WINE = FLUIDS.register("sweet_berry_wine", () -> new VirtualFluid(Properties.SWEET_BERRY_WINE, true));
    public static final Supplier<VirtualFluid> FLOWING_SWEET_BERRY_WINE = FLUIDS.register("flowing_sweet_berry_wine", () -> new VirtualFluid(Properties.SWEET_BERRY_WINE, false));
    public static final Supplier<VirtualFluid> WINE = FLUIDS.register("wine", () -> new VirtualFluid(Properties.WINE, true));
    public static final Supplier<VirtualFluid> FLOWING_WINE = FLUIDS.register("flowing_wine", () -> new VirtualFluid(Properties.WINE, false));
    public static final Supplier<VirtualFluid> AMBROSIA = FLUIDS.register("ambrosia", () -> new VirtualFluid(Properties.AMBROSIA, true));
    public static final Supplier<VirtualFluid> FLOWING_AMBROSIA = FLUIDS.register("flowing_ambrosia", () -> new VirtualFluid(Properties.AMBROSIA, false));

    public static final ItemDefinition<BucketItem> APPLE_JUICE_BUCKET = registerBucket("apple_juice", APPLE_JUICE, p -> p);
    public static final ItemDefinition<BucketItem> GOLDEN_APPLE_JUICE_BUCKET = registerBucket("golden_apple_juice", GOLDEN_APPLE_JUICE, p -> p);
    public static final ItemDefinition<BucketItem> GRAPE_JUICE_BUCKET = registerBucket("grape_juice", GRAPE_JUICE, p -> p);
    public static final ItemDefinition<BucketItem> SWEET_BERRY_JUICE_BUCKET = registerBucket("sweet_berry_juice", SWEET_BERRY_JUICE, p -> p);
    public static final ItemDefinition<BucketItem> IRONBERRY_JUICE_BUCKET = registerBucket("ironberry_juice", IRONBERRY_JUICE, p -> p);
    public static final ItemDefinition<BucketItem> ALE_WORT_BUCKET = registerBucket("ale_wort", ALE_WORT, p -> p);
    public static final ItemDefinition<BucketItem> OLIVE_OIL_BUCKET = registerBucket("olive_oil", OLIVE_OIL, p -> p);
    public static final ItemDefinition<BucketItem> VANTA_OIL_BUCKET = registerBucket("vanta_oil", VANTA_OIL, p -> p);

    public static void staticInit(IEventBus bus) {
        FLUIDS.register(bus);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item.getClass() == AgresticBucketItem.class)
                event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new AgresticBucketWrapper(stack), item);
        }
        if (KrystalCore.HONEY_BUCKET.isBound()) {
            event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new AgresticBucketWrapper(stack), KrystalCore.HONEY_BUCKET.get());
        }
    }

    public static class Types {
        public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Agrestic.MODID);


        public static final Supplier<FluidType> OLIVE_OIL = FLUID_TYPES.register("olive_oil", () -> new AgresticFluidType("olive_oil"));
        public static final Supplier<FluidType> IRONBERRY_JUICE = FLUID_TYPES.register("ironberry_juice", () -> new AgresticFluidType("ironberry_juice"));
        public static final Supplier<FluidType> SWEET_BERRY_JUICE = FLUID_TYPES.register("sweet_berry_juice", () -> new AgresticFluidType("sweet_berry_juice"));
        public static final Supplier<FluidType> GRAPE_JUICE = FLUID_TYPES.register("grape_juice", () -> new AgresticFluidType("grape_juice"));
        public static final Supplier<FluidType> APPLE_JUICE = FLUID_TYPES.register("apple_juice", () -> new AgresticFluidType("apple_juice"));
        public static final Supplier<FluidType> ALE_WORT = FLUID_TYPES.register("ale_wort", () -> new AgresticFluidType("ale_wort"));
        public static final Supplier<FluidType> GOLDEN_APPLE_JUICE = FLUID_TYPES.register("golden_apple_juice", () -> new AgresticFluidType("golden_apple_juice"));

        public static final Supplier<FluidType> VANTA_OIL = FLUID_TYPES.register("vanta_oil", () -> new AgresticFluidType("vanta_oil"));

        public static final Supplier<FluidType> ALE = FLUID_TYPES.register("ale", () -> new AgresticFluidType("ale").booze());
        public static final Supplier<FluidType> CIDER = FLUID_TYPES.register("cider", () -> new AgresticFluidType("cider").booze());
        public static final Supplier<FluidType> IRON_WINE = FLUID_TYPES.register("iron_wine", () -> new AgresticFluidType("iron_wine").booze());
        public static final Supplier<FluidType> MEAD = FLUID_TYPES.register("mead", () -> new AgresticFluidType("mead").booze());
        public static final Supplier<FluidType> SWEET_BERRY_WINE = FLUID_TYPES.register("sweet_berry_wine", () -> new AgresticFluidType("sweet_berry_wine").booze());
        public static final Supplier<FluidType> WINE = FLUID_TYPES.register("wine", () -> new AgresticFluidType("wine").booze());
        public static final Supplier<FluidType> AMBROSIA = FLUID_TYPES.register("ambrosia", () -> new AgresticFluidType("ambrosia").booze());

        public static void staticInit(IEventBus bus) {
            FLUID_TYPES.register(bus);
        }
    }

    public static class Properties {
        public static final BaseFlowingFluid.Properties OLIVE_OIL = new BaseFlowingFluid.Properties(AgresticFluids.Types.OLIVE_OIL, AgresticFluids.OLIVE_OIL, AgresticFluids.FLOWING_OLIVE_OIL);
        public static final BaseFlowingFluid.Properties IRONBERRY_JUICE = new BaseFlowingFluid.Properties(AgresticFluids.Types.IRONBERRY_JUICE, AgresticFluids.IRONBERRY_JUICE, AgresticFluids.FLOWING_IRONBERRY_JUICE);
        public static final BaseFlowingFluid.Properties SWEET_BERRY_JUICE = new BaseFlowingFluid.Properties(AgresticFluids.Types.SWEET_BERRY_JUICE, AgresticFluids.SWEET_BERRY_JUICE, AgresticFluids.FLOWING_SWEET_BERRY_JUICE);
        public static final BaseFlowingFluid.Properties GRAPE_JUICE = new BaseFlowingFluid.Properties(AgresticFluids.Types.GRAPE_JUICE, AgresticFluids.GRAPE_JUICE, AgresticFluids.FLOWING_GRAPE_JUICE);
        public static final BaseFlowingFluid.Properties APPLE_JUICE = new BaseFlowingFluid.Properties(AgresticFluids.Types.APPLE_JUICE, AgresticFluids.APPLE_JUICE, AgresticFluids.FLOWING_APPLE_JUICE);
        public static final BaseFlowingFluid.Properties ALE_WORT = new BaseFlowingFluid.Properties(AgresticFluids.Types.ALE_WORT, AgresticFluids.ALE_WORT, AgresticFluids.FLOWING_ALE_WORT);
        public static final BaseFlowingFluid.Properties GOLDEN_APPLE_JUICE = new BaseFlowingFluid.Properties(AgresticFluids.Types.GOLDEN_APPLE_JUICE, AgresticFluids.GOLDEN_APPLE_JUICE, AgresticFluids.FLOWING_GOLDEN_APPLE_JUICE);
        public static final BaseFlowingFluid.Properties VANTA_OIL = new BaseFlowingFluid.Properties(AgresticFluids.Types.VANTA_OIL, AgresticFluids.VANTA_OIL, AgresticFluids.FLOWING_VANTA_OIL);
        public static final BaseFlowingFluid.Properties ALE = new BaseFlowingFluid.Properties(AgresticFluids.Types.ALE, AgresticFluids.ALE, AgresticFluids.FLOWING_ALE);
        public static final BaseFlowingFluid.Properties CIDER = new BaseFlowingFluid.Properties(AgresticFluids.Types.CIDER, AgresticFluids.CIDER, AgresticFluids.FLOWING_CIDER);
        public static final BaseFlowingFluid.Properties IRON_WINE = new BaseFlowingFluid.Properties(AgresticFluids.Types.IRON_WINE, AgresticFluids.IRON_WINE, AgresticFluids.FLOWING_IRON_WINE);
        public static final BaseFlowingFluid.Properties MEAD = new BaseFlowingFluid.Properties(AgresticFluids.Types.MEAD, AgresticFluids.MEAD, AgresticFluids.FLOWING_MEAD);
        public static final BaseFlowingFluid.Properties SWEET_BERRY_WINE = new BaseFlowingFluid.Properties(AgresticFluids.Types.SWEET_BERRY_WINE, AgresticFluids.SWEET_BERRY_WINE, AgresticFluids.FLOWING_SWEET_BERRY_WINE);
        public static final BaseFlowingFluid.Properties WINE = new BaseFlowingFluid.Properties(AgresticFluids.Types.WINE, AgresticFluids.WINE, AgresticFluids.FLOWING_WINE);
        public static final BaseFlowingFluid.Properties AMBROSIA = new BaseFlowingFluid.Properties(AgresticFluids.Types.AMBROSIA, AgresticFluids.AMBROSIA, AgresticFluids.FLOWING_AMBROSIA);
    }
}
