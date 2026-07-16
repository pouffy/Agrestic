package io.github.pouffy.agrestic.datagen.server.recipe.builder;

import io.github.pouffy.agrestic.core.recipe.ShapelessNoReturnRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ShapelessNoReturnRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final ItemStack resultStack;
    private final NonNullList<Ingredient> ingredients;
    private final Map<String, Criterion<?>> criteria;
    @Nullable
    private String group;

    public ShapelessNoReturnRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        this(category, new ItemStack(result, count));
    }

    public ShapelessNoReturnRecipeBuilder(RecipeCategory category, ItemStack result) {
        this.ingredients = NonNullList.create();
        this.criteria = new LinkedHashMap();
        this.category = category;
        this.result = result.getItem();
        this.count = result.getCount();
        this.resultStack = result;
    }

    public static ShapelessNoReturnRecipeBuilder shapeless(RecipeCategory category, ItemLike result) {
        return new ShapelessNoReturnRecipeBuilder(category, result, 1);
    }

    public static ShapelessNoReturnRecipeBuilder shapeless(RecipeCategory category, ItemLike result, int count) {
        return new ShapelessNoReturnRecipeBuilder(category, result, count);
    }

    public static ShapelessNoReturnRecipeBuilder shapeless(RecipeCategory category, ItemStack result) {
        return new ShapelessNoReturnRecipeBuilder(category, result);
    }

    public ShapelessNoReturnRecipeBuilder requires(TagKey<Item> tag) {
        return this.requires(Ingredient.of(tag));
    }

    public ShapelessNoReturnRecipeBuilder requires(ItemLike item) {
        return this.requires((ItemLike)item, 1);
    }

    public ShapelessNoReturnRecipeBuilder requires(ItemLike item, int quantity) {
        for(int i = 0; i < quantity; ++i) {
            this.requires(Ingredient.of(new ItemLike[]{item}));
        }

        return this;
    }

    public ShapelessNoReturnRecipeBuilder requires(Ingredient ingredient) {
        return this.requires((Ingredient)ingredient, 1);
    }

    public ShapelessNoReturnRecipeBuilder requires(Ingredient ingredient, int quantity) {
        for(int i = 0; i < quantity; ++i) {
            this.ingredients.add(ingredient);
        }

        return this;
    }

    public ShapelessNoReturnRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public ShapelessNoReturnRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    public @NotNull Item getResult() {
        return this.result;
    }

    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        this.ensureValid(id);
        Advancement.Builder advancement$builder = recipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
        Objects.requireNonNull(advancement$builder);
        this.criteria.forEach(advancement$builder::addCriterion);
        ShapelessNoReturnRecipe recipe = new ShapelessNoReturnRecipe(Objects.requireNonNullElse(this.group, ""), RecipeBuilder.determineBookCategory(this.category), this.resultStack, this.ingredients);
        recipeOutput.accept(id, recipe, advancement$builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }
}
