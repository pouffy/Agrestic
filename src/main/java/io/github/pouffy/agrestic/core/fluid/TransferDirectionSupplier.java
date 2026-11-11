package io.github.pouffy.agrestic.core.fluid;

import slimeknights.mantle.fluid.transfer.IFluidContainerTransfer;

public interface TransferDirectionSupplier {
    IFluidContainerTransfer.TransferDirection getTransferDirection();
}
