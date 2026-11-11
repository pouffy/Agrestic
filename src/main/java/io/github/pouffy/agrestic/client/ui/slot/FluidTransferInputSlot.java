package io.github.pouffy.agrestic.client.ui.slot;

import io.github.pouffy.agrestic.core.fluid.*;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import slimeknights.mantle.fluid.FluidTransferHelper;
import slimeknights.mantle.fluid.transfer.IFluidContainerTransfer;

public class FluidTransferInputSlot extends Slot {
    private final IFluidHandler tank;
    private final TransferDirectionSupplier directionSupplier;
    private final Player player;

    private final int resultIndex;

    public FluidTransferInputSlot(Container pContainer, int index, int resultIndex, int pX, int pY, IFluidHandler tank, TransferDirectionSupplier directionSupplier, Player player) {
        super(pContainer, index, pX, pY);
        this.tank = tank;
        this.directionSupplier = directionSupplier;
        this.player = player;
        this.resultIndex = resultIndex;
    }

    @Override
    public void set(ItemStack stack) {
        if (!stack.isEmpty() && container.getItem(resultIndex).isEmpty()) {
            IFluidContainerTransfer.TransferResult result = FluidTransferHelper.interactWithStack(tank, stack, directionSupplier.getTransferDirection());
            if (result != null) {
                FluidTransferHelper.playUISound(player, result.getSound());
                container.setItem(resultIndex, result.stack());
            }
        }
        super.set(stack);
    }
}
