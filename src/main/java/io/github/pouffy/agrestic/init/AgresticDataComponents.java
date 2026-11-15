package io.github.pouffy.agrestic.init;

import com.mojang.serialization.Codec;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.item.OliveOiled;
import io.github.pouffy.agrestic.core.item.StoredFluidStack;
import io.github.pouffy.agrestic.core.item.StoredItemStack;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AgresticDataComponents {
    public static final DeferredRegister<DataComponentType<?>> HELPER = Agrestic.getRegistryHelper().createComponents();

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<StoredItemStack>> ITEM_STACK = HELPER.register("item_stack", () -> DataComponentType.<StoredItemStack>builder().persistent(StoredItemStack.CODEC).networkSynchronized(StoredItemStack.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<StoredFluidStack>> FLUID_STACK = HELPER.register("fluid_stack", () -> DataComponentType.<StoredFluidStack>builder().persistent(StoredFluidStack.CODEC).networkSynchronized(StoredFluidStack.STREAM_CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> QUALITY = HELPER.register("quality", () -> DataComponentType.<Float>builder().persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).build());
    public static final Supplier<DataComponentType<OliveOiled>> OLIVE_OILED = HELPER.register("olive_oiled", () -> DataComponentType.<OliveOiled>builder().persistent(OliveOiled.CODEC).networkSynchronized(OliveOiled.STREAM_CODEC).build());

    public static void staticInit() {}
}
