package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.datagen.server.bootstrap.AgresticConfiguredFeatures;
import io.github.pouffy.agrestic.datagen.server.bootstrap.AgresticPlacedFeatures;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class AgresticTreeGrowers {

    public static final TreeGrower OLIVE = new TreeGrower(Agrestic.MODID + ":olive", Optional.empty(), Optional.of(AgresticConfiguredFeatures.OLIVE_TREE), Optional.empty());
    public static final TreeGrower IRONWOOD = new TreeGrower(Agrestic.MODID + ":ironwood", Optional.empty(), Optional.of(AgresticConfiguredFeatures.IRONWOOD_TREE), Optional.empty());
    public static final TreeGrower APPLE = new TreeGrower(Agrestic.MODID + ":apple", Optional.empty(), Optional.of(AgresticConfiguredFeatures.IRONWOOD_TREE), Optional.empty());
}
