package io.github.pouffy.agrestic.datagen.server.recipe;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.AgresticRecipeProvider;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class AgresticFurnaceProvider extends AgresticRecipeProvider {
    public AgresticFurnaceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private static ResourceLocation location(String path, int type) {
        String typeStr = switch (type) {
            case 1 -> "blasting";
            case 2 -> "smoking";
            default -> "smelting";
        };
        return Agrestic.location(typeStr + "/" + path);
    }

    @Override
    protected String type() {
        return "Furnace";
    }

    protected void buildRecipes(RecipeOutput output) {
        smeltAndBlast(RecipeCategory.MISC, Items.IRON_NUGGET, AgresticItems.TINY_IRON_DUST, 0.15f, 200, output, "iron_nugget_from_dust");
        smeltAndBlast(RecipeCategory.MISC, Items.IRON_INGOT, AgresticItems.IRON_DUST, 0.3f, 200, output, "iron_ingot_from_dust");
        smeltAndBlast(RecipeCategory.MISC, Items.GOLD_NUGGET, AgresticItems.TINY_GOLD_DUST, 0.25f, 200, output, "gold_nugget_from_dust");
        smeltAndBlast(RecipeCategory.MISC, Items.GOLD_INGOT, AgresticItems.GOLD_DUST, 0.5f, 200, output, "gold_ingot_from_dust");
    }

    public static void smeltAndBlast(RecipeCategory category, Item result, ItemLike input, float xp, int time, RecipeOutput consumer, String recipeId) {
        int blastTime = time / 2;
        smeltingRecipe(category, result, input, xp, time).save(consumer, location(recipeId, 0));
        blastingRecipe(category, result, input, xp, blastTime).save(consumer, location(recipeId, 1));
    }

    public static void smeltAndSmoke(RecipeCategory category, Item result, ItemLike input, float xp, int time, RecipeOutput consumer) {
        int smokeTime = time / 2;
        String inputName = input.asItem().toString().split(":")[1];
        String resultName = result.toString().split(":")[1];
        smeltingRecipe(category, result, input, xp, time).save(consumer, location(resultName + "_from_" + inputName, 0));
        smokingRecipe(category, result, input, xp, smokeTime).save(consumer, location(resultName + "_from_" + inputName, 2));
    }

    public static void smeltAndBlast(RecipeCategory category, Item result, float xp, int time, RecipeOutput consumer, ItemLike... ingredients) {
        int blastTime = time / 2;
        String resultName = result.toString().split(":")[1];
        smeltingRecipe(category, result, xp, time, ingredients).save(consumer, location(resultName + "_from_ingredients", 0));
        blastingRecipe(category, result, xp, blastTime, ingredients).save(consumer, location(resultName + "_from_ingredients", 1));
    }

    public static void smeltAndSmoke(RecipeCategory category, Item result, float xp, int time, RecipeOutput consumer, ItemLike... ingredients) {
        int smokeTime = time / 2;
        String resultName = result.toString().split(":")[1];
        smeltingRecipe(category, result, xp, time, ingredients).save(consumer, location(resultName + "_from_ingredients", 0));
        smokingRecipe(category, result, xp, smokeTime, ingredients).save(consumer, location(resultName + "_from_ingredients", 2));
    }

    public static SimpleCookingRecipeBuilder smeltingRecipe(RecipeCategory category, Item result, ItemLike input, float xp, int time) {
        String inputName = input.asItem().toString().split(":")[1];
        return SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), category, result, xp, time).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }

    public static SimpleCookingRecipeBuilder blastingRecipe(RecipeCategory category, Item result, ItemLike input, float xp, int time) {
        String inputName = input.asItem().toString().split(":")[1];
        return SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), category, result, xp, time).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }

    public static SimpleCookingRecipeBuilder smokingRecipe(RecipeCategory category, Item result, ItemLike input, float xp, int time) {
        String inputName = input.asItem().toString().split(":")[1];
        return SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), category, result, xp, time).unlockedBy("has_" + inputName, InventoryChangeTrigger.TriggerInstance.hasItems(input));
    }

    public static SimpleCookingRecipeBuilder smeltingRecipe(RecipeCategory category, Item result, float xp, int time, ItemLike... ingredients) {
        Ingredient ingredient = Ingredient.of(ingredients);
        return SimpleCookingRecipeBuilder.smelting(ingredient, category, result, xp, time).unlockedBy("has_ingredients", InventoryChangeTrigger.TriggerInstance.hasItems(ingredients));
    }

    public static SimpleCookingRecipeBuilder blastingRecipe(RecipeCategory category, Item result, float xp, int time, ItemLike... ingredients) {
        Ingredient ingredient = Ingredient.of(ingredients);
        return SimpleCookingRecipeBuilder.blasting(ingredient, category, result, xp, time).unlockedBy("has_ingredients", InventoryChangeTrigger.TriggerInstance.hasItems(ingredients));
    }

    public static SimpleCookingRecipeBuilder smokingRecipe(RecipeCategory category, Item result, float xp, int time, ItemLike... ingredients) {
        Ingredient ingredient = Ingredient.of(ingredients);
        return SimpleCookingRecipeBuilder.smoking(ingredient, category, result, xp, time).unlockedBy("has_ingredients", InventoryChangeTrigger.TriggerInstance.hasItems(ingredients));
    }
}
