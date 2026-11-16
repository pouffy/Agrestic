package io.github.pouffy.agrestic.common.block.entity;

import io.github.pouffy.agrestic.core.block.AgresticContainerBlockEntity;
import io.github.pouffy.agrestic.core.fluid.AgresticFluidTank;
import io.github.pouffy.agrestic.core.item.AgresticItemContainer;
import io.github.pouffy.agrestic.init.AgresticBlockEntities;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.fluid.FluidTransferHelper;

public class CondenserBlockEntity extends AgresticContainerBlockEntity {

    @Getter
    protected final AgresticFluidTank tank;

    protected boolean updateCapability;
    protected IFluidHandler fluidCapability;
    protected IItemHandlerModifiable itemCapability;

    protected BlockPos controller;

    public CondenserBlockEntity(BlockPos pos, BlockState blockState) {
        super(AgresticBlockEntities.BASIC_CONDENSER.get(), pos, blockState);
        this.tank = new AgresticFluidTank(8000, this::onTankChanged);
        updateCapability = false;
        refreshCapability();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AgresticBlockEntities.BASIC_CONDENSER.get(), (be, context) -> be.getNewFluidCapability());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AgresticBlockEntities.BASIC_CONDENSER.get(), (be, context) -> be.getNewItemCapability());
    }

    private void refreshCapability() {
        fluidCapability = getNewFluidCapability();
        itemCapability = getNewItemCapability();
        invalidateCapabilities();
    }

    private IItemHandlerModifiable getNewItemCapability() {
        return isController() ? container : getControllerBE() != null ? getControllerBE().getNewItemCapability() : itemCapability;
    }

    private IFluidHandler getNewFluidCapability() {
        return isController() ? tank : getControllerBE() != null ? getControllerBE().getNewFluidCapability() : fluidCapability;
    }

    protected void onTankChanged(FluidStack newFluids) {
        if (tank != null) {
            tank.setCapacity(8000);
            if (tank.getSpace() < 0) tank.drain(-tank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }
        markUpdated();
    }

    public InteractionResult interactEmpty(Player player) {
        return InteractionResult.PASS;
    }

    public ItemInteractionResult interact(Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (FluidTransferHelper.interactWithTank(level, worldPosition, player, hand, hitResult))
            return ItemInteractionResult.SUCCESS;
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public AgresticItemContainer newContainer() {
        return new AgresticItemContainer(5, this::onItemsChanged);
    }

    @Override
    public ContainerData createContainerData() {
        return new ContainerData() {

            @Override
            public int get(int i) {
                return 0;
            }

            @Override
            public void set(int i, int i1) {

            }

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    public boolean isController() {
        return controller == null || worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY() && worldPosition.getZ() == controller.getZ();
    }

    @Nullable
    public CondenserBlockEntity getControllerBE() {
        if (level == null || isController()) return this;
        return level.getBlockEntity(controller) instanceof CondenserBlockEntity blockEntity ? blockEntity : null;
    }

    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }

    public void setController(BlockPos controller) {
        if (level == null || level.isClientSide)
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;
        refreshCapability();
        setChanged();
        if (level instanceof ServerLevel serverLevel)
            serverLevel.getChunkSource().blockChanged(getBlockPos());
    }

    @Override
    public String getContainerName() {
        return "basic_condenser";
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        controller = null;
        if (NbtUtils.readBlockPos(tag, "Controller").isPresent())
            controller = NbtUtils.readBlockPos(tag, "Controller").get();

        if (isController()) {
            this.tank.readFromNBT(registries, tag.getCompound("Tank"));
        }
        updateCapability = true;
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (isController()) {
            tag.put("Tank", this.tank.writeToNBT(registries, new CompoundTag()));
        } else {
            tag.put("Controller", NbtUtils.writeBlockPos(controller));
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }

    @Override
    public void serverTick() {
        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
    }

    @Override
    public void clientTick() {

    }
}
