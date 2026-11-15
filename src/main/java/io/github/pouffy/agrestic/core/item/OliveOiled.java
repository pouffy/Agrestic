package io.github.pouffy.agrestic.core.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public record OliveOiled() implements TooltipProvider {
    public static final Codec<OliveOiled> CODEC = Codec.unit(new OliveOiled());
    public static final StreamCodec<ByteBuf, OliveOiled> STREAM_CODEC = StreamCodec.unit(new OliveOiled());

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        consumer.accept(Component.translatable("ui.agrestic.tooltip.olive_oiled").withStyle(Style.EMPTY.withColor(0xff7c7c0d)));
    }
}
