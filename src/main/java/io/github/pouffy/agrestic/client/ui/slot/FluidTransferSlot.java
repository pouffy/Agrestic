package io.github.pouffy.agrestic.client.ui.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import slimeknights.mantle.fluid.transfer.FluidContainerTransferManager;

public class FluidTransferSlot extends Slot {
    public FluidTransferSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return FluidContainerTransferManager.INSTANCE.mayHaveTransfer(stack) || stack.getCapability(Capabilities.FluidHandler.ITEM) != null;
    }
}
