package io.github.pouffy.agrestic.datagen.server.recipe;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.AgresticRecipeProvider;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.ShapelessNoReturnRecipeBuilder;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
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
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AgresticBlocks.EVAPORATING_BASIN)
                .pattern("B B")
                .pattern("BBB")
                .define('B', Items.BRICK)
                .unlockedBy("has_brick", has(Items.BRICK))
                .save(output, Agrestic.location("crafting/evaporating_basin"));

        ShapelessNoReturnRecipeBuilder.shapeless(RecipeCategory.BREWING, AgresticItems.ALE_WORT_BUCKET)
                .requires(Tags.Items.BUCKETS_WATER)
                .requires(Items.SUGAR)
                .requires(Tags.Items.FOODS_BREAD)
                .unlockedBy("has_bread", has(Tags.Items.FOODS_BREAD))
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .unlockedBy("has_water", has(Tags.Items.BUCKETS_WATER))
                .save(output, Agrestic.location("crafting/ale_wort_bucket"));
    }
}
