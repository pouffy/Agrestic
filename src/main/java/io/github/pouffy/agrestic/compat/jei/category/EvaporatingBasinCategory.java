package io.github.pouffy.agrestic.compat.jei.category;

import com.mojang.serialization.Codec;
import io.github.pouffy.agrestic.client.AgresticSprites;
import io.github.pouffy.agrestic.common.recipe.CrushingTubRecipe;
import io.github.pouffy.agrestic.common.recipe.EvaporatingBasinRecipe;
import io.github.pouffy.agrestic.compat.jei.AgresticJEIConstants;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.common.Internal;
import mezz.jei.common.gui.elements.DrawableAnimated;
import mezz.jei.common.gui.elements.DrawableSprite;
import mezz.jei.common.util.ErrorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Arrays;

import static io.github.pouffy.agrestic.compat.jei.AgresticJEIConstants.formatTime;

public class EvaporatingBasinCategory extends AbstractRecipeCategory<RecipeHolder<EvaporatingBasinRecipe>> {

    public EvaporatingBasinCategory(IGuiHelper guiHelper) {
        super(AgresticJEIConstants.EVAPORATING_BASIN, Component.translatable("emi.category.agrestic.basin_evaporating"), guiHelper.createDrawableItemLike(AgresticBlocks.EVAPORATING_BASIN), 108, 64);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<EvaporatingBasinRecipe> recipeHolder, IFocusGroup focuses) {
        EvaporatingBasinRecipe recipe = recipeHolder.value();

        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = ErrorUtil.checkNotNull(minecraft.level, "level");
        RegistryAccess registryAccess = level.registryAccess();

        builder.addInputSlot(20, 15)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.TANK), -1, -1)
                .setFluidRenderer(Math.min(recipe.getInput().amount() * 3, 6000), false, 16, 32)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(recipe.getInput().getFluids()));

        builder.addInputSlot(70, 23)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                .addItemStack(recipe.getResultItem(registryAccess));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<EvaporatingBasinRecipe> recipe, IFocusGroup focuses) {
        builder.addDrawableWidget(AgresticJEIConstants.asDrawable(AgresticSprites.ARROW_BASE)).setPosition(42, 23).setTooltip(Component.translatable("recipe.agrestic.time", formatTime(recipe.value().getTime())).withStyle(ChatFormatting.GOLD));
        IDrawableStatic arrow = new DrawableSprite(Minecraft.getInstance().getGuiSprites(), AgresticSprites.ARROW.location, AgresticSprites.ARROW.width, AgresticSprites.ARROW.height);
        builder.addDrawableWidget(new DrawableAnimated(arrow, recipe.value().getTime(), IDrawableAnimated.StartDirection.LEFT, false)).setPosition(42, 23);
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<EvaporatingBasinRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public Codec<RecipeHolder<EvaporatingBasinRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }
}
