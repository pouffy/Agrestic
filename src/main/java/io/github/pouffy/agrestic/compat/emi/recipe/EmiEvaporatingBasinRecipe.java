package io.github.pouffy.agrestic.compat.emi.recipe;

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import io.github.pouffy.agrestic.AgresticConfig;
import io.github.pouffy.agrestic.common.recipe.EvaporatingBasinRecipe;
import io.github.pouffy.agrestic.compat.emi.AgresticEmiPlugin;
import io.github.pouffy.agrestic.compat.emi.FluidTankWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

import static io.github.pouffy.agrestic.compat.emi.EmiUIUtils.formatTime;

public class EmiEvaporatingBasinRecipe extends BasicEmiRecipe {
    private final EvaporatingBasinRecipe recipe;
    private final HolderLookup.Provider registries;

    public EmiEvaporatingBasinRecipe(RecipeHolder<EvaporatingBasinRecipe> holder, HolderLookup.Provider registries) {
        super(AgresticEmiPlugin.BASIN_EVAPORATING, holder.id(), 108, 64);
        this.recipe = holder.value();
        this.registries = registries;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(NeoForgeEmiIngredient.of(recipe.getInput()));
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(recipe.getResultItem(registries)));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        EmiIngredient fluid = NeoForgeEmiIngredient.of(recipe.getInput());
        SlotWidget tank = widgets.add(FluidTankWidget.input(fluid, 20, 15, Math.min(recipe.getInput().amount() * 3, 6000)));
        widgets.addSlot(EmiStack.of(recipe.getResultItem(registries)), 70, 23).recipeContext(this);
        widgets.addTexture(AgresticEmiPlugin.ARROW_BG, 42, 23).tooltipText(List.of(Component.translatable("recipe.agrestic.time", formatTime(recipe.getTime())).withStyle(ChatFormatting.GOLD)));
        widgets.addAnimatedTexture(AgresticEmiPlugin.ARROW, 42, 22, recipe.getTime() * 20, true, false, false);
    }
}
