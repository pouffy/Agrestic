package io.github.pouffy.agrestic.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public enum BasicCondensingRecipeBookTab implements StringRepresentable {
    ELIXIRS("elixirs"),
    MISC("misc");

    public static final Codec<BasicCondensingRecipeBookTab> CODEC = Codec.STRING.flatXmap(s -> {
        BasicCondensingRecipeBookTab tab = findByName(s);
        if (tab == null) {
            return DataResult.error(() -> "Optional field 'recipe_book_tab' does not match any valid tab. If defined, must be one of the following: " + EnumSet.allOf(BasicCondensingRecipeBookTab.class));
        }
        return DataResult.success(tab);
    }, tab -> DataResult.success(tab.toString()));

    public final String name;

    BasicCondensingRecipeBookTab(String name) {
        this.name = name;
    }

    public static BasicCondensingRecipeBookTab findByName(String name) {
        for (BasicCondensingRecipeBookTab value : values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
