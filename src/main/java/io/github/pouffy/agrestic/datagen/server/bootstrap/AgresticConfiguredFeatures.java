package io.github.pouffy.agrestic.datagen.server.bootstrap;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.PineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class AgresticConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> IRONWOOD_TREE = makeKey("ironwood_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OLIVE_TREE = makeKey("olive_tree");

    private static ResourceKey<ConfiguredFeature<?, ?>> makeKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Agrestic.location(name));
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // Iron Wood Tree
        register(context, IRONWOOD_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(AgresticBlocks.IRONWOOD.log().block()),
                new StraightTrunkPlacer(10, 2, 0),
                BlockStateProvider.simple(AgresticBlocks.IRONWOOD.leaves().block()),
                new PineFoliagePlacer(ConstantInt.of(3),ConstantInt.of(0), ConstantInt.of(3)),
                new TwoLayersFeatureSize(3, 1, 2)
        ).build());

        // Olive Tree
        register(context, OLIVE_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(AgresticBlocks.OLIVE.log().block()),
                new DarkOakTrunkPlacer(4, 2, 2),
                BlockStateProvider.simple(AgresticBlocks.OLIVE.leaves().block()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(3),3),
                new TwoLayersFeatureSize(3, 0, 2)
        ).build());

    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }


}
