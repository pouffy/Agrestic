package io.github.pouffy.agrestic.client.ui.slot;

import io.github.pouffy.agrestic.core.fluid.TransferDirectionSupplier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import slimeknights.mantle.fluid.FluidTransferHelper;
import slimeknights.mantle.fluid.transfer.IFluidContainerTransfer;

public class FluidTransferResultSlot extends Slot {
    private final IFluidHandler tank;
    private final TransferDirectionSupplier directionSupplier;
    private final Player player;

    private final int inputIndex;

    public FluidTransferResultSlot(Container pContainer, int index, int inputIndex, int pX, int pY, IFluidHandler tank, TransferDirectionSupplier directionSupplier, Player player) {
        super(pContainer, index, pX, pY);
        this.tank = tank;
        this.directionSupplier = directionSupplier;
        this.player = player;
        this.inputIndex = inputIndex;
    }

    @Override
    public void set(ItemStack stack) {
        if (stack.isEmpty()) {
            ItemStack bucket = container.getItem(inputIndex);
            if (!bucket.isEmpty()) {
                IFluidContainerTransfer.TransferResult result = FluidTransferHelper.interactWithStack(tank, bucket, directionSupplier.getTransferDirection());
                if (result != null) {
                    FluidTransferHelper.playUISound(player, result.getSound());
                    container.setItem(inputIndex, bucket);
                    stack = result.stack();
                }
            }
        }
        super.set(stack);
    }
}
