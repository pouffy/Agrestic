package io.github.pouffy.agrestic.datagen.server.recipe;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.AgresticRecipeProvider;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.EvaporatingBasinRecipeBuilder;
import io.github.pouffy.agrestic.init.AgresticFluids;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.concurrent.CompletableFuture;

public class AgresticEvaporatingProvider extends AgresticRecipeProvider {
    public AgresticEvaporatingProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected String type() {
        return "Basin Evaporating";
    }

    protected void buildRecipes(RecipeOutput output) {
        recipe(AgresticItems.TINY_IRON_DUST.stack(), SizedFluidIngredient.of(AgresticFluids.IRONBERRY_JUICE.get(), 500), output, "ironberry_juice");
        recipe(AgresticItems.GOLD_DUST.stack(), SizedFluidIngredient.of(AgresticFluids.GOLDEN_APPLE_JUICE.get(), 100), output, "golden_apple_juice");
    }

    public void recipe(ItemStack output, SizedFluidIngredient ingredient, RecipeOutput recipeOutput, String path) {
        recipe(output, ingredient, ingredient.amount(), recipeOutput, path);
    }

    public void recipe(ItemStack output, SizedFluidIngredient ingredient, int time, RecipeOutput recipeOutput, String path) {
        var builder = new EvaporatingBasinRecipeBuilder(ingredient, output);
        builder.time(time);
        builder.save(recipeOutput, Agrestic.location("evaporating_basin/").withSuffix(path));
    }
}
