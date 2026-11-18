package io.github.pouffy.agrestic.compat.jei;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.client.ui.BrewingBarrelScreen;
import io.github.pouffy.agrestic.client.ui.CondenserScreen;
import io.github.pouffy.agrestic.common.item.ElixirItem;
import io.github.pouffy.agrestic.common.recipe.*;
import io.github.pouffy.agrestic.compat.jei.category.BrewingBarrelCategory;
import io.github.pouffy.agrestic.compat.jei.category.CondensingCategory;
import io.github.pouffy.agrestic.compat.jei.category.CrushingTubCategory;
import io.github.pouffy.agrestic.compat.jei.category.EvaporatingBasinCategory;
import io.github.pouffy.agrestic.compat.jei.util.GuiContainerTankHandler;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.Internal;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.library.plugins.vanilla.cooking.fuel.FuelRecipeMaker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.plugin.jei.MantleJEIConstants;

import java.util.List;

@JeiPlugin
public class AgresticJEIPlugin implements IModPlugin {
    private static final ResourceLocation ID = Agrestic.location("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Nullable
    private IRecipeCategory<RecipeHolder<CrushingTubRecipe>> crushingCategory;
    @Nullable
    private IRecipeCategory<RecipeHolder<EvaporatingBasinRecipe>> evaporatingCategory;
    @Nullable
    private IRecipeCategory<RecipeHolder<BrewingBarrelRecipe>> brewingCategory;
    @Nullable
    private IRecipeCategory<RecipeHolder<BasicCondensingRecipe>> condensingCategory;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        Textures textures = Internal.getTextures();
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        List<ItemStack> furnaceFuels = FuelRecipeMaker.getFuelRecipes(jeiHelpers.getIngredientManager()).stream()
                .flatMap(recipe -> recipe.getInputs().stream())
                .toList();
        registration.addRecipeCategories(
                crushingCategory = new CrushingTubCategory(guiHelper),
                evaporatingCategory = new EvaporatingBasinCategory(guiHelper),
                brewingCategory = new BrewingBarrelCategory(guiHelper),
                condensingCategory = new CondensingCategory(guiHelper, furnaceFuels)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IIngredientManager ingredientManager = registration.getIngredientManager();
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        ErrorUtil.checkNotNull(level, "minecraft.level");
        AgresticJEIRecipes agresticRecipes = new AgresticJEIRecipes(ingredientManager);

        registration.addRecipes(AgresticJEIConstants.CRUSHING_TUB, agresticRecipes.getCrushingRecipes());
        registration.addRecipes(AgresticJEIConstants.EVAPORATING_BASIN, agresticRecipes.getEvaporatingRecipes());
        registration.addRecipes(AgresticJEIConstants.BREWING_BARREL, agresticRecipes.getBrewingRecipes());
        registration.addRecipes(AgresticJEIConstants.CONDENSING, agresticRecipes.getCondensingRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(AgresticBlocks.CRUSHING_TUB, AgresticJEIConstants.CRUSHING_TUB);
        registration.addRecipeCatalyst(AgresticBlocks.EVAPORATING_BASIN, AgresticJEIConstants.EVAPORATING_BASIN);
        registration.addRecipeCatalyst(AgresticBlocks.BREWING_BARREL, AgresticJEIConstants.BREWING_BARREL);
        registration.addRecipeCatalyst(AgresticBlocks.BASIC_CONDENSER, AgresticJEIConstants.CONDENSING, RecipeTypes.FUELING);
        registration.addRecipeCatalyst(AgresticBlocks.BASIC_RETORT, AgresticJEIConstants.CONDENSING);

        registration.addRecipeCatalyst(AgresticBlocks.FLUID_BARREL, MantleJEIConstants.FLUID_TRANSFER);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(BrewingBarrelScreen.class, new GuiContainerTankHandler<>());
        registration.addGenericGuiContainerHandler(CondenserScreen.class, new GuiContainerTankHandler<>());
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(OliveOilingRecipe.class, AgresticJEIConstants.oliveOiling());
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration registration) {
        registration.addExtraItemStacks(ElixirItem.getElixirs());
    }
}
