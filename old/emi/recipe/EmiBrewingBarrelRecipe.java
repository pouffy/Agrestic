package io.github.pouffy.agrestic.compat.emi.recipe;

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import io.github.pouffy.agrestic.AgresticConfig;
import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
import io.github.pouffy.agrestic.compat.emi.AgresticEmiPlugin;
import io.github.pouffy.agrestic.compat.emi.FluidTankWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.text.DecimalFormat;
import java.util.List;

import static io.github.pouffy.agrestic.compat.emi.EmiUIUtils.formatTime;

public class EmiBrewingBarrelRecipe extends BasicEmiRecipe {
    public static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#%");
    private final BrewingBarrelRecipe recipe;

    public EmiBrewingBarrelRecipe(RecipeHolder<BrewingBarrelRecipe> holder) {
        super(AgresticEmiPlugin.BREWING, holder.id(), 130, 80);
        this.recipe = holder.value();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiStack.of(recipe.getInput().getFluid(), recipe.getInput().getComponentsPatch(), 1));
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(recipe.getOutput().getFluid(), recipe.getOutput().getComponentsPatch(), 1));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        SlotWidget input = widgets.add(FluidTankWidget.input(NeoForgeEmiIngredient.of(FluidIngredient.single(recipe.getInput())), 39, 23, 1000));
        SlotWidget output = widgets.add(FluidTankWidget.result(recipe.getOutput().copyWithAmount(1000), 93, 23, 1000)).recipeContext(this);
        SlotWidget aux = widgets.add(FluidTankWidget.input(NeoForgeEmiIngredient.of(FluidIngredient.single(recipe.getOutput())), 3, 31, 1000).size(18, 18, 16, 16, AgresticEmiPlugin.RESULT));
        aux.appendTooltip(Component.translatable("recipe.agrestic.brewing.optional").withStyle(ChatFormatting.GOLD));

        widgets.addTexture(AgresticEmiPlugin.TINY_ARROW_BG, 23, 34);
        widgets.addAnimatedTexture(AgresticEmiPlugin.TINY_ARROW, 23, 33, recipe.getTime(), true, false, false);
        widgets.addTexture(AgresticEmiPlugin.ARROW_BG, 64, 31).tooltipText(List.of(Component.translatable("recipe.agrestic.time", formatTime(recipe.getTime())).withStyle(ChatFormatting.GOLD)));
        widgets.addAnimatedTexture(AgresticEmiPlugin.ARROW, 64, 30, recipe.getTime(), true, false, false);
        widgets.addTexture(AgresticEmiPlugin.BUBBLES_BG, 114, 24).tooltipText(List.of(
                Component.translatable("recipe.agrestic.brewing.quality_change",
                        PERCENT_FORMAT.format(AgresticConfig.SERVER.minBrewQualityChange.get() / 100.0),
                        PERCENT_FORMAT.format(AgresticConfig.SERVER.maxBrewQualityChange.get() / 100.0)).withStyle(ChatFormatting.GOLD)
        ));
        widgets.addAnimatedTexture(AgresticEmiPlugin.BUBBLES, 114, 24, recipe.getTime(), false, true, true);
    }
}
