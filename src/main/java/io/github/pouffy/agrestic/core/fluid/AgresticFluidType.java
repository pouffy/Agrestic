package io.github.pouffy.agrestic.core.fluid;

import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

public class AgresticFluidType extends FluidType {

    public final String name;
    public boolean booze = false;

    public AgresticFluidType(String name) {
        super(FluidType.Properties.create()
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
        );
        this.name = name;
    }

    public AgresticFluidType booze() {
        this.booze = true;
        return this;
    }

    public IClientFluidTypeExtensions getExtension() {
        if (booze) {
            return new IClientFluidTypeExtensions() {
                @Override
                public @NotNull ResourceLocation getStillTexture() {
                    return Agrestic.location("block/fluid/booze/%s_still".formatted(name));
                }

                @Override
                public @NotNull ResourceLocation getFlowingTexture() {
                    return Agrestic.location("block/fluid/booze/%s_flowing".formatted(name));
                }
            };
        } else {
            return new IClientFluidTypeExtensions() {
                @Override
                public @NotNull ResourceLocation getStillTexture() {
                    return Agrestic.location("block/fluid/%s_still".formatted(name));
                }

                @Override
                public @NotNull ResourceLocation getFlowingTexture() {
                    return Agrestic.location("block/fluid/%s_flowing".formatted(name));
                }

                @Override
                public @NotNull ResourceLocation getOverlayTexture() {
                    return Agrestic.location("block/fluid/%s_overlay".formatted(name));
                }
                @Override
                public ResourceLocation getRenderOverlayTexture(@NotNull Minecraft mc) {
                    return Agrestic.location("textures/misc/%s_overlay".formatted(name));
                }
            };
        }
    }
}
