package io.github.pouffy.agrestic.core.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.function.Consumer;

public record StoredFluidStack(FluidStack stack, int capacity) implements TooltipProvider {
    public static final Codec<StoredFluidStack> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            FluidStack.OPTIONAL_CODEC.fieldOf("stack").forGetter(StoredFluidStack::stack),
            Codec.INT.fieldOf("capacity").forGetter(StoredFluidStack::capacity)
    ).apply(inst, StoredFluidStack::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StoredFluidStack> STREAM_CODEC = StreamCodec.composite(
            FluidStack.OPTIONAL_STREAM_CODEC,
            StoredFluidStack::stack,
            ByteBufCodecs.INT,
            StoredFluidStack::capacity,
            StoredFluidStack::new
    );

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        if (!stack.isEmpty()) {
            MutableComponent stackName = Component.empty().append(stack.getHoverName());
            if (tooltipFlag.isAdvanced()) {
                stackName = stackName.append(Component.literal("[%s]".formatted(BuiltInRegistries.FLUID.getKey(stack.getFluid()))).withStyle(ChatFormatting.GRAY));
            }
            consumer.accept(Component.translatable("ui.agrestic.tooltip.fluid", stackName, stack.getAmount(), capacity));
        }
    }
}
