package io.github.pouffy.agrestic.core.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;

import javax.annotation.Nonnull;

public class ShapelessNoReturnRecipe extends ShapelessRecipe {
    protected final ItemStack result;

    public static final MapCodec<ShapelessNoReturnRecipe> CODEC = RecordCodecBuilder.mapCodec((obj) -> obj.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(ShapelessRecipe::getGroup),
            CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapelessRecipe::category),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter((recipe) -> recipe.result),
            Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap((list) -> {
                Ingredient[] aingredient = list.toArray(Ingredient[]::new);
                if (aingredient.length == 0) {
                    return DataResult.error(() -> "No ingredients for shapeless recipe");
                } else {
                    return aingredient.length > ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth() ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth())) : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
                }
            }, DataResult::success).forGetter(ShapelessRecipe::getIngredients)
    ).apply(obj, ShapelessNoReturnRecipe::new));

    public ShapelessNoReturnRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(group, category, result, ingredients);
        this.result = result;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> ret = super.getRemainingItems(inv);
        for (int i = 0; i < ret.size(); i++) {
            if (ret.get(i).getItem() == Items.BUCKET || ret.get(i).getItem() == Items.GLASS_BOTTLE/* || ret.get(i).getItem() == AgresticBlocks.FLUID_BARREL*/) {
                ret.set(i, ItemStack.EMPTY);
                break;
            }
        }
        return ret;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AgresticRecipeTypes.Serializers.SHAPELESS_NO_RETURN.get();
    }

}
