package io.github.pouffy.agrestic.compat.jade.provider;

import io.github.pouffy.agrestic.common.block.entity.BrewingBarrelBlockEntity;
import io.github.pouffy.agrestic.compat.jade.AgresticJadePlugin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum BrewingBarrelProgressProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, BrewingBarrelProgressProvider.Data> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig config) {
        Data data = this.decodeFromData(blockAccessor).orElse(null);
        if (data != null) {
            IElementHelper helper = IElementHelper.get();
            tooltip.append(helper.progress((float)data.progress / (float)data.total).translate(new Vec2(-2.0F, 0.0F)));
        }
    }

    public Data streamData(BlockAccessor accessor) {
        BrewingBarrelBlockEntity brewingBarrel = (BrewingBarrelBlockEntity)accessor.getBlockEntity();
        return new Data(brewingBarrel.getProgress(), brewingBarrel.getProgressTotal());
    }

    public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
        return Data.STREAM_CODEC;
    }

    public ResourceLocation getUid() {
        return AgresticJadePlugin.brewingBarrel;
    }

    public record Data(int progress, int total) {
        public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC;

        static {
            STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Data::progress, ByteBufCodecs.VAR_INT, Data::total, Data::new);
        }
    }
}
