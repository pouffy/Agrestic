package io.github.pouffy.agrestic.common.block.entity;

import io.github.pouffy.agrestic.common.recipe.EvaporatingBasinRecipe;
import io.github.pouffy.agrestic.core.block.ILightEmitting;
import io.github.pouffy.agrestic.core.fluid.AgresticFluidTank;
import io.github.pouffy.agrestic.core.fluid.transfer.FluidTransferHelper;
import io.github.pouffy.agrestic.core.item.DisplayedItemContainer;
import io.github.pouffy.agrestic.core.recipe.FluidRecipeInput;
import io.github.pouffy.agrestic.core.recipe.RecipeSearch;
import io.github.pouffy.agrestic.init.AgresticBlockEntities;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class EvaporatingBasinBlockEntity extends BlockEntity {
    @Getter
    protected final AgresticFluidTank tank;
    @Getter
    protected final DisplayedItemContainer container;
    private FluidStack evaporatedFluid;
    private int age = 0;
    private int remainder = 0;

    public EvaporatingBasinBlockEntity(BlockPos pos, BlockState blockState) {
        super(AgresticBlockEntities.EVAPORATING_BASIN.get(), pos, blockState);
        tank = new AgresticFluidTank(getCapacity(), this::onFluidStackChanged);
        container = new DisplayedItemContainer(1, (stack) -> this.markUpdated()).forbidInsertion();
    }

    protected void onFluidStackChanged(FluidStack newFluids) {
        if (level == null) return;
        if (tank != null) {
            tank.setCapacity(getCapacity());
            if (tank.getSpace() < 0) tank.drain(-tank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }
        updateLight(this, tank);
        markUpdated();
    }

    public int getCapacity() {
        return 6000;
    }

    public void serverTick() {
        if (level == null) return;
        age++;
        if (age >= 1000000) {
            age = 0;
        }
        if (this.age % 20 == 0) {
            if (this.getFluidStack().isEmpty()  || (this.evaporatedFluid != null && this.evaporatedFluid.getFluid() != this.getFluidStack().getFluid())) {
                // change of recipe - reset
                this.evaporatedFluid = null;
                this.remainder = 0;
            }
            int to_drain = 0;
            EvaporatingBasinRecipe recipe;
            if (!this.getFluidStack().isEmpty()) {
                var input = new FluidRecipeInput(this.getFluidStack());
                recipe = RecipeSearch.search(level, AgresticRecipeTypes.EVAPORATING_BASIN.get()).findRecipe(input);
                if (recipe != null) {
                    int temp = 20 * recipe.getInput().amount() + this.remainder;
                    to_drain = temp / recipe.getTime();
                    this.remainder = temp % recipe.getTime();
                }
            }

            FluidStack drained = tank.drain(to_drain, IFluidHandler.FluidAction.EXECUTE);

            if (!drained.isEmpty()) {
                if (evaporatedFluid == null) {
                    evaporatedFluid = drained;
                } else {
                    if (!evaporatedFluid.getFluid().equals(drained.getFluid())) {
                        evaporatedFluid = drained;
                    } else {
                        evaporatedFluid.setAmount(evaporatedFluid.getAmount() + drained.getAmount());
                    }
                }
                level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
                setChanged();
            }

            if (evaporatedFluid != null && evaporatedFluid.getAmount() > 0) {
                var input = new FluidRecipeInput(evaporatedFluid);
                recipe = RecipeSearch.search(level, AgresticRecipeTypes.EVAPORATING_BASIN.get()).findRecipe(input);
                ItemStack result = recipe.getResultItem(level.registryAccess());
                if (evaporatedFluid.getAmount() >= recipe.getInput().amount() && container.insertItem(0, result, true).isEmpty()) {
                    evaporatedFluid.setAmount(evaporatedFluid.getAmount() - recipe.getInput().amount());
                    container.insertItem(0, result, false);
                    level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
                    setChanged();
                }
            }
        }
    }

    public void clientTick() {

    }

    public ItemInteractionResult interact(Player player, InteractionHand hand, Direction side) {
        if (level == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem != ItemStack.EMPTY) {
            if (FluidTransferHelper.interactWithTank(level, worldPosition, player, hand, side, side)) {
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public InteractionResult interactEmpty(Player player) {
        if (level == null) return InteractionResult.PASS;
        if (player.isShiftKeyDown() && this.getFluidStack().getAmount() > 0) {
            FluidStack drained = this.tank.drain(getCapacity(), IFluidHandler.FluidAction.EXECUTE);
            SoundEvent soundevent = FluidTransferHelper.getEmptySound(drained);
            if (soundevent != null) {
                level.playSound(null, this.worldPosition, soundevent, SoundSource.BLOCKS, 1F, 1F);
            }
            FluidTransferHelper.displayDrained(player, drained);
            markUpdated();
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (!getStack().isEmpty() && !level.isClientSide) {
            level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), getStack()));
            container.setStackInSlot(0, ItemStack.EMPTY);
            markUpdated();
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AgresticBlockEntities.EVAPORATING_BASIN.get(), (be, context) -> be.tank);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AgresticBlockEntities.EVAPORATING_BASIN.get(), (be, context) -> be.container);
    }

    @Override
    public final void setRemoved() {
        super.setRemoved();
        invalidateCapabilities();
    }

    public static void updateLight(BlockEntity be, IFluidTank tank) {
        Level level = be.getLevel();
        if (level != null && !level.isClientSide) {
            FluidStack fluid = tank.getFluid();
            int light = fluid.isEmpty() ? 0 : fluid.getFluid().getFluidType().getLightLevel(fluid);
            BlockState state = be.getBlockState();
            if (light != state.getValue(ILightEmitting.LIGHT)) {
                level.setBlock(be.getBlockPos(), state.setValue(ILightEmitting.LIGHT, light), Block.UPDATE_CLIENTS);
            }
        }
    }

    public FluidStack getFluidStack() {
        var inv = getTank();
        return inv == null ? FluidStack.EMPTY : inv.getFluid();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        updateLight(this, this.tank.readFromNBT(registries, tag.getCompound("Tank")));
        this.container.deserializeNBT(registries, tag.getCompound("Container"));
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Tank", this.tank.writeToNBT(registries, new CompoundTag()));
        tag.put("Container", this.container.serializeNBT(registries));
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean hasStack() {
        return !getStack().isEmpty();
    }

    public ItemStack getStack() {
        return container.getStackInSlot(0);
    }
}
