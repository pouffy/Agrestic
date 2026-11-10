package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.Agrestic;
import net.neoforged.bus.api.IEventBus;
import slimeknights.mantle.fluid.UnplaceableFluid;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FluidObject;

import java.util.ArrayList;

public class AgresticFluids {

    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(Agrestic.MODID);

    public static final ArrayList<FluidObject<?>> REGISTERED_FLUIDS = new ArrayList<>();

    public static final FluidObject<UnplaceableFluid> OLIVE_OIL = create("olive_oil");
    public static final FluidObject<UnplaceableFluid> IRONBERRY_JUICE = create("ironberry_juice");
    public static final FluidObject<UnplaceableFluid> SWEET_BERRY_JUICE = create("sweet_berry_juice");
    public static final FluidObject<UnplaceableFluid> GRAPE_JUICE = create("grape_juice");
    public static final FluidObject<UnplaceableFluid> APPLE_JUICE = create("apple_juice");
    public static final FluidObject<UnplaceableFluid> ALE_WORT = create("ale_wort");
    public static final FluidObject<UnplaceableFluid> GOLDEN_APPLE_JUICE = create("golden_apple_juice");
    public static final FluidObject<UnplaceableFluid> VANTA_OIL = create("vanta_oil");
    public static final FluidObject<UnplaceableFluid> ALE = createBooze("ale");
    public static final FluidObject<UnplaceableFluid> CIDER = createBooze("cider");
    public static final FluidObject<UnplaceableFluid> IRON_WINE = createBooze("iron_wine");
    public static final FluidObject<UnplaceableFluid> MEAD = createBooze("mead");
    public static final FluidObject<UnplaceableFluid> SWEET_BERRY_WINE = createBooze("sweet_berry_wine");
    public static final FluidObject<UnplaceableFluid> WINE = createBooze("wine");
    public static final FluidObject<UnplaceableFluid> AMBROSIA = createBooze("ambrosia");

    public static void init(IEventBus bus) {
        FLUIDS.register(bus);
        Agrestic.LOGGER.debug("Registered Fluids");
    }
    
    public static FluidObject<UnplaceableFluid> create(String name) {
        var toRet = FLUIDS.register(name).commonTag().bucket().unplacable();
        REGISTERED_FLUIDS.add(toRet);
        return toRet;
    }

    public static FluidObject<UnplaceableFluid> createBooze(String name) {
        var toRet = FLUIDS.register(name).commonTag().unplacable();
        REGISTERED_FLUIDS.add(toRet);
        return toRet;
    }
}
