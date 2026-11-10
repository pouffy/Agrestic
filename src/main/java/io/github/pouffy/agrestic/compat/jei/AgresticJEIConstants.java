package io.github.pouffy.agrestic.compat.jei;

import io.github.pouffy.agrestic.client.AgresticSprites;
import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
import io.github.pouffy.agrestic.common.recipe.CrushingTubRecipe;
import io.github.pouffy.agrestic.common.recipe.EvaporatingBasinRecipe;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.RecipeHolder;

public class AgresticJEIConstants {

    public static final RecipeType<RecipeHolder<CrushingTubRecipe>> CRUSHING_TUB = RecipeType.createFromVanilla(AgresticRecipeTypes.CRUSHING_TUB.get());
    public static final RecipeType<RecipeHolder<EvaporatingBasinRecipe>> EVAPORATING_BASIN = RecipeType.createFromVanilla(AgresticRecipeTypes.EVAPORATING_BASIN.get());
    public static final RecipeType<RecipeHolder<BrewingBarrelRecipe>> BREWING_BARREL = RecipeType.createFromVanilla(AgresticRecipeTypes.BREWING_BARREL.get());

    public static IDrawable asDrawable(AgresticSprites sprite) {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return sprite.width;
            }

            @Override
            public int getHeight() {
                return sprite.height;
            }

            @Override
            public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
                sprite.blit(graphics, xOffset, yOffset);
            }
        };
    }

    public static String formatTime(int ticks) {
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
