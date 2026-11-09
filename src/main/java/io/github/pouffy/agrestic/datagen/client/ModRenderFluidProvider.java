package io.github.pouffy.agrestic.datagen.client;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import slimeknights.mantle.client.render.FluidCuboid;
import slimeknights.mantle.data.datamap.BlockStateDataMapProvider;

import java.util.List;

public class ModRenderFluidProvider extends BlockStateDataMapProvider<List<FluidCuboid>> {

    public ModRenderFluidProvider(PackOutput output) {
        super(output, PackOutput.Target.RESOURCE_PACK, FluidCuboid.REGISTRY, Agrestic.MODID);
    }

    @Override
    protected void addEntries() {
        Direction[] horizontal = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        block(AgresticBlocks.EVAPORATING_BASIN).variant(List.of(
                FluidCuboid.builder()
                        .from(2, 2, 2)
                        .to(14, 6, 14)
                        .face(false, 0, Direction.UP, horizontal).build()
        ));
        block(AgresticBlocks.CRUSHING_TUB).variant(List.of(
                FluidCuboid.builder()
                        .from(2, 2, 2)
                        .to(14, 8, 14)
                        .face(false, 0, Direction.UP, horizontal).build()
        ));
        block(AgresticBlocks.FLUID_BARREL).variant(List.of(
                FluidCuboid.builder()
                        .from(4, 2, 4)
                        .to(12, 14, 12)
                        .face(false, 0, Direction.UP, horizontal).build()
        ));
    }

    @Override
    public String getName() {
        return "Agrestic render fluid provider";
    }
}
