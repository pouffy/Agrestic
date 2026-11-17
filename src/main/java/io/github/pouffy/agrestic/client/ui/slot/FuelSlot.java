package io.github.pouffy.agrestic.client.ui.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.function.Function;

public class FuelSlot extends SlotItemHandler {

    private final Function<ItemStack, Boolean> testFuel;

    public FuelSlot(IItemHandler container, int slot, int x, int y, Function<ItemStack, Boolean> testFuel) {
        super(container, slot, x, y);
        this.testFuel = testFuel;
    }

    public boolean mayPlace(ItemStack stack) {
        return this.testFuel.apply(stack) && super.mayPlace(stack);
    }
}
