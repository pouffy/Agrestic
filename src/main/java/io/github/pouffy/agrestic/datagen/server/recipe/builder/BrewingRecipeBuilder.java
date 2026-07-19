package io.github.pouffy.agrestic.datagen.server.recipe.builder;

import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class BrewingRecipeBuilder implements RecipeBuilder {
    private final FluidStack input;
    private final FluidStack output;
    private int time = 12000;

    public BrewingRecipeBuilder(FluidStack input, FluidStack output) {
        this.input = input;
        this.output = output;
    }

    public BrewingRecipeBuilder time(int time) {
        this.time = time;
        return this;
    }

    @Override
    public BrewingRecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        return this;
    }

    @Override
    public BrewingRecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public Item getResult() {
        return Items.AIR;
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId) {
        Advancement.Builder advancement$builder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        BrewingBarrelRecipe recipe = new BrewingBarrelRecipe(input, output, time);
        pRecipeOutput.accept(pId, recipe, advancement$builder.build(pId.withPrefix("recipes/")));
    }
}
