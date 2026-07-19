package io.github.pouffy.agrestic.datagen.server.recipe;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.AgresticRecipeProvider;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.ShapelessNoReturnRecipeBuilder;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticFluids;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
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
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AgresticBlocks.FLUID_BARREL)
                .pattern("P P")
                .pattern("I I")
                .pattern("PSP")
                .define('P', ItemTags.PLANKS)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(output, Agrestic.location("crafting/fluid_barrel"));

        ShapelessNoReturnRecipeBuilder.shapeless(RecipeCategory.BREWING, AgresticFluids.ALE_WORT_BUCKET)
                .requires(Tags.Items.BUCKETS_WATER)
                .requires(Items.SUGAR)
                .requires(Tags.Items.FOODS_BREAD)
                .unlockedBy("has_bread", has(Tags.Items.FOODS_BREAD))
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .unlockedBy("has_water", has(Tags.Items.BUCKETS_WATER))
                .save(output, Agrestic.location("crafting/ale_wort_bucket"));

        compressedBlock(AgresticItems.IRON_DUST.asItem(), AgresticItems.TINY_IRON_DUST, false).save(output, Agrestic.location("crafting/iron_dust_from_tiny_iron_dust"));
        decompressedBlock(AgresticItems.TINY_IRON_DUST.asItem(), AgresticItems.IRON_DUST, false).save(output, Agrestic.location("crafting/tiny_iron_dust_from_iron_dust"));
        compressedBlock(AgresticItems.GOLD_DUST.asItem(), AgresticItems.TINY_GOLD_DUST, false).save(output, Agrestic.location("crafting/gold_dust_from_tiny_gold_dust"));
        decompressedBlock(AgresticItems.TINY_GOLD_DUST.asItem(), AgresticItems.GOLD_DUST, false).save(output, Agrestic.location("crafting/tiny_gold_dust_from_gold_dust"));

        for(AgresticBlocks.Woodset woodset : AgresticBlocks.WOODSETS) {
            String woodName = getItemName(woodset.planks()).replace("_planks", "");
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, woodset.planks(), 4).requires(woodset.logTag().itemTag()).group("planks").unlockedBy("has_logs", has(woodset.logTag().itemTag())).save(output, Agrestic.location("crafting/" + woodName + "_planks"));
            simpleStairs(woodset.stairs().asItem(), woodset.planks()).save(output, Agrestic.location("crafting/" + woodName + "_stairs"));
            simpleSlab(woodset.slab().asItem(), woodset.planks()).save(output, Agrestic.location("crafting/" + woodName + "_slab"));
            simpleFence(woodset.fence().asItem(), woodset.planks()).save(output, Agrestic.location("crafting/" + woodName + "_fence"));
            simpleFenceGate(woodset.fenceGate().asItem(), woodset.planks()).save(output, Agrestic.location("crafting/" + woodName + "_fence_gate"));
            simpleDoor(woodset.door().asItem(), woodset.planks()).save(output, Agrestic.location("crafting/" + woodName + "_door"));
            simpleTrapdoor(woodset.trapdoor().asItem(), woodset.planks()).save(output, Agrestic.location("crafting/" + woodName + "_trapdoor"));
            simpleSign(woodset.sign().asItem(), woodset.planks()).save(output, Agrestic.location("crafting/" + woodName + "_sign"));
            simpleHangingSign(woodset.hangingSign().asItem(), woodset.planks()).save(output, Agrestic.location("crafting/" + woodName + "_hanging_sign"));
        }
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, AgresticItems.OLIVE_BOAT)
                .pattern("P P")
                .pattern("PPP")
                .define('P', AgresticBlocks.OLIVE.planks())
                .group("boat")
                .unlockedBy("in_water", insideOf(Blocks.WATER))
                .save(output, Agrestic.location("crafting/olive_boat"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, AgresticItems.OLIVE_CHEST_BOAT).requires(Blocks.CHEST).requires(AgresticItems.OLIVE_BOAT).group("chest_boat").unlockedBy("has_boat", has(ItemTags.BOATS)).save(output, Agrestic.location("crafting/olive_chest_boat"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, AgresticItems.IRONWOOD_BOAT)
                .pattern("P P")
                .pattern("PPP")
                .define('P', AgresticBlocks.IRONWOOD.planks())
                .group("boat")
                .unlockedBy("in_water", insideOf(Blocks.WATER))
                .save(output, Agrestic.location("crafting/ironwood_boat"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, AgresticItems.IRONWOOD_CHEST_BOAT).requires(Blocks.CHEST).requires(AgresticItems.IRONWOOD_BOAT).group("chest_boat").unlockedBy("has_boat", has(ItemTags.BOATS)).save(output, Agrestic.location("crafting/ironwood_chest_boat"));
    }

    public static ShapelessRecipeBuilder decompressedBlock(Item result, ItemLike input, boolean fourByFour) {
        if (fourByFour) {
            return halfDecompress(result, input);
        } else {
            return fullDecompress(result, input);
        }
    }
    public static ShapedRecipeBuilder compressedBlock(Item result, ItemLike input, boolean fourByFour) {
        if (fourByFour) {
            return halfCompress(result, input,1);
        } else {
            return fullCompress(result, input,1);
        }
    }
    public static ShapedRecipeBuilder compressedBlock(Item result, ItemLike input, boolean fourByFour, int count) {
        if (fourByFour) { return halfCompress(result, input, count);
        } else {
            return fullCompress(result, input, count);
        }
    }
    public static ShapelessRecipeBuilder fullDecompress(Item result, ItemLike input) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 9).requires(input).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
    public static ShapelessRecipeBuilder halfDecompress(Item result, ItemLike input) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 4).requires(input).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
    public static ShapelessRecipeBuilder simpleShapeless(Item result, ItemLike input, int count) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, count).requires(input).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
    public static ShapedRecipeBuilder fullCompress(Item result, ItemLike input, int count) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, count).pattern("###").pattern("###").pattern("###").define('#', input).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
    public static ShapedRecipeBuilder halfCompress(Item result, ItemLike input, int count) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, count).pattern("##").pattern("##").define('#', input).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }

    public static ShapedRecipeBuilder simpleStairs(Item result, ItemLike input) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 4).pattern("#  ").pattern("## ").pattern("###").define('#', input).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
    public static ShapedRecipeBuilder simpleSlab(Item result, ItemLike input) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 6).pattern("###").define('#', input).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
    public static ShapedRecipeBuilder simpleWall(Item result, ItemLike input) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 6).pattern("###").pattern("###").define('#', input).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
    public static ShapedRecipeBuilder simpleFence(Item result, ItemLike input) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 3).pattern("#S#").pattern("#S#").define('#', input).define('S', Items.STICK).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
    public static ShapedRecipeBuilder simpleFenceGate(Item result, ItemLike input) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 1).pattern("S#S").pattern("S#S").define('#', input).define('S', Items.STICK).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
    public static ShapedRecipeBuilder simpleDoor(Item result, ItemLike input) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 3).pattern("##").pattern("##").pattern("##").define('#', input).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
    public static ShapedRecipeBuilder simpleTrapdoor(Item result, ItemLike input) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 2).pattern("###").pattern("###").define('#', input).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
    public static ShapedRecipeBuilder simpleSign(Item result, ItemLike input) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 3).pattern("###").pattern("###").pattern(" S ").define('#', input).define('S', Items.STICK).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
    public static ShapedRecipeBuilder simpleHangingSign(Item result, ItemLike input) {
        String inputName = input.asItem().toString().split(":")[1];
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 6).pattern("C C").pattern("###").pattern("###").define('#', input).define('C', Items.CHAIN).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }
}
