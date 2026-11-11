package io.github.pouffy.agrestic.core;

import net.minecraft.client.renderer.Rect2i;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;

public interface IFluidTankScreen {

    @Nullable
    FluidLocation getFluidUnderMouse(int mouseX, int mouseY);

    record FluidLocation(FluidStack fluid, Rect2i location) {}
}
