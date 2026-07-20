package io.github.pouffy.agrestic.core.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.function.Consumer;

public class AgresticItemContainer extends ItemStackHandler {
    private final Consumer<ItemStack> updateCallback;
    private boolean forbidInsertion = false;
    private boolean forbidExtraction = false;

    public AgresticItemContainer() {
        this(1, (stack) -> {});
    }

    public AgresticItemContainer(int size, Consumer<ItemStack> updateCallback) {
        super(size);
        this.updateCallback = updateCallback;
    }

    public AgresticItemContainer(NonNullList<ItemStack> stacks, Consumer<ItemStack> updateCallback) {
        super(stacks);
        this.updateCallback = updateCallback;
    }

    public AgresticItemContainer forbidInsertion() {
        this.forbidInsertion = true;
        return this;
    }

    public AgresticItemContainer forbidExtraction() {
        this.forbidExtraction = true;
        return this;
    }

    public boolean canInsert() {
        return !this.forbidInsertion;
    }

    public boolean canExtract() {
        return !this.forbidExtraction;
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        this.validateSlotIndex(slot);
        this.stacks.set(slot, stack);
        this.onContentsChanged(slot);
    }

    public boolean canInsert(int slot, ItemStack stack) {
        if (forbidInsertion) return false;
        if (stack.isEmpty() || !this.isItemValid(slot, stack)) return false;
        this.validateSlotIndex(slot);
        ItemStack existing = this.stacks.get(slot);
        int limit = this.getStackLimit(slot, stack);
        if (limit <= 0) return false;
        if (!existing.isEmpty()) {
            return ItemStack.isSameItemSameComponents(stack, existing) && existing.getCount() + stack.getCount() <= limit;
        } else {
            return stack.getCount() <= limit;
        }
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (forbidInsertion)
            return stack;
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (!this.isItemValid(slot, stack)) {
            return stack;
        } else {
            this.validateSlotIndex(slot);
            ItemStack existing = this.stacks.get(slot);
            int limit = this.getStackLimit(slot, stack);
            if (!existing.isEmpty()) {
                if (!ItemStack.isSameItemSameComponents(stack, existing)) {
                    return stack;
                }

                limit -= existing.getCount();
            }

            if (limit <= 0) {
                return stack;
            } else {
                boolean reachedLimit = stack.getCount() > limit;
                if (!simulate) {
                    if (existing.isEmpty()) {
                        this.stacks.set(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
                    } else {
                        existing.grow(reachedLimit ? limit : stack.getCount());
                    }

                    this.onContentsChanged(slot);
                }

                return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
            }
        }
    }

    public boolean canExtract(int slot, int amount) {
        if (forbidExtraction) return false;
        if (amount == 0) return false;
        this.validateSlotIndex(slot);
        ItemStack existing = this.stacks.get(slot);
        return !existing.isEmpty();
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (forbidExtraction)
            return ItemStack.EMPTY;
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else {
            this.validateSlotIndex(slot);
            ItemStack existing = this.stacks.get(slot);
            if (existing.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                int toExtract = Math.min(amount, existing.getMaxStackSize());
                if (existing.getCount() <= toExtract) {
                    if (!simulate) {
                        this.stacks.set(slot, ItemStack.EMPTY);
                        this.onContentsChanged(slot);
                        return existing;
                    } else {
                        return existing.copy();
                    }
                } else {
                    if (!simulate) {
                        this.stacks.set(slot, existing.copyWithCount(existing.getCount() - toExtract));
                        this.onContentsChanged(slot);
                    }

                    return existing.copyWithCount(toExtract);
                }
            }
        }
    }

    protected void onContentsChanged(int slot) {
        updateCallback.accept(getStackInSlot(slot));
    }

    public void clear() {
        for (int i = 0; i < this.getSlots(); i++) {
            this.stacks.set(i, ItemStack.EMPTY);
        }
    }

    public boolean isEmpty() {
        for (int i = 0; i < this.getSlots(); i++) {
            if (!this.stacks.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
