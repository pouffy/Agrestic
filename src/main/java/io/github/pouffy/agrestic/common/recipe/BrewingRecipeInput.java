package io.github.pouffy.agrestic.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public record BrewingRecipeInput(FluidStack stack, FluidStack auxiliary) implements RecipeInput {

    public FluidStack getFluid() {
        return stack;
    }

    public FluidStack getAuxiliary() {
        return auxiliary;
    }

    @Override
    public ItemStack getItem(int i) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
