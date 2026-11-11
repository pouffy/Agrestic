package io.github.pouffy.agrestic.compat.jei.category;

import com.mojang.serialization.Codec;
import io.github.pouffy.agrestic.AgresticConfig;
import io.github.pouffy.agrestic.client.AgresticSprites;
import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
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
import mezz.jei.api.recipe.RecipeIngredientRole;
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
import slimeknights.mantle.plugin.jei.fluid.FluidTooltipCallback;

import java.text.DecimalFormat;
import java.util.Collections;

import static io.github.pouffy.agrestic.compat.jei.AgresticJEIConstants.formatTime;

public class BrewingBarrelCategory extends AbstractRecipeCategory<RecipeHolder<BrewingBarrelRecipe>> {

    public BrewingBarrelCategory(IGuiHelper guiHelper) {
        super(AgresticJEIConstants.BREWING_BARREL, Component.translatable("emi.category.agrestic.brewing"), guiHelper.createDrawableItemLike(AgresticBlocks.BREWING_BARREL), 130, 80);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<BrewingBarrelRecipe> recipeHolder, IFocusGroup focuses) {
        BrewingBarrelRecipe recipe = recipeHolder.value();

        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = ErrorUtil.checkNotNull(minecraft.level, "level");
        RegistryAccess registryAccess = level.registryAccess();

        builder.addInputSlot(39, 23)
                .addRichTooltipCallback(FluidTooltipCallback.UNITS)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.TANK), -1, -1)
                .setFluidRenderer(1, false, 16, 32)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Collections.singletonList(recipe.getInput()));

        builder.addOutputSlot(93, 23)
                .addRichTooltipCallback(FluidTooltipCallback.UNITS)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.TANK), -1, -1)
                .setFluidRenderer(1, false, 16, 32)
                .addFluidStack(recipe.getOutput().getFluid(), 1);

        builder.addSlot(RecipeIngredientRole.CATALYST, 3, 31)
                .addRichTooltipCallback(FluidTooltipCallback.UNITS)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                .setFluidRenderer(1, false, 16, 16)
                .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable("recipe.agrestic.brewing.optional").withStyle(ChatFormatting.GOLD)))
                .addIngredients(NeoForgeTypes.FLUID_STACK, Collections.singletonList(recipe.getOutput()));
    }

    public static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#%");

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<BrewingBarrelRecipe> recipe, IFocusGroup focuses) {
        builder.addDrawableWidget(AgresticJEIConstants.asDrawable(AgresticSprites.TINY_ARROW_BASE)).setPosition(23, 34);
        IDrawableStatic tinyArrow = new DrawableSprite(Minecraft.getInstance().getGuiSprites(), AgresticSprites.TINY_ARROW.location, AgresticSprites.TINY_ARROW.width, AgresticSprites.TINY_ARROW.height);
        builder.addDrawableWidget(new DrawableAnimated(tinyArrow, recipe.value().getTime(), IDrawableAnimated.StartDirection.LEFT, false)).setPosition(23, 33);

        builder.addDrawableWidget(AgresticJEIConstants.asDrawable(AgresticSprites.ARROW_BASE)).setPosition(64, 31).setTooltip(Component.translatable("recipe.agrestic.time", formatTime(recipe.value().getTime())).withStyle(ChatFormatting.GOLD));
        IDrawableStatic arrow = new DrawableSprite(Minecraft.getInstance().getGuiSprites(), AgresticSprites.ARROW.location, AgresticSprites.ARROW.width, AgresticSprites.ARROW.height);
        builder.addDrawableWidget(new DrawableAnimated(arrow, recipe.value().getTime(), IDrawableAnimated.StartDirection.LEFT, false)).setPosition(64, 30);

        builder.addDrawableWidget(AgresticJEIConstants.asDrawable(AgresticSprites.BUBBLES_BASE)).setPosition(114, 24).setTooltip(Component.translatable("recipe.agrestic.brewing.quality_change",
                PERCENT_FORMAT.format(AgresticConfig.SERVER.minBrewQualityChange.get() / 100.0),
                PERCENT_FORMAT.format(AgresticConfig.SERVER.maxBrewQualityChange.get() / 100.0)).withStyle(ChatFormatting.GOLD));
        IDrawableStatic bubbles = new DrawableSprite(Minecraft.getInstance().getGuiSprites(), AgresticSprites.BUBBLES.location, AgresticSprites.BUBBLES.width, AgresticSprites.BUBBLES.height);
        builder.addDrawableWidget(new DrawableAnimated(bubbles, recipe.value().getTime(), IDrawableAnimated.StartDirection.BOTTOM, false)).setPosition(114, 24);
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<BrewingBarrelRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public Codec<RecipeHolder<BrewingBarrelRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }
}
