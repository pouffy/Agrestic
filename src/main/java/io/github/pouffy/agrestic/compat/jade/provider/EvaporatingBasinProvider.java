package io.github.pouffy.agrestic.compat.jade.provider;

import io.github.pouffy.agrestic.common.block.entity.EvaporatingBasinBlockEntity;
import io.github.pouffy.agrestic.compat.jade.AgresticJadePlugin;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.StreamServerDataProvider;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum EvaporatingBasinProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, EvaporatingBasinProvider.Data> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig config) {
        Data data = this.decodeFromData(blockAccessor).orElse(null);
        if (data != null) {
            IElementHelper helper = IElementHelper.get();
            tooltip.append(helper.progress(data.progress / data.total).translate(new Vec2(-2.0F, 0.0F)));
        }
    }

    public Data streamData(BlockAccessor accessor) {
        EvaporatingBasinBlockEntity evaporatingBasin = (EvaporatingBasinBlockEntity)accessor.getBlockEntity();
        return new Data(evaporatingBasin.getProgress(), evaporatingBasin.getProgressTotal());
    }

    public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
        return Data.STREAM_CODEC;
    }

    public ResourceLocation getUid() {
        return AgresticJadePlugin.evaporatingBasin;
    }

    public record Data(float progress, float total) {
        public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC;

        static {
            STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, Data::progress, ByteBufCodecs.FLOAT, Data::total, Data::new);
        }
    }
}
