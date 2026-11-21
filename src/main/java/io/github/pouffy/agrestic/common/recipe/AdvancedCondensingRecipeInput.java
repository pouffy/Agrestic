package io.github.pouffy.agrestic.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record AdvancedCondensingRecipeInput(FluidStack base, List<ItemStack> ingredients, ItemStack container, ItemStack modifier) implements RecipeInput {

    public FluidStack getFluid() {
        return base;
    }

    @Override
    public @NonNull ItemStack getItem(int index) {
        return ingredients.get(index);
    }

    @Override
    public int size() {
        return ingredients.size();
    }
}
