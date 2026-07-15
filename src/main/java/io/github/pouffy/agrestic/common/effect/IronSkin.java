package io.github.pouffy.agrestic.common.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record IronSkin(boolean render) {
    public static final Codec<IronSkin> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.optionalFieldOf("render", true).forGetter(IronSkin::render)
    ).apply(instance, IronSkin::new));

    public static final StreamCodec<FriendlyByteBuf, IronSkin> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            IronSkin::render,
            IronSkin::new
    );
}
