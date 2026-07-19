package io.github.pouffy.agrestic.client.ui;

import io.github.pouffy.agrestic.client.AgresticSprites;
import io.github.pouffy.agrestic.common.block.entity.BrewingBarrelBlockEntity;
import lombok.Getter;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class BrewingBarrelMenu extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData data;
    @Getter
    public final BrewingBarrelBlockEntity blockEntity;

    public BrewingBarrelMenu(int syncId, Inventory playerInventory, BrewingBarrelBlockEntity blockEntity, ContainerData data) {
        super(menuType, syncId);
        this.inventory = blockEntity;
        this.blockEntity = blockEntity;
        this.data = data;

        this.addSlot(new BucketSlot(inventory, 0, 62, 7));
        this.addSlot(new BottleSlot(inventory, 1, 116, 7));
        this.addSlot(new BoozeBottleSlot(inventory, 2, 26, 15));

        this.addSlot(new OutputSlot(inventory, 3, 62, 63));
        this.addSlot(new OutputSlot(inventory, 4, 116, 63));
        this.addSlot(new OutputSlot(inventory, 5, 26, 55));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        this.addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            if (index < inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, inventory.getContainerSize(), slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (BucketSlot.matches(originalStack)) {
                    if (!this.moveItemStackTo(originalStack, 0, 1, false))
                        return ItemStack.EMPTY;
                }
                else if (BottleSlot.matches(originalStack)) {
                    if (!this.moveItemStackTo(originalStack, 1, 2, false))
                        return ItemStack.EMPTY;
                }
                else if (BoozeBottleSlot.matches(originalStack)) {
                    if (!this.moveItemStackTo(originalStack, 2, 3, false))
                        return ItemStack.EMPTY;
                }
                else if (!this.moveItemStackTo(originalStack, 0, 3, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return inventory.stillValid(player);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public int getBrewingTime() {
        return this.data.get(0);
    }

    public int getScaledArrowProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowProgressWidth = AgresticSprites.ARROW.width;
        return (maxProgress == 0 || progress == 0) ? 0 : progress * arrowProgressWidth / maxProgress;
    }

    public int getScaledAuxArrowProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowProgressWidth = AgresticSprites.TINY_ARROW.width;
        return (maxProgress == 0 || progress == 0) ? 0 : progress * arrowProgressWidth / maxProgress;
    }
}
