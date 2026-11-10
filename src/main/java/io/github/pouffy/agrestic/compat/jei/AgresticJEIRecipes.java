package io.github.pouffy.agrestic.compat.jei;

import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
import io.github.pouffy.agrestic.common.recipe.CrushingTubRecipe;
import io.github.pouffy.agrestic.common.recipe.EvaporatingBasinRecipe;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.util.ErrorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.*;

import java.util.List;

public class AgresticJEIRecipes {
    private final RecipeManager recipeManager;
    private final IIngredientManager ingredientManager;

    public AgresticJEIRecipes(IIngredientManager ingredientManager) {
        Minecraft minecraft = Minecraft.getInstance();
        ErrorUtil.checkNotNull(minecraft, "minecraft");
        ClientLevel world = minecraft.level;
        ErrorUtil.checkNotNull(world, "minecraft world");
        this.recipeManager = world.getRecipeManager();
        this.ingredientManager = ingredientManager;
    }

    public List<RecipeHolder<BrewingBarrelRecipe>> getBrewingRecipes() {
        return getValidHandledRecipes(recipeManager, AgresticRecipeTypes.BREWING_BARREL.get());
    }

    public List<RecipeHolder<CrushingTubRecipe>> getCrushingRecipes() {
        return getValidHandledRecipes(recipeManager, AgresticRecipeTypes.CRUSHING_TUB.get());
    }

    public List<RecipeHolder<EvaporatingBasinRecipe>> getEvaporatingRecipes() {
        return getValidHandledRecipes(recipeManager, AgresticRecipeTypes.EVAPORATING_BASIN.get());
    }

    private static <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> getValidHandledRecipes(
            RecipeManager recipeManager,
            RecipeType<T> recipeType
    ) {
        return recipeManager.getAllRecipesFor(recipeType)
                .stream()
                .toList();
    }
}
