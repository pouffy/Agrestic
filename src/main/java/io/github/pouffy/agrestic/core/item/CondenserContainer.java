package io.github.pouffy.agrestic.core.item;

import io.github.pouffy.agrestic.common.block.entity.CondenserBlockEntity;
import net.minecraft.world.item.ItemStack;

public class CondenserContainer extends AgresticItemContainer {

    final CondenserBlockEntity blockEntity;

    public CondenserContainer(CondenserBlockEntity blockEntity) {
        super(5, blockEntity::onItemsChanged);
        this.blockEntity = blockEntity;
    }

    public boolean canInsert(int slot, ItemStack stack) {
        if (!blockEntity.hasRetorts()) return false;
        if (slot == 4) return false;
        return super.canInsert(slot, stack);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!blockEntity.hasRetorts()) return stack;
        if (slot == 4) return stack;
        return super.insertItem(slot, stack, simulate);
    }
}
