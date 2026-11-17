package io.github.pouffy.agrestic.datagen.server.recipe.builder;

import io.github.pouffy.agrestic.common.recipe.BasicCondensingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;

public class CondensingRecipeBuilder implements RecipeBuilder {
    private final NonNullList<Ingredient> ingredients;
    private final SizedFluidIngredient base;
    private final ItemStack output;
    private int time = 400;

    public CondensingRecipeBuilder(SizedFluidIngredient base, ItemStack output) {
        this.ingredients = NonNullList.create();
        this.base = base;
        this.output = output;
    }

    public CondensingRecipeBuilder time(int time) {
        this.time = time;
        return this;
    }

    public CondensingRecipeBuilder require(Ingredient ingredient) {
        if ((this.ingredients.size() + 1) > 2) throw new IllegalArgumentException("Too many ingredients for condensing recipe");
        this.ingredients.add(ingredient);
        return this;
    }

    @Override
    public CondensingRecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        return this;
    }

    @Override
    public CondensingRecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public Item getResult() {
        return output.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation pId) {
        Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        BasicCondensingRecipe recipe = new BasicCondensingRecipe(base, ingredients, output, time);
        recipeOutput.accept(pId, recipe, advancement$builder.build(pId.withPrefix("recipes/")));
    }
}
