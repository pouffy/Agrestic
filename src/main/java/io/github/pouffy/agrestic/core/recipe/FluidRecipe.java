package io.github.pouffy.agrestic.core.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;

public abstract class FluidRecipe<T extends RecipeInput> implements Recipe<T> {
    private final RecipeSerializer<?> recipeSerializer;
    private final RecipeType<?> recipeType;
    public final FluidStack output;

    public FluidRecipe(RecipeSerializer<?> recipeSerializer, RecipeType<?> recipeType, FluidStack output) {
        this.recipeSerializer = recipeSerializer;
        this.recipeType = recipeType;
        this.output = output;
    }

    public FluidStack finish(T input, HolderLookup.Provider registries) {
        return getResultFluid(registries).copy();
    }

    public FluidStack getResultFluid(HolderLookup.Provider registries) {
        return output;
    }

    @Override
    public ItemStack assemble(T input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return recipeSerializer;
    }

    @Override
    public RecipeType<?> getType() {
        return recipeType;
    }
}
