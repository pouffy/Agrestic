package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.item.StoredFluidStack;
import io.github.pouffy.agrestic.core.item.StoredItemStack;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AgresticDataComponents {
    public static final DeferredRegister<DataComponentType<?>> HELPER = Agrestic.getRegistryHelper().createComponents();

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<StoredItemStack>> ITEM_STACK = HELPER.register("item_stack", () -> DataComponentType.<StoredItemStack>builder().persistent(StoredItemStack.CODEC).networkSynchronized(StoredItemStack.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<StoredFluidStack>> FLUID_STACK = HELPER.register("fluid_stack", () -> DataComponentType.<StoredFluidStack>builder().persistent(StoredFluidStack.CODEC).networkSynchronized(StoredFluidStack.STREAM_CODEC).build());

    public static void staticInit() {}
}
