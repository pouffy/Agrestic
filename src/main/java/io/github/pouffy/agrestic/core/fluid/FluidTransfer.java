package io.github.pouffy.agrestic.core.fluid;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public record FluidTransfer(ItemStack stack, FluidStack fluid, boolean didFill) {

}
