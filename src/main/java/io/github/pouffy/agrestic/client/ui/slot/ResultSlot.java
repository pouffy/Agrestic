package io.github.pouffy.agrestic.client.ui.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ResultSlot extends Slot {
    private int amount;

    public ResultSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
