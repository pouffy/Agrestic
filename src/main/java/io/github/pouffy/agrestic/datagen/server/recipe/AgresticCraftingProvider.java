package io.github.pouffy.agrestic.datagen.server.recipe;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.AgresticRecipeProvider;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class AgresticCraftingProvider extends AgresticRecipeProvider {
    public AgresticCraftingProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected String type() {
        return "Crafting";
    }

    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AgresticBlocks.CRUSHING_TUB)
                .pattern("P P")
                .pattern("I I")
                .pattern("SSS")
                .define('P', ItemTags.PLANKS)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(output, Agrestic.location("crafting/crushing_tub"));
    }
}
