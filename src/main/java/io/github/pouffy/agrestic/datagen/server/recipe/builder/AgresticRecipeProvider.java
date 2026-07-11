package io.github.pouffy.agrestic.datagen.server.recipe.builder;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

public abstract class AgresticRecipeProvider extends RecipeProvider {
    public AgresticRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected abstract String type();

    @Override
    public String getName() {
        return "%s Recipes".formatted(this.type());
    }
}
