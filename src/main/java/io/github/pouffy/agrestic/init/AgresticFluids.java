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
import slimeknights.mantle.fluid.UnplaceableFluid;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FluidObject;

import java.util.function.Supplier;

import static io.github.pouffy.agrestic.init.AgresticItems.registerBucket;

@EventBusSubscriber
public class AgresticFluids {

    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(Agrestic.MODID);

    public static final FluidObject<UnplaceableFluid> OLIVE_OIL = FLUIDS.register("olive_oil").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> IRONBERRY_JUICE = FLUIDS.register("ironberry_juice").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> SWEET_BERRY_JUICE = FLUIDS.register("sweet_berry_juice").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> GRAPE_JUICE = FLUIDS.register("grape_juice").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> APPLE_JUICE = FLUIDS.register("apple_juice").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> ALE_WORT = FLUIDS.register("ale_wort").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> GOLDEN_APPLE_JUICE = FLUIDS.register("golden_apple_juice").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> VANTA_OIL = FLUIDS.register("vanta_oil").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> ALE = FLUIDS.register("ale").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> CIDER = FLUIDS.register("cider").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> IRON_WINE = FLUIDS.register("iron_wine").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> MEAD = FLUIDS.register("mead").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> SWEET_BERRY_WINE = FLUIDS.register("sweet_berry_wine").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> WINE = FLUIDS.register("wine").commonTag().bucket().unplacable();
    public static final FluidObject<UnplaceableFluid> AMBROSIA = FLUIDS.register("ambrosia").commonTag().bucket().unplacable();

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
}
