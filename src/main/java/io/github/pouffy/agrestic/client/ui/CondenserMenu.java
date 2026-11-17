package io.github.pouffy.agrestic.client.ui;

import io.github.pouffy.agrestic.client.AgresticSprites;
import io.github.pouffy.agrestic.client.ui.slot.CondenserResultSlot;
import io.github.pouffy.agrestic.client.ui.slot.FuelSlot;
import io.github.pouffy.agrestic.common.block.entity.CondenserBlockEntity;
import io.github.pouffy.agrestic.init.AgresticMenuTypes;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.Objects;

public class CondenserMenu extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData data;
    @Getter
    public final CondenserBlockEntity blockEntity;

    @Getter
    private final Container container;

    public CondenserMenu(int syncId, Inventory playerInventory, final FriendlyByteBuf data) {
        this(syncId, playerInventory, getBlockEntity(playerInventory, data), new SimpleContainerData(4));
    }

    public CondenserMenu(int syncId, Inventory playerInventory, CondenserBlockEntity blockEntity, ContainerData data) {
        super(AgresticMenuTypes.BASIC_CONDENSER.get(), syncId);
        this.inventory = blockEntity;
        this.blockEntity = blockEntity;
        this.data = data;

        container = new SimpleContainer(5);

        this.addSlot(new SlotItemHandler(blockEntity.getContainer(), 0, 27, 23));
        this.addSlot(new SlotItemHandler(blockEntity.getContainer(), 1, 27, 47));
        this.addSlot(new FuelSlot(blockEntity.getContainer(), 2, 66, 62, this::isFuel));
        this.addSlot(new SlotItemHandler(blockEntity.getContainer(), 3, 105, 7));
        this.addSlot(new CondenserResultSlot(blockEntity.getContainer(), 4, 105, 35, playerInventory.player));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        this.addDataSlots(data);
    }

    private static CondenserBlockEntity getBlockEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final BlockEntity blockEntity = playerInventory.player.level().getBlockEntity(data.readBlockPos());
        if (blockEntity instanceof CondenserBlockEntity condenser) {
            return condenser;
        }
        throw new IllegalStateException("Block entity is not correct! " + blockEntity);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();

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

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            this.clearContainer(player, container);
        }
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 85 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 143));
        }
    }

    public int getCondensingTime() {
        return this.data.get(0);
    }

    protected boolean isFuel(ItemStack stack) {
        return stack.getBurnTime(AgresticRecipeTypes.CONDENSING.get()) > 0;
    }

    public boolean isLit() {
        return this.data.get(2) > 0;
    }

    public float getLitProgress() {
        int i = this.data.get(3);
        if (i == 0) {
            i = 200;
        }

        return Mth.clamp((float)this.data.get(2) / (float)i, 0.0F, 1.0F);
    }

    public int getScaledArrowProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowProgressWidth = AgresticSprites.ALCHEMY_ARROW.width;
        return (maxProgress == 0 || progress == 0) ? 0 : progress * arrowProgressWidth / maxProgress;
    }
}
