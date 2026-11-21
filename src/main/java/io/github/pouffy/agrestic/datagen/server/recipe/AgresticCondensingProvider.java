package io.github.pouffy.agrestic.datagen.server.recipe;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.item.ElixirItem;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.AdvancedCondensingRecipeBuilder;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.AgresticRecipeProvider;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.CondensingRecipeBuilder;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticEffects;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.BlockTagIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.Nullable;
import java.util.List;
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
        condense(ElixirItem.withEffect(MobEffects.HEAL, 0, 1), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticItems.MARSH_MALLOW), List.of(Ingredient.of(AgresticBlocks.CHAMOMILE), Ingredient.of(Items.BEEF)), 400, output, "instant_health_amplified");

        condense(ElixirItem.withEffect(MobEffects.REGENERATION, 900, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.COHOSH), Ingredient.of(Items.HONEYCOMB), 400, output, "regeneration");
        condense(ElixirItem.withEffect(MobEffects.REGENERATION, 1800, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.HORSETAIL), List.of(Ingredient.of(AgresticBlocks.COHOSH), Ingredient.of(Items.HONEYCOMB)), 400, output, "regeneration_lengthened");
        condense(ElixirItem.withEffect(MobEffects.REGENERATION, 450, 1), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticItems.MARSH_MALLOW), List.of(Ingredient.of(AgresticBlocks.COHOSH), Ingredient.of(Items.HONEYCOMB)), 400, output, "regeneration_amplified");

        condense(ElixirItem.withEffect(MobEffects.WITHER, 900, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.DEATHSTALK), new BlockTagIngredient(BlockTags.WITHER_SUMMON_BASE_BLOCKS).toVanilla(), 400, output, "wither");
        condense(ElixirItem.withEffect(MobEffects.WITHER, 1800, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.HORSETAIL), List.of(Ingredient.of(AgresticBlocks.DEATHSTALK), new BlockTagIngredient(BlockTags.WITHER_SUMMON_BASE_BLOCKS).toVanilla()), 400, output, "wither_lengthened");
        condense(ElixirItem.withEffect(MobEffects.WITHER, 450, 1), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticItems.MARSH_MALLOW), List.of(Ingredient.of(AgresticBlocks.DEATHSTALK), new BlockTagIngredient(BlockTags.WITHER_SUMMON_BASE_BLOCKS).toVanilla()), 400, output, "wither_amplified");

        condense(ElixirItem.withEffect(MobEffects.NIGHT_VISION, 3600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.MOONCAP), Ingredient.of(Items.SPIDER_EYE), 400, output, "night_vision");
        condense(ElixirItem.withEffect(MobEffects.WITHER, 9600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.HORSETAIL), List.of(Ingredient.of(AgresticBlocks.MOONCAP), Ingredient.of(Items.SPIDER_EYE)), 400, output, "night_vision_lengthened");

        condense(ElixirItem.withEffect(MobEffects.MOVEMENT_SPEED, 3600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.WIND_THISTLE), Ingredient.of(Items.SUGAR), 400, output, "speed");
        condense(ElixirItem.withEffect(MobEffects.REGENERATION, 9600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.HORSETAIL), List.of(Ingredient.of(AgresticBlocks.WIND_THISTLE), Ingredient.of(Items.SUGAR)), 400, output, "speed_lengthened");
        condense(ElixirItem.withEffect(MobEffects.REGENERATION, 1800, 1), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticItems.MARSH_MALLOW), List.of(Ingredient.of(AgresticBlocks.WIND_THISTLE), Ingredient.of(Items.SUGAR)), 400, output, "speed_amplified");

        condense(ElixirItem.withEffect(MobEffects.FIRE_RESISTANCE, 3600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), null, List.of(Ingredient.of(AgresticBlocks.ALOE_VERA), Ingredient.of(Tags.Items.BRICKS_NORMAL), Ingredient.of(ItemTags.COALS)), 400, output, "fire_resistance");
        condense(ElixirItem.withEffect(MobEffects.FIRE_RESISTANCE, 9600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.HORSETAIL), List.of(Ingredient.of(AgresticBlocks.ALOE_VERA), Ingredient.of(Tags.Items.BRICKS_NORMAL), Ingredient.of(ItemTags.COALS)), 400, output, "fire_resistance_lengthened");

        condense(ElixirItem.withEffect(MobEffects.HEALTH_BOOST, 3600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), null, List.of(Ingredient.of(AgresticBlocks.BLOOD_ORCHID), Ingredient.of(Items.ROTTEN_FLESH), Ingredient.of(Tags.Items.DUSTS_REDSTONE)), 400, output, "health_boost");
        condense(ElixirItem.withEffect(MobEffects.HEALTH_BOOST, 9600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.HORSETAIL), List.of(Ingredient.of(AgresticBlocks.BLOOD_ORCHID), Ingredient.of(Items.ROTTEN_FLESH), Ingredient.of(Tags.Items.DUSTS_REDSTONE)), 400, output, "health_boost_lengthened");
        condense(ElixirItem.withEffect(MobEffects.HEALTH_BOOST, 1800, 1), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticItems.MARSH_MALLOW), List.of(Ingredient.of(AgresticBlocks.BLOOD_ORCHID), Ingredient.of(Items.ROTTEN_FLESH), Ingredient.of(Tags.Items.DUSTS_REDSTONE)), 400, output, "health_boost_amplified");

        condense(ElixirItem.withEffect(MobEffects.DIG_SPEED, 3600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), null, List.of(Ingredient.of(AgresticItems.CORE_ROOT), Ingredient.of(Tags.Items.NUGGETS_IRON), Ingredient.of(Tags.Items.DUSTS_REDSTONE)), 400, output, "haste");
        condense(ElixirItem.withEffect(MobEffects.DIG_SPEED, 9600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.HORSETAIL), List.of(Ingredient.of(AgresticItems.CORE_ROOT), Ingredient.of(Tags.Items.NUGGETS_IRON), Ingredient.of(Tags.Items.DUSTS_REDSTONE)), 400, output, "haste_lengthened");
        condense(ElixirItem.withEffect(MobEffects.DIG_SPEED, 1800, 1), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticItems.MARSH_MALLOW), List.of(Ingredient.of(AgresticItems.CORE_ROOT), Ingredient.of(Tags.Items.NUGGETS_IRON), Ingredient.of(Tags.Items.DUSTS_REDSTONE)), 400, output, "haste_amplified");

        condense(ElixirItem.withEffect(MobEffects.DAMAGE_BOOST, 3600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), null, List.of(Ingredient.of(AgresticItems.GINSENG), Ingredient.of(Tags.Items.BONES), Ingredient.of(Tags.Items.GUNPOWDERS)), 400, output, "strength");
        condense(ElixirItem.withEffect(MobEffects.DAMAGE_BOOST, 9600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.HORSETAIL), List.of(Ingredient.of(AgresticItems.GINSENG), Ingredient.of(Tags.Items.BONES), Ingredient.of(Tags.Items.GUNPOWDERS)), 400, output, "strength_lengthened");
        condense(ElixirItem.withEffect(MobEffects.DAMAGE_BOOST, 1800, 1), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticItems.MARSH_MALLOW), List.of(Ingredient.of(AgresticItems.GINSENG), Ingredient.of(Tags.Items.BONES), Ingredient.of(Tags.Items.GUNPOWDERS)), 400, output, "strength_amplified");

        condense(ElixirItem.withEffect(AgresticEffects.IRONSKIN, 3600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), null, List.of(Ingredient.of(AgresticItems.IRONBERRIES), Ingredient.of(Tags.Items.LEATHERS), Ingredient.of(Items.CLAY_BALL)), 400, output, "ironskin");
        condense(ElixirItem.withEffect(AgresticEffects.IRONSKIN, 9600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.HORSETAIL), List.of(Ingredient.of(AgresticItems.IRONBERRIES), Ingredient.of(Tags.Items.LEATHERS), Ingredient.of(Items.CLAY_BALL)), 400, output, "ironskin_lengthened");
        condense(ElixirItem.withEffect(AgresticEffects.IRONSKIN, 1800, 1), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticItems.MARSH_MALLOW), List.of(Ingredient.of(AgresticItems.IRONBERRIES), Ingredient.of(Tags.Items.LEATHERS), Ingredient.of(Items.CLAY_BALL)), 400, output, "ironskin_amplified");

        condense(ElixirItem.withEffect(MobEffects.SLOW_FALLING, 3600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), null, List.of(Ingredient.of(AgresticItems.CLOUDSBLUFF), Ingredient.of(Tags.Items.FEATHERS), Ingredient.of(Items.PAPER)), 400, output, "slow_falling");
        condense(ElixirItem.withEffect(MobEffects.SLOW_FALLING, 9600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.HORSETAIL), List.of(Ingredient.of(AgresticItems.CLOUDSBLUFF), Ingredient.of(Tags.Items.FEATHERS), Ingredient.of(Items.PAPER)), 400, output, "slow_falling_lengthened");

        condense(ElixirItem.withEffect(AgresticEffects.BLAZING_TRAIL, 3600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), null, List.of(Ingredient.of(AgresticItems.CHILLI_PEPPER), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.NETHERRACK)), 400, output, "blazing_trail");
        condense(ElixirItem.withEffect(AgresticEffects.BLAZING_TRAIL, 9600, 0), SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(AgresticBlocks.HORSETAIL), List.of(Ingredient.of(AgresticItems.CHILLI_PEPPER), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.NETHERRACK)), 400, output, "blazing_trail_lengthened");
    }

    public void condense(ItemStack result, SizedFluidIngredient base, Ingredient first, @Nullable Ingredient second, int time, RecipeOutput recipeOutput, String path) {
        var builder = new CondensingRecipeBuilder(base, result).time(time).require(first).container(Ingredient.of(Items.GLASS_BOTTLE));
        if (second != null)
            builder.require(second);
        builder.save(recipeOutput, Agrestic.location("basic_condensing/").withSuffix(path));
    }

    public void condense(ItemStack result, SizedFluidIngredient base, @Nullable Ingredient modifier, List<Ingredient> inputs, int time, RecipeOutput recipeOutput, String path) {
        var builder = new AdvancedCondensingRecipeBuilder(base, result).time(time);
        for (Ingredient ingredient : inputs) {
            builder.require(ingredient);
        }
        if (modifier != null) {
            builder.modifier(modifier);
        }
        builder.container(Ingredient.of(Items.GLASS_BOTTLE));

        builder.save(recipeOutput, Agrestic.location("advanced_condensing/").withSuffix(path));
    }
}
