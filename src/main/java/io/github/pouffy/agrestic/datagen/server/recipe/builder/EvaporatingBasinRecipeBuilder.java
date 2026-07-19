package io.github.pouffy.agrestic.datagen.server.recipe.builder;

import io.github.pouffy.agrestic.common.recipe.EvaporatingBasinRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;

public class EvaporatingBasinRecipeBuilder implements RecipeBuilder {
    private final SizedFluidIngredient ingredient;
    private final ItemStack output;
    private int time = 100;

    public EvaporatingBasinRecipeBuilder(SizedFluidIngredient ingredient, ItemStack output) {
        this.ingredient = ingredient;
        this.output = output;
    }

    public EvaporatingBasinRecipeBuilder time(int time) {
        this.time = time;
        return this;
    }

    @Override
    public EvaporatingBasinRecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        return this;
    }

    @Override
    public EvaporatingBasinRecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public Item getResult() {
        return output.getItem();
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId) {
        Advancement.Builder advancement$builder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        EvaporatingBasinRecipe recipe = new EvaporatingBasinRecipe(ingredient, output, time);
        pRecipeOutput.accept(pId, recipe, advancement$builder.build(pId.withPrefix("recipes/")));
    }
}
