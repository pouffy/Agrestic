package io.github.pouffy.agrestic.core.worldgen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record FeatureWithStep(ResourceKey<PlacedFeature> key, GenerationStep.Decoration step) {
    @SafeVarargs
    public static List<FeatureWithStep> vegetationFeatures(ResourceKey<PlacedFeature>... placedFeatures) {
        ArrayList<FeatureWithStep> features = new ArrayList<>();
        Arrays.stream(placedFeatures).toList().forEach(key -> {
            features.add(new FeatureWithStep(key, GenerationStep.Decoration.VEGETAL_DECORATION));
        });

        return features;
    }
}
