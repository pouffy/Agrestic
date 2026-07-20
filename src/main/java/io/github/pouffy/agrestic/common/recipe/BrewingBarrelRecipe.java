package io.github.pouffy.agrestic.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.agrestic.AgresticConfig;
import io.github.pouffy.agrestic.core.fluid.FluidHelper;
import io.github.pouffy.agrestic.core.recipe.FluidRecipe;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import lombok.Getter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class BrewingBarrelRecipe extends FluidRecipe<BrewingRecipeInput> {
    public static final float DEFAULT_QUALITY = 0f;
    @Getter
    protected FluidStack input;
    @Getter
    protected FluidStack output;
    @Getter
    protected int time;

    public static final MapCodec<BrewingBarrelRecipe> CODEC = RecordCodecBuilder.mapCodec((obj) -> obj.group(
            FluidHelper.CODEC_NO_SIZE.fieldOf("input").forGetter((recipe) -> recipe.input),
            FluidHelper.CODEC_NO_SIZE.fieldOf("output").forGetter((recipe) -> recipe.output),
            Codec.INT.fieldOf("time").forGetter((recipe) -> recipe.time)
    ).apply(obj, BrewingBarrelRecipe::new));

    public BrewingBarrelRecipe(FluidStack input, FluidStack output, int time) {
        super(AgresticRecipeTypes.Serializers.BREWING_BARREL.get(), AgresticRecipeTypes.BREWING_BARREL.get(), output);
        this.input = input;
        this.output = output;
        this.time = time;
    }

    public BrewingBarrelRecipe(FluidStack input, FluidStack output) {
        this(input, output, 12000);
    }

    @Override
    public boolean matches(BrewingRecipeInput input, Level level) {
        return matches(input);
    }

    private boolean matches(BrewingRecipeInput input) {
        FluidStack in = input.getFluid();
        FluidStack aux = input.getAuxiliary();
        if (in == null || in.isEmpty() || in.getAmount() <= 0) {
            return false;
        }

        boolean inputMatches = FluidStack.isSameFluidSameComponents(this.input, in);

        if (aux != null && !aux.isEmpty() && aux.getAmount() > 0) {
            boolean auxMatches = FluidStack.isSameFluidSameComponents(this.output, aux);
            return inputMatches && auxMatches;
        }

        return inputMatches;
    }

    public FluidStack finish(BrewingRecipeInput input, RandomSource random) {
        FluidStack in = input.getFluid();
        FluidStack aux = input.getAuxiliary();
        if (aux == null || aux.isEmpty() || aux.getAmount() <= 0 || !matches(input)) {
            if (matches(new BrewingRecipeInput(in, FluidStack.EMPTY)) && this.output != null) {
                return withQuality(this.output, getBaseQuality(random));
            }
        }
        if (matches(input) && output != null) {
            if (hasQuality(aux)) {
                float auxQuality = getQuality(aux);
                int minChange = getMinBrewQualityChange();
                int maxChange = getMaxBrewQualityChange();
                if (maxChange < minChange) maxChange = minChange;

                int brewQualityChange = random.nextInt((maxChange - minChange) + 1) + minChange;
                float quality = Math.clamp((brewQualityChange + (int) (100 * auxQuality)) / 100F, 0, 1);

                return withQuality(this.output, quality);
            } else {
                return withQuality(this.output, getBaseQuality(random));
            }
        }
        return FluidStack.EMPTY;
    }

    public static int getMinBrewQualityChange() {
        return AgresticConfig.SERVER.minBrewQualityChange.get();
    }

    public static int getMaxBrewQualityChange() {
        return AgresticConfig.SERVER.maxBrewQualityChange.get();
    }

    private float getBaseQuality(RandomSource random) {
        return (5 + random.nextInt(71)) / 100F;
    }

    public static float getQuality(FluidStack stack) {
        if (stack == null || stack.isEmpty()) return DEFAULT_QUALITY;
        return stack.getOrDefault(AgresticDataComponents.QUALITY, DEFAULT_QUALITY);
    }

    public static FluidStack withQuality(FluidStack stack, float quality) {
        DataComponentPatch patch = DataComponentPatch.builder().set(AgresticDataComponents.QUALITY.get(), Math.clamp(quality, 0f, 1f)).build();
        FluidStack applied = stack.copy();
        applied.applyComponents(patch);
        return applied;
    }

    public static boolean hasQuality(FluidStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        return stack.has(AgresticDataComponents.QUALITY);
    }
}
