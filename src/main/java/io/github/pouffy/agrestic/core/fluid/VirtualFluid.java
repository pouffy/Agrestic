package io.github.pouffy.agrestic.core.fluid;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class VirtualFluid extends BaseFlowingFluid {
    private final boolean source;

    public VirtualFluid(BaseFlowingFluid.Properties properties, boolean source) {
        super(properties);
        this.source = source;
    }

    public Fluid getSource() {
        return source ? this : super.getSource();
    }

    public Fluid getFlowing() {
        return source ? super.getFlowing() : this;
    }

    public Item getBucket() {
        return Items.AIR;
    }

    protected BlockState createLegacyBlock(FluidState fluidState) {
        return Blocks.AIR.defaultBlockState();
    }

    public boolean isSource(FluidState fluidState) {
        return source;
    }

    public int getAmount(FluidState fluidState) {
        return 0;
    }
}
