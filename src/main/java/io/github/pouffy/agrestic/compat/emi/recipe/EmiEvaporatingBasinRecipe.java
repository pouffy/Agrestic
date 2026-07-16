package io.github.pouffy.agrestic.compat.emi.recipe;

import com.mojang.datafixers.util.Either;
import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.recipe.EvaporatingBasinRecipe;
import io.github.pouffy.agrestic.compat.emi.AgresticEmiPlugin;
import io.github.pouffy.agrestic.compat.emi.FluidTankWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collections;
import java.util.List;

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
        SlotWidget tank = widgets.add(FluidTankWidget.input(NeoForgeEmiIngredient.of(recipe.getInput()), 20, 15, 6000)).recipeContext(this);
        widgets.addSlot(EmiStack.of(recipe.getResultItem(registries)), 70, 23).recipeContext(this);
        widgets.addTexture(AgresticEmiPlugin.ARROW, 42, 23).tooltipText(List.of(Component.translatable("recipe.agrestic.time", formatTime(recipe.getTime())).withStyle(ChatFormatting.GOLD)));
    }

    private String formatTime(int ticks) {
        double seconds = ticks / 20.0;

        if (seconds < 60) {
            // Less than 1 minute = show seconds
            if (seconds == (int) seconds) {
                return String.format("%ds", (int) seconds);
            } else {
                return String.format("%.1fs", seconds);
            }
        } else {
            // 1 minute or more = show minutes and seconds
            int minutes = (int) (seconds / 60);
            int remainingSeconds = (int) (seconds % 60);
            if (remainingSeconds == 0) {
                return String.format("%dm", minutes);

            } else {
                return String.format("%dm %ds", minutes, remainingSeconds);
            }
        }
    }
}
