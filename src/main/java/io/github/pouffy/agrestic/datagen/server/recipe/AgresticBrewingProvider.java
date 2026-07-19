package io.github.pouffy.agrestic.datagen.server.recipe;

import com.pouffydev.krystal_core.KrystalCore;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.AgresticRecipeProvider;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.BrewingRecipeBuilder;
import io.github.pouffy.agrestic.init.AgresticFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.concurrent.CompletableFuture;

public class AgresticBrewingProvider extends AgresticRecipeProvider {
    public AgresticBrewingProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected String type() {
        return "Brewing Barrel";
    }

    protected void buildRecipes(RecipeOutput output) {
        brew(AgresticFluids.ALE.get(), AgresticFluids.ALE_WORT.get(), output, "ale");
        brew(AgresticFluids.CIDER.get(), AgresticFluids.APPLE_JUICE.get(), output, "cider");
        brew(AgresticFluids.IRON_WINE.get(), AgresticFluids.IRONBERRY_JUICE.get(), output, "iron_wine");
        brew(AgresticFluids.MEAD.get(), KrystalCore.HONEY.get(), output, "mead");
        brew(AgresticFluids.SWEET_BERRY_WINE.get(), AgresticFluids.SWEET_BERRY_JUICE.get(), output, "sweet_berry_wine");
        brew(AgresticFluids.WINE.get(), AgresticFluids.GRAPE_JUICE.get(), output, "wine");
        brew(AgresticFluids.AMBROSIA.get(), AgresticFluids.GOLDEN_APPLE_JUICE.get(), output, "ambrosia");
    }

    public void brew(Fluid output, Fluid input, RecipeOutput recipeOutput, String path) {
        brew(output, input, 12000, recipeOutput, path);
    }

    public void brew(Fluid output, Fluid input, int time, RecipeOutput recipeOutput, String path) {
        new BrewingRecipeBuilder(new FluidStack(input, 1), new FluidStack(output, 1)).time(time).save(recipeOutput, Agrestic.location("brewing_barrel/").withSuffix(path));
    }
}
