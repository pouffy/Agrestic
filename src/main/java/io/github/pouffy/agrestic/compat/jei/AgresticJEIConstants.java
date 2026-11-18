package io.github.pouffy.agrestic.compat.jei;

import io.github.pouffy.agrestic.client.AgresticSprites;
import io.github.pouffy.agrestic.common.recipe.*;
import io.github.pouffy.agrestic.core.item.OliveOiled;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import io.github.pouffy.agrestic.init.AgresticItems;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AgresticJEIConstants {

    public static final RecipeType<RecipeHolder<CrushingTubRecipe>> CRUSHING_TUB = RecipeType.createFromVanilla(AgresticRecipeTypes.CRUSHING_TUB.get());
    public static final RecipeType<RecipeHolder<EvaporatingBasinRecipe>> EVAPORATING_BASIN = RecipeType.createFromVanilla(AgresticRecipeTypes.EVAPORATING_BASIN.get());
    public static final RecipeType<RecipeHolder<BrewingBarrelRecipe>> BREWING_BARREL = RecipeType.createFromVanilla(AgresticRecipeTypes.BREWING_BARREL.get());
    public static final RecipeType<RecipeHolder<BasicCondensingRecipe>> CONDENSING = RecipeType.createFromVanilla(AgresticRecipeTypes.CONDENSING.get());

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

    public static ICraftingCategoryExtension<OliveOilingRecipe> oliveOiling() {
        return new ICraftingCategoryExtension<>() {
            @Override
            public void setRecipe(RecipeHolder<OliveOilingRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
                List<ItemStack> validFoods = getValidFoods();
                if (validFoods.isEmpty()) return;

                List<ItemStack> foods = new ArrayList<>();
                List<ItemStack> outPuts = new ArrayList<>();

                List<IFocus<?>> allFocuses = focuses.getAllFocuses();

                boolean foundSpecificFocus = false;

                for (ItemStack food : validFoods) {
                    ItemStack oiled = food.copy();
                    oiled.set(AgresticDataComponents.OLIVE_OILED, new OliveOiled());

                    for (IFocus<?> focus : allFocuses) {
                        ITypedIngredient<?> typedIngredient = focus.getTypedValue();

                        if (typedIngredient.getType() == VanillaTypes.ITEM_STACK) {
                            ItemStack stack = (ItemStack) typedIngredient.getIngredient();

                            if (areStacksEqual(stack, food)) {
                                foods.clear();
                                foods.add(food);
                                outPuts.clear();
                                outPuts.add(oiled);
                                foundSpecificFocus = true;
                                break;
                            }

                            if (areStacksEqual(stack, oiled)) {
                                foods.clear();
                                foods.add(food);
                                outPuts.clear();
                                outPuts.add(oiled);
                                foundSpecificFocus = true;
                                break;
                            }
                        }
                    }

                    if (foundSpecificFocus) {
                        break;
                    }

                    foods.add(food);
                    outPuts.add(oiled);
                }

                List<List<ItemStack>> inputs = new ArrayList<>();
                inputs.add(foods);
                inputs.add(List.of(AgresticItems.OLIVE_OIL_BOTTLE.stack()));

                craftingGridHelper.createAndSetInputs(builder, VanillaTypes.ITEM_STACK, inputs, 0, 0);
                craftingGridHelper.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, outPuts);
            }

            private boolean areStacksEqual(ItemStack stack1, ItemStack stack2) {
                if (!stack1.is(stack2.getItem())) {
                    return false;
                }
                return stack1.getComponents().equals(stack2.getComponents());
            }

            private List<ItemStack> getValidFoods() {
                return BuiltInRegistries.ITEM.stream()
                        .filter(item -> OliveOilingRecipe.isValidFood(new ItemStack(item)))
                        .map(ItemStack::new)
                        .collect(Collectors.toList());
            }
        };
    }
}
