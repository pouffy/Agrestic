package io.github.pouffy.agrestic.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.client.AgresticSprites;
import io.github.pouffy.agrestic.common.recipe.*;
import io.github.pouffy.agrestic.compat.emi.recipe.EmiBrewingBarrelRecipe;
import io.github.pouffy.agrestic.compat.emi.recipe.EmiCrushingTubRecipe;
import io.github.pouffy.agrestic.compat.emi.recipe.EmiEvaporatingBasinRecipe;
import io.github.pouffy.agrestic.compat.emi.recipe.EmiFluidTransferRecipe;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import io.github.pouffy.agrestic.mixin.RecipeManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

@EmiEntrypoint
public class AgresticEmiPlugin implements EmiPlugin {
    public static final EmiTexture TANK = fromSprite(AgresticSprites.TANK);
    public static final EmiTexture ARROW_BG = fromSprite(AgresticSprites.ARROW_BASE);
    public static final EmiTexture ARROW = fromSprite(AgresticSprites.ARROW);
    public static final EmiTexture TINY_ARROW_BG = fromSprite(AgresticSprites.TINY_ARROW_BASE);
    public static final EmiTexture TINY_ARROW = fromSprite(AgresticSprites.TINY_ARROW);
    public static final EmiTexture RESULT = fromSprite(AgresticSprites.SLOT);
    public static final EmiTexture RESULT_CHANCE = fromSprite(AgresticSprites.CHANCE_SLOT);
    public static final EmiTexture BUBBLES_BG = fromSprite(AgresticSprites.BUBBLES_BASE);
    public static final EmiTexture BUBBLES = fromSprite(AgresticSprites.BUBBLES);


    public static final EmiStack CRUSHING_TUB = EmiStack.of(AgresticBlocks.CRUSHING_TUB.get());
    public static final EmiStack EVAPORATING_BASIN = EmiStack.of(AgresticBlocks.EVAPORATING_BASIN.get());
    public static final EmiStack BREWING_BARREL = EmiStack.of(AgresticBlocks.BREWING_BARREL.get());

    public static final EmiRecipeCategory TUB_CRUSHING = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Agrestic.MODID, "tub_crushing"), CRUSHING_TUB);
    public static final EmiRecipeCategory BASIN_EVAPORATING = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Agrestic.MODID, "basin_evaporating"), EVAPORATING_BASIN);
    public static final EmiRecipeCategory BREWING = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Agrestic.MODID, "brewing"), BREWING_BARREL);
    public static final EmiRecipeCategory FLUID_TRANSFER = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Agrestic.MODID, "fluid_transfer"), EmiStack.of(Items.BUCKET));

    private static EmiTexture fromSprite(AgresticSprites sprite) {
        return new EmiTexture(sprite.location, 0, 0, sprite.width, sprite.height);
    }

    public AgresticEmiPlugin() {
    }

    @Override
    public void register(EmiRegistry registry) {
        Level level = Minecraft.getInstance().level;

        // Tell EMI to add a tab for your category
        registry.addCategory(TUB_CRUSHING);
        registry.addCategory(BASIN_EVAPORATING);
        registry.addCategory(BREWING);
        registry.addCategory(FLUID_TRANSFER);

        // Add all the workstations your category uses
        registry.addWorkstation(TUB_CRUSHING, CRUSHING_TUB);
        registry.addWorkstation(BASIN_EVAPORATING, EVAPORATING_BASIN);
        registry.addWorkstation(BREWING, BREWING_BARREL);
        RecipeManager manager = registry.getRecipeManager();

        for (RecipeHolder<CrushingTubRecipe> recipe : manager.getAllRecipesFor(AgresticRecipeTypes.CRUSHING_TUB.get())) {
            registry.addRecipe(new EmiCrushingTubRecipe(recipe, ((RecipeManagerAccessor) registry.getRecipeManager()).getRegistries()));
        }
        for (RecipeHolder<EvaporatingBasinRecipe> recipe : manager.getAllRecipesFor(AgresticRecipeTypes.EVAPORATING_BASIN.get())) {
            registry.addRecipe(new EmiEvaporatingBasinRecipe(recipe, ((RecipeManagerAccessor) registry.getRecipeManager()).getRegistries()));
        }
        for (RecipeHolder<BrewingBarrelRecipe> recipe : manager.getAllRecipesFor(AgresticRecipeTypes.BREWING_BARREL.get())) {
            registry.addRecipe(new EmiBrewingBarrelRecipe(recipe));
        }
        for (RecipeHolder<EmptyingRecipe> recipe : manager.getAllRecipesFor(AgresticRecipeTypes.EMPTYING.get())) {
            registry.addRecipe(EmiFluidTransferRecipe.emptying(recipe, ((RecipeManagerAccessor) registry.getRecipeManager()).getRegistries()));
        }
        for (RecipeHolder<FillingRecipe> recipe : manager.getAllRecipesFor(AgresticRecipeTypes.FILLING.get())) {
            registry.addRecipe(EmiFluidTransferRecipe.filling(recipe, ((RecipeManagerAccessor) registry.getRecipeManager()).getRegistries()));
        }
    }
}
