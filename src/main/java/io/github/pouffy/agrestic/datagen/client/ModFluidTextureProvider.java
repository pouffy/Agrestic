package io.github.pouffy.agrestic.datagen.client;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticFluids;
import net.minecraft.data.PackOutput;
import slimeknights.mantle.fluid.texture.AbstractFluidTextureProvider;
import slimeknights.mantle.fluid.texture.FluidTexture;
import slimeknights.mantle.registration.object.FluidObject;

public class ModFluidTextureProvider extends AbstractFluidTextureProvider {

    public ModFluidTextureProvider(PackOutput packOutput) {
        super(packOutput, Agrestic.MODID);
    }

    /** Wraps the texture with the fluid folder and a suffix of underscore for the two textures. */
    private FluidTexture.Builder root(FluidObject<?> fluid) {
        return texture(fluid).wrapId("fluid/", "_").still().flowing();
    }

    private FluidTexture.Builder booze(FluidObject<?> fluid) {
        return texture(fluid).wrapId("fluid/booze/", "_").still().flowing();
    }

    @Override
    public void addTextures() {
        root(AgresticFluids.OLIVE_OIL);
        root(AgresticFluids.IRONBERRY_JUICE);
        root(AgresticFluids.SWEET_BERRY_JUICE);
        root(AgresticFluids.GRAPE_JUICE);
        root(AgresticFluids.APPLE_JUICE);
        root(AgresticFluids.ALE_WORT);
        root(AgresticFluids.GOLDEN_APPLE_JUICE);
        root(AgresticFluids.VANTA_OIL);
        booze(AgresticFluids.ALE);
        booze(AgresticFluids.CIDER);
        booze(AgresticFluids.IRON_WINE);
        booze(AgresticFluids.MEAD);
        booze(AgresticFluids.SWEET_BERRY_WINE);
        booze(AgresticFluids.WINE);
        booze(AgresticFluids.AMBROSIA);
    }

    @Override
    public String getName() {
        return "Agrestic Fluid Textures";
    }
}
