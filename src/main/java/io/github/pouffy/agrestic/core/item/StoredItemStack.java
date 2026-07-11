package io.github.pouffy.agrestic.core.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public record StoredItemStack(ItemStack stack) implements TooltipProvider {
    public static final Codec<StoredItemStack> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            ItemStack.OPTIONAL_CODEC.fieldOf("stack").forGetter(StoredItemStack::stack)
    ).apply(inst, StoredItemStack::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StoredItemStack> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC,
            StoredItemStack::stack,
            StoredItemStack::new
    );

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        if (!stack.isEmpty()) {
            MutableComponent stackName = Component.empty().append(stack.getHoverName());
            if (tooltipFlag.isAdvanced()) {
                stackName = stackName.append(Component.literal("[%s]".formatted(BuiltInRegistries.ITEM.getKey(stack.getItem()))).withStyle(ChatFormatting.GRAY));
            }
            consumer.accept(Component.translatable("ui.agrestic.tooltip.item", stackName, Component.literal(String.valueOf(stack.getCount()))));
        }
    }
}
