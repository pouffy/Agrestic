package io.github.pouffy.agrestic.common.recipe;

import io.github.pouffy.agrestic.core.item.OliveOiled;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import io.github.pouffy.agrestic.init.AgresticItems;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import io.github.pouffy.agrestic.init.AgresticTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class OliveOilingRecipe extends CustomRecipe {
    public OliveOilingRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        int numStacks = craftingInput.ingredientCount();
        if (numStacks != 2) {
            return false;
        }

        ItemStack food = ItemStack.EMPTY;
        ItemStack oil = ItemStack.EMPTY;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (stack.isEmpty()) continue;

            if (isValidFood(stack) && food.isEmpty() ) {
                if (stack.has(AgresticDataComponents.OLIVE_OILED)) {
                    return false;
                }
                food = stack;
            } else if (stack.is(AgresticItems.OLIVE_OIL_BOTTLE) && oil.isEmpty()) {
                oil = stack;
            } else {
                return false;
            }
        }
        return !food.isEmpty() && !oil.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        ItemStack foodStack = ItemStack.EMPTY;
        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty() && isValidFood(stack)) {
                foodStack = stack;
                break;
            }
        }

        if (foodStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = foodStack.copyWithCount(1);
        result.set(AgresticDataComponents.OLIVE_OILED, new OliveOiled());

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AgresticRecipeTypes.Serializers.OLIVE_OILING.get();
    }

    public static boolean isValidFood(ItemStack stack) {
        if (!stack.has(DataComponents.FOOD)) return false;
        return stack.is(AgresticTags.OLIVE_OILING_WHITELIST);
    }
}
