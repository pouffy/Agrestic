package io.github.pouffy.agrestic.datagen.server.tags;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends BiomeTagsProvider {

    public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Agrestic.MODID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.tag(AgresticTags.FeatureAddition.HAS_IRONWOOD_TREES).add(Biomes.PLAINS, Biomes.MEADOW, Biomes.FOREST, Biomes.SWAMP, Biomes.WINDSWEPT_HILLS, Biomes.JUNGLE);
        this.tag(AgresticTags.FeatureAddition.HAS_OLIVE_TREES).add(Biomes.PLAINS, Biomes.FOREST, Biomes.WINDSWEPT_HILLS);
    }
}
