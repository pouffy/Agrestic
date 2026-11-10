package io.github.pouffy.agrestic.datagen.server;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticFluids;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import slimeknights.mantle.fluid.transfer.AbstractFluidContainerTransferProvider;

import java.util.concurrent.CompletableFuture;

public class FluidTransferProvider extends AbstractFluidContainerTransferProvider {

    public FluidTransferProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, Agrestic.MODID, registries);
    }

    @Override
    protected void addTransfers(HolderLookup.Provider lookup) {
        addFillEmpty("apple_juice_bottle_", AgresticItems.APPLE_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.APPLE_JUICE.get(), AgresticFluids.APPLE_JUICE.getCommonTag(), 250, false);
        addFillEmpty("golden_apple_juice_bottle_", AgresticItems.GOLDEN_APPLE_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.GOLDEN_APPLE_JUICE.get(), AgresticFluids.GOLDEN_APPLE_JUICE.getCommonTag(), 250, false);
        addFillEmpty("grape_juice_bottle_", AgresticItems.GRAPE_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.GRAPE_JUICE.get(), AgresticFluids.GRAPE_JUICE.getCommonTag(), 250, false);
        addFillEmpty("sweet_berry_juice_bottle_", AgresticItems.SWEET_BERRY_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.SWEET_BERRY_JUICE.get(), AgresticFluids.SWEET_BERRY_JUICE.getCommonTag(), 250, false);
        addFillEmpty("ironberry_juice_bottle_", AgresticItems.IRONBERRY_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.IRONBERRY_JUICE.get(), AgresticFluids.IRONBERRY_JUICE.getCommonTag(), 250, false);
        addFillEmpty("ale_wort_bottle_", AgresticItems.ALE_WORT_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.ALE_WORT.get(), AgresticFluids.ALE_WORT.getCommonTag(), 250, false);
        addFillEmpty("olive_oil_bottle_", AgresticItems.OLIVE_OIL_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.OLIVE_OIL.get(), AgresticFluids.OLIVE_OIL.getCommonTag(), 250, false);
        addFillEmpty("vanta_oil_bottle_", AgresticItems.VANTA_OIL_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.VANTA_OIL.get(), AgresticFluids.VANTA_OIL.getCommonTag(), 250, false);

        addFillEmpty("ale_bottle_", AgresticItems.ALE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.ALE.get(), AgresticFluids.ALE.getCommonTag(), 250, true);
        addFillEmpty("cider_bottle_", AgresticItems.CIDER_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.CIDER.get(), AgresticFluids.CIDER.getCommonTag(), 250, true);
        addFillEmpty("iron_wine_bottle_", AgresticItems.IRON_WINE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.IRON_WINE.get(), AgresticFluids.IRON_WINE.getCommonTag(), 250, true);
        addFillEmpty("mead_bottle_", AgresticItems.MEAD_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.MEAD.get(), AgresticFluids.MEAD.getCommonTag(), 250, true);
        addFillEmpty("sweet_berry_wine_bottle_", AgresticItems.SWEET_BERRY_WINE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.SWEET_BERRY_WINE.get(), AgresticFluids.SWEET_BERRY_WINE.getCommonTag(), 250, true);
        addFillEmpty("wine_bottle_", AgresticItems.WINE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.WINE.get(), AgresticFluids.WINE.getCommonTag(), 250, true);
        addFillEmpty("ambrosia_bottle_", AgresticItems.AMBROSIA_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.AMBROSIA.get(), AgresticFluids.AMBROSIA.getCommonTag(), 250, true);
    }

    @Override
    public String getName() {
        return "Agrestic Fluid Transfer Provider";
    }
}
