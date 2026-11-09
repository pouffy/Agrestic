package io.github.pouffy.agrestic.datagen.server.tags;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticFluids;
import io.github.pouffy.agrestic.init.AgresticTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.mantle.registration.object.FluidObject;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ModFluidTagsProvider extends FluidTagsProvider {

    public ModFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Agrestic.MODID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        addTags(AgresticFluids.APPLE_JUICE);
        addTags(AgresticFluids.GOLDEN_APPLE_JUICE);
        addTags(AgresticFluids.GRAPE_JUICE);
        addTags(AgresticFluids.SWEET_BERRY_JUICE);
        addTags(AgresticFluids.IRONBERRY_JUICE);
        addTags(AgresticFluids.ALE_WORT);
        addTags(AgresticFluids.OLIVE_OIL);
        addTags(AgresticFluids.VANTA_OIL);

        addTags(AgresticFluids.ALE);
        addTags(AgresticFluids.CIDER);
        addTags(AgresticFluids.IRON_WINE);
        addTags(AgresticFluids.MEAD);
        addTags(AgresticFluids.SWEET_BERRY_WINE);
        addTags(AgresticFluids.WINE);
        addTags(AgresticFluids.AMBROSIA);

    }

    protected final void addToTag(TagKey<Fluid> tagKey, Fluid... fluids) {
        var tag = this.tag(tagKey);
        List.of(fluids).forEach(tag::add);
    }

    /** Tags a fluid object in its common tag */
    private void addTags(FluidObject<?> fluid) {
        tag(Objects.requireNonNull(fluid.getCommonTag())).addOptional(fluid.id());
    }

    /** Tags a fluid object in its common tag */
    private void addTags(FlowingFluidObject<?> fluid) {
        tag(Objects.requireNonNull(fluid.getCommonTag())).addOptionalTag(fluid.getLocalTag());
        tag(fluid.getLocalTag()).addOptional(fluid.id()).addOptional(fluid.id().withPrefix("flowing_"));
    }
}
