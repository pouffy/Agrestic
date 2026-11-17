package io.github.pouffy.agrestic.datagen.server.recipe;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.item.ElixirItem;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.AgresticRecipeProvider;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.CondensingRecipeBuilder;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class AgresticCondensingProvider extends AgresticRecipeProvider {
    public AgresticCondensingProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected String type() {
        return "Condensing";
    }

    protected void buildRecipes(RecipeOutput output) {
        condense(ElixirItem.withEffect(MobEffects.HEAL, 0, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.CHAMOMILE), Ingredient.of(Items.BEEF), 400, output, "instant_health");
    }

    public void condense(ItemStack result, SizedFluidIngredient base, Ingredient first, @Nullable Ingredient second, int time, RecipeOutput recipeOutput, String path) {
        var builder = new CondensingRecipeBuilder(base, result).time(time).require(first);
        if (second != null)
            builder.require(second);
        builder.save(recipeOutput, Agrestic.location("basic_condensing/").withSuffix(path));
    }
}
