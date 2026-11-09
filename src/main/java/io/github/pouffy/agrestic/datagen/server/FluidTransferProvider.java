package io.github.pouffy.agrestic.datagen.server;

import com.pouffydev.krystal_core.KrystalCore;
import com.pouffydev.krystal_core.core.KCTags;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import io.github.pouffy.agrestic.init.AgresticFluids;
import io.github.pouffy.agrestic.init.AgresticItems;
import io.github.pouffy.agrestic.init.AgresticTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import slimeknights.mantle.fluid.transfer.AbstractFluidContainerTransferProvider;
import slimeknights.mantle.recipe.helper.FluidOutput;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FluidTransferProvider extends AbstractFluidContainerTransferProvider {

    public FluidTransferProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, Agrestic.MODID, registries);
    }

    @Override
    protected void addTransfers(HolderLookup.Provider lookup) {
        addFillEmpty("honey_bottle_", Items.HONEY_BOTTLE, Items.GLASS_BOTTLE, FluidOutput.fromFluid(KrystalCore.HONEY.get(), 250), SizedFluidIngredient.of(KCTags.Fluids.HONEY.tag(), 250), List.of());

        addFillEmpty("apple_juice_bottle_", AgresticItems.APPLE_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.APPLE_JUICE.result(250), SizedFluidIngredient.of(AgresticFluids.APPLE_JUICE.getCommonTag(), 250), List.of());
        addFillEmpty("golden_apple_juice_bottle_", AgresticItems.GOLDEN_APPLE_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.GOLDEN_APPLE_JUICE.result(250), SizedFluidIngredient.of(AgresticFluids.GOLDEN_APPLE_JUICE.getCommonTag(), 250), List.of());
        addFillEmpty("grape_juice_bottle_", AgresticItems.GRAPE_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.GRAPE_JUICE.result(250),SizedFluidIngredient.of( AgresticFluids.GRAPE_JUICE.getCommonTag(), 250), List.of());
        addFillEmpty("sweet_berry_juice_bottle_", AgresticItems.SWEET_BERRY_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.SWEET_BERRY_JUICE.result(250), SizedFluidIngredient.of(AgresticFluids.SWEET_BERRY_JUICE.getCommonTag(), 250), List.of());
        addFillEmpty("ironberry_juice_bottle_", AgresticItems.IRONBERRY_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.IRONBERRY_JUICE.result(250), SizedFluidIngredient.of(AgresticFluids.IRONBERRY_JUICE.getCommonTag(), 250), List.of());
        addFillEmpty("ale_wort_bottle_", AgresticItems.ALE_WORT_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.ALE_WORT.result(250), SizedFluidIngredient.of(AgresticFluids.ALE_WORT.getCommonTag(), 250), List.of());
        addFillEmpty("olive_oil_bottle_", AgresticItems.OLIVE_OIL_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.OLIVE_OIL.result(250), SizedFluidIngredient.of(AgresticFluids.OLIVE_OIL.getCommonTag(), 250), List.of());
        addFillEmpty("vanta_oil_bottle_", AgresticItems.VANTA_OIL_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.VANTA_OIL.result(250), SizedFluidIngredient.of(AgresticFluids.VANTA_OIL.getCommonTag(), 250), List.of());

        addFillEmpty("ale_bottle_", AgresticItems.ALE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.ALE.result(250), SizedFluidIngredient.of(AgresticFluids.ALE.getCommonTag(), 250), List.of(AgresticDataComponents.QUALITY.get()));
        addFillEmpty("cider_bottle_", AgresticItems.CIDER_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.CIDER.result(250), SizedFluidIngredient.of(AgresticFluids.CIDER.getCommonTag(), 250), List.of(AgresticDataComponents.QUALITY.get()));
        addFillEmpty("iron_wine_bottle_", AgresticItems.IRON_WINE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.IRON_WINE.result(250), SizedFluidIngredient.of(AgresticFluids.IRON_WINE.getCommonTag(), 250), List.of(AgresticDataComponents.QUALITY.get()));
        addFillEmpty("mead_bottle_", AgresticItems.MEAD_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.MEAD.result(250), SizedFluidIngredient.of(AgresticFluids.MEAD.getCommonTag(), 250), List.of(AgresticDataComponents.QUALITY.get()));
        addFillEmpty("sweet_berry_wine_bottle_", AgresticItems.SWEET_BERRY_WINE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.SWEET_BERRY_WINE.result(250), SizedFluidIngredient.of(AgresticFluids.SWEET_BERRY_WINE.getCommonTag(), 250), List.of(AgresticDataComponents.QUALITY.get()));
        addFillEmpty("wine_bottle_", AgresticItems.WINE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.WINE.result(250), SizedFluidIngredient.of(AgresticFluids.WINE.getCommonTag(), 250), List.of(AgresticDataComponents.QUALITY.get()));
        addFillEmpty("ambrosia_bottle_", AgresticItems.AMBROSIA_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.AMBROSIA.result(250), SizedFluidIngredient.of(AgresticFluids.AMBROSIA.getCommonTag(), 250), List.of(AgresticDataComponents.QUALITY.get()));
    }

    @Override
    public String getName() {
        return "Agrestic Fluid Transfer Provider";
    }
}
