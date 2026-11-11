package io.github.pouffy.agrestic.datagen.client;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticFluids;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.fluids.FluidType;
import slimeknights.mantle.datagen.MantleValues;
import slimeknights.mantle.fluid.tooltip.AbstractFluidTooltipProvider;
import slimeknights.mantle.registration.object.FluidObject;

public class ModFluidTooltipProvider extends AbstractFluidTooltipProvider {
    public ModFluidTooltipProvider(PackOutput output) {
        super(output, Agrestic.MODID);
    }

    @Override
    protected void addFluids() {
        drink(AgresticFluids.IRONBERRY_JUICE);
        drink(AgresticFluids.VANTA_OIL);
        drink(AgresticFluids.ALE_WORT);
        drink(AgresticFluids.OLIVE_OIL);
        drink(AgresticFluids.GOLDEN_APPLE_JUICE);
        drink(AgresticFluids.APPLE_JUICE);
        drink(AgresticFluids.SWEET_BERRY_JUICE);
        drink(AgresticFluids.GRAPE_JUICE);

        booze(AgresticFluids.IRON_WINE);
        booze(AgresticFluids.ALE);
        booze(AgresticFluids.CIDER);
        booze(AgresticFluids.MEAD);
        booze(AgresticFluids.AMBROSIA);
        booze(AgresticFluids.SWEET_BERRY_WINE);
        booze(AgresticFluids.WINE);
    }

    private void drink(FluidObject<?> fluid) {
        add(fluid.id().getPath(), fluid.getCommonTag())
                .addUnit("bucket", FluidType.BUCKET_VOLUME)
                .addUnit("bottle", MantleValues.BOTTLE)
                .addUnit("drop", MantleValues.DROP);
    }

    private void booze(FluidObject<?> fluid) {
        add(fluid.id().getPath(), fluid.getCommonTag())
                .addUnit("bottle", MantleValues.BOTTLE)
                .addUnit("drop", MantleValues.DROP);
    }

    @Override
    public String getName() {
        return "Agrestic Fluid Tooltips";
    }
}
