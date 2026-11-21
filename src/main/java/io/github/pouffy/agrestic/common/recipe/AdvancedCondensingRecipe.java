package io.github.pouffy.agrestic.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;
import java.util.Optional;

public class AdvancedCondensingRecipe implements Recipe<AdvancedCondensingRecipeInput> {

    @Getter
    protected SizedFluidIngredient base;
    protected List<Ingredient> ingredients;
    @Getter
    protected Optional<Ingredient> container;
    @Getter
    protected Optional<Ingredient> modifier;

    @Getter
    protected int time;
    @Getter
    public final ItemStack output;

    public AdvancedCondensingRecipe(SizedFluidIngredient base, List<Ingredient> ingredients, Optional<Ingredient> container, Optional<Ingredient> modifier, ItemStack output, int time) {
        this.base = base;
        this.ingredients = ingredients;
        this.container = container;
        this.modifier = modifier;
        this.output = output;
        this.time = time;
    }

    public static final MapCodec<AdvancedCondensingRecipe> CODEC = RecordCodecBuilder.mapCodec((obj) -> obj.group(
            SizedFluidIngredient.FLAT_CODEC.fieldOf("base").forGetter((recipe) -> recipe.base),
            Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").forGetter((recipe) -> recipe.ingredients),
            Ingredient.CODEC_NONEMPTY.optionalFieldOf("container").forGetter((recipe) -> recipe.container),
            Ingredient.CODEC_NONEMPTY.optionalFieldOf("modifier").forGetter((recipe) -> recipe.modifier),
            ItemStack.CODEC.fieldOf("output").forGetter((recipe) -> recipe.output),
            ExtraCodecs.POSITIVE_INT.fieldOf("time").forGetter((recipe) -> recipe.time)
    ).apply(obj, AdvancedCondensingRecipe::new));

    @Override
    public boolean matches(AdvancedCondensingRecipeInput input, Level level) {
        FluidStack in = input.getFluid();
        boolean items = false;
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).test(input.getItem(i))) {
                items = true;
            } else {
                items = false;
                break;
            }
        }
        boolean c = container.isEmpty();
        if (container.isPresent()) {
            c = container.get().test(input.container());
        }
        boolean m = modifier.isEmpty();
        if (modifier.isPresent()) {
            m = modifier.get().test(input.modifier());
        }
        return items && base.test(in) && c && m;
    }

    @Override
    public ItemStack assemble(AdvancedCondensingRecipeInput input, HolderLookup.Provider provider) {
        return getResultItem(provider).copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.copyOf(ingredients);
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AgresticRecipeTypes.Serializers.ADVANCED_CONDENSING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AgresticRecipeTypes.ADVANCED_CONDENSING.get();
    }
}
