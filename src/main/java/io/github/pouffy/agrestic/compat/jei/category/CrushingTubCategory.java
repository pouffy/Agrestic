package io.github.pouffy.agrestic.compat.jei.category;

import com.mojang.serialization.Codec;
import com.pouffydev.krystal_core.foundation.data.recipe.result.ChanceResult;
import io.github.pouffy.agrestic.client.AgresticSprites;
import io.github.pouffy.agrestic.common.recipe.CrushingTubRecipe;
import io.github.pouffy.agrestic.compat.jei.AgresticJEIConstants;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.common.util.ErrorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CrushingTubCategory extends AbstractRecipeCategory<RecipeHolder<CrushingTubRecipe>> {

    public CrushingTubCategory(IGuiHelper guiHelper) {
        super(AgresticJEIConstants.CRUSHING_TUB, Component.translatable("emi.category.agrestic.tub_crushing"), guiHelper.createDrawableItemLike(AgresticBlocks.CRUSHING_TUB), 108, 64);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CrushingTubRecipe> recipeHolder, IFocusGroup focuses) {
        CrushingTubRecipe recipe = recipeHolder.value();

        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = ErrorUtil.checkNotNull(minecraft.level, "level");
        RegistryAccess registryAccess = level.registryAccess();

        ChanceResult byproduct = recipe.getByproduct();
        boolean hasByproduct = byproduct != ChanceResult.EMPTY;

        builder.addInputSlot(20, 23)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                .addIngredients(recipe.getInput());

        builder.addOutputSlot(70, hasByproduct ? 7 : 15)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.TANK), -1, -1)
                .setFluidRenderer(Math.min(recipe.getResultFluid(registryAccess).getAmount() * 4, 8000), false, 16, 32)
                .addFluidStack(recipe.getResultFluid(registryAccess).getFluid(), recipe.getResultFluid(registryAccess).getAmount());

        if (hasByproduct) {
            var slot = builder.addOutputSlot(70, 42)
                    .setBackground(AgresticJEIConstants.asDrawable(byproduct.chance() < 1 ? AgresticSprites.CHANCE_SLOT : AgresticSprites.SLOT), -1, -1)
                    .addItemStack(recipe.getByproduct().stack());

            if (byproduct.chance() < 1) {
                float chance = byproduct.chance() * 100;
                String formattedChance = chance % 1 == 0 ? String.format("%d%%", (int) chance) : String.format("%.1f%%", chance);
                slot.addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable("recipe.agrestic.chance", formattedChance).withStyle(ChatFormatting.GOLD)));
            }
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<CrushingTubRecipe> recipe, IFocusGroup focuses) {
        builder.addDrawableWidget(AgresticJEIConstants.asDrawable(AgresticSprites.ARROW_BASE)).setPosition(42, 23);
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<CrushingTubRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public Codec<RecipeHolder<CrushingTubRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }
}
