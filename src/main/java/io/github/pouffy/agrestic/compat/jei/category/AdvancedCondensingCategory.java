package io.github.pouffy.agrestic.compat.jei.category;

import com.mojang.serialization.Codec;
import io.github.pouffy.agrestic.client.AgresticSprites;
import io.github.pouffy.agrestic.common.recipe.AdvancedCondensingRecipe;
import io.github.pouffy.agrestic.common.recipe.BasicCondensingRecipe;
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
import mezz.jei.common.gui.elements.DrawableAnimated;
import mezz.jei.common.gui.elements.DrawableResource;
import mezz.jei.common.util.ErrorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import slimeknights.mantle.plugin.jei.fluid.FluidTooltipCallback;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.github.pouffy.agrestic.compat.jei.AgresticJEIConstants.formatTime;

public class AdvancedCondensingCategory extends AbstractRecipeCategory<RecipeHolder<AdvancedCondensingRecipe>> {
    private final List<ItemStack> furnaceFuels;

    public AdvancedCondensingCategory(IGuiHelper guiHelper, List<ItemStack> furnaceFuels) {
        super(AgresticJEIConstants.ADVANCED_CONDENSING, Component.translatable("emi.category.agrestic.advanced_alchemy"), guiHelper.createDrawableItemLike(AgresticBlocks.ADVANCED_CONDENSER), 130, 80);
        this.furnaceFuels = List.copyOf(furnaceFuels);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<AdvancedCondensingRecipe> recipeHolder, IFocusGroup focuses) {
        AdvancedCondensingRecipe recipe = recipeHolder.value();

        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = ErrorUtil.checkNotNull(minecraft.level, "level");
        RegistryAccess registryAccess = level.registryAccess();

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 46, 59)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                .addItemStacks(furnaceFuels);

        builder.addInputSlot(6, 7)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                .addIngredients(Optional.of(recipe.getIngredients().get(0)).orElse(Ingredient.EMPTY));

        Ingredient second = recipe.getIngredients().size() > 1 ? recipe.getIngredients().get(1) : Ingredient.EMPTY;
        builder.addInputSlot(6, 31)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                .addIngredients(second);

        Ingredient third = recipe.getIngredients().size() > 2 ? recipe.getIngredients().get(2) : Ingredient.EMPTY;
        builder.addInputSlot(6, 55)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                .addIngredients(third);

        if (recipe.getContainer().isPresent()) {
            builder.addInputSlot(82, 5)
                    .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                    .addIngredients(recipe.getContainer().get());
        }

        if (recipe.getModifier().isPresent()) {
            builder.addInputSlot(46, 5)
                    .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                    .addIngredients(recipe.getModifier().get());
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, 109, 23)
                .addRichTooltipCallback(FluidTooltipCallback.UNITS)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.TANK), -1, -1)
                .setFluidRenderer(Math.min(recipe.getBase().amount() * 4, 8000), false, 16, 32)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(recipe.getBase().getFluids()));

        builder.addOutputSlot(82, 31)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.BIG_SLOT), -5, -5)
                .addItemStack(recipe.getResultItem(registryAccess));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<AdvancedCondensingRecipe> recipeHolder, IFocusGroup focuses) {
        AdvancedCondensingRecipe recipe = recipeHolder.value();
        if (recipe.getModifier().isPresent()) {
            builder.addDrawableWidget(AgresticJEIConstants.asDrawable(AgresticSprites.ADV_ALCHEMY_ARROW_BASE_MOD)).setPosition(23, 15);
            IDrawableStatic arrow1 = new DrawableResource(AgresticSprites.ADV_ALCHEMY_ARROW_MOD.location, 0, 0, AgresticSprites.ADV_ALCHEMY_ARROW_MOD.width, AgresticSprites.ADV_ALCHEMY_ARROW_MOD.height, 0, 0, 0, 0, AgresticSprites.ADV_ALCHEMY_ARROW_MOD.width, AgresticSprites.ADV_ALCHEMY_ARROW_MOD.height);
            IDrawableAnimated animatedFill1 = new DrawableAnimated(arrow1, recipe.getTime(), IDrawableAnimated.StartDirection.LEFT, false);
            builder.addDrawableWidget(animatedFill1).setPosition(23, 14);
        }
        builder.addDrawableWidget(AgresticJEIConstants.asDrawable(AgresticSprites.ADV_ALCHEMY_ARROW_BASE)).setPosition(23, 15);
        IDrawableStatic arrow = new DrawableResource(AgresticSprites.ADV_ALCHEMY_ARROW.location, 0, 0, AgresticSprites.ADV_ALCHEMY_ARROW.width, AgresticSprites.ADV_ALCHEMY_ARROW.height, 0, 0, 0, 0, AgresticSprites.ADV_ALCHEMY_ARROW.width, AgresticSprites.ADV_ALCHEMY_ARROW.height);
        IDrawableAnimated animatedFill = new DrawableAnimated(arrow, recipe.getTime(), IDrawableAnimated.StartDirection.LEFT, false);
        builder.addDrawableWidget(animatedFill).setPosition(23, 14);
        if (recipe.getIngredients().size() > 1) {
            builder.addDrawableWidget(AgresticJEIConstants.asDrawable(AgresticSprites.ADV_ALCHEMY_ARROW_BASE_TWO)).setPosition(23, 15);
            IDrawableStatic arrow1 = new DrawableResource(AgresticSprites.ADV_ALCHEMY_ARROW_TWO.location, 0, 0, AgresticSprites.ADV_ALCHEMY_ARROW_TWO.width, AgresticSprites.ADV_ALCHEMY_ARROW_TWO.height, 0, 0, 0, 0, AgresticSprites.ADV_ALCHEMY_ARROW_TWO.width, AgresticSprites.ADV_ALCHEMY_ARROW_TWO.height);
            IDrawableAnimated animatedFill1 = new DrawableAnimated(arrow1, recipe.getTime(), IDrawableAnimated.StartDirection.LEFT, false);
            builder.addDrawableWidget(animatedFill1).setPosition(23, 14);
        }
        if (recipe.getIngredients().size() > 2) {
            builder.addDrawableWidget(AgresticJEIConstants.asDrawable(AgresticSprites.ADV_ALCHEMY_ARROW_BASE_THREE)).setPosition(23, 15);
            IDrawableStatic arrow1 = new DrawableResource(AgresticSprites.ADV_ALCHEMY_ARROW_THREE.location, 0, 0, AgresticSprites.ADV_ALCHEMY_ARROW_THREE.width, AgresticSprites.ADV_ALCHEMY_ARROW_THREE.height, 0, 0, 0, 0, AgresticSprites.ADV_ALCHEMY_ARROW_THREE.width, AgresticSprites.ADV_ALCHEMY_ARROW_THREE.height);
            IDrawableAnimated animatedFill1 = new DrawableAnimated(arrow1, recipe.getTime(), IDrawableAnimated.StartDirection.LEFT, false);
            builder.addDrawableWidget(animatedFill1).setPosition(23, 14);
        }
        builder.addAnimatedRecipeFlameWidget(recipe.getTime())
                .setPosition(47, 42);

        builder.addTooltipArea(23, 27, 50, 28).setTooltip(Component.translatable("recipe.agrestic.time", formatTime(recipe.getTime())).withStyle(ChatFormatting.GOLD));
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<AdvancedCondensingRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public Codec<RecipeHolder<AdvancedCondensingRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }
}
