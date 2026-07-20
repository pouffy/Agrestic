package io.github.pouffy.agrestic.common.block.entity;

import com.mojang.datafixers.util.Pair;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.client.ui.BrewingBarrelMenu;
import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
import io.github.pouffy.agrestic.common.recipe.BrewingRecipeInput;
import io.github.pouffy.agrestic.common.recipe.EvaporatingBasinRecipe;
import io.github.pouffy.agrestic.core.TextUtils;
import io.github.pouffy.agrestic.core.block.AgresticBlockEntity;
import io.github.pouffy.agrestic.core.fluid.*;
import io.github.pouffy.agrestic.core.item.AgresticItemContainer;
import io.github.pouffy.agrestic.core.recipe.RecipeSearch;
import io.github.pouffy.agrestic.init.AgresticBlockEntities;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BrewingBarrelBlockEntity extends AgresticBlockEntity implements Container, MenuProvider, Nameable {

    @Getter
    protected final CombinedFluidTank tanks;
    @Getter
    protected final AgresticFluidTank inputTank;
    @Getter
    protected final AgresticFluidTank resultTank;
    @Getter
    protected final AgresticFluidTank auxiliaryTank;
    @Getter
    protected final AgresticItemContainer container;

    protected int progress;
    protected int progressTotal;

    private Component customName;

    protected final ContainerData data;

    protected BrewingBarrelRecipe recipe = null;

    public BrewingBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(AgresticBlockEntities.BREWING_BARREL.get(), pos, state);
        this.inputTank = new AgresticFluidTank(8000, this::onInputChanged);
        this.resultTank = new AgresticFluidTank(8000, this::onOutputChanged).forbidInsertion();
        this.auxiliaryTank = new AgresticFluidTank(1000, this::onAuxiliaryChanged);
        this.tanks = new CombinedFluidTank(this.inputTank, this.resultTank, this.auxiliaryTank);
        this.container = new AgresticItemContainer(6, this::onItemsChanged);
        this.data = createIntArray();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AgresticBlockEntities.BREWING_BARREL.get(), (be, context) -> be.tanks);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AgresticBlockEntities.BREWING_BARREL.get(), (be, context) -> be.container);
    }

    public void serverTick() {
        this.handleContainerInteraction(0, 3, this.getInputTank());
        this.handleContainerInteraction(1, 4, this.getResultTank());
        this.handleContainerInteraction(2, 5, this.getAuxiliaryTank());
        if (!getInputTank().isEmpty()) {
            this.tryBrew();
        }
    }

    public void clientTick() {

    }

    public void tryBrew() {
        if (level == null) return;

        BrewingRecipeInput input = new BrewingRecipeInput(getInputTank().getFluid(), getAuxiliaryTank().getFluid());
        BrewingBarrelRecipe recipe = RecipeSearch.search(level, AgresticRecipeTypes.BREWING_BARREL.get()).findRecipe(input);

        if (recipe == null) {
            this.progress = 0;
            this.progressTotal = 0;
            markUpdated();
            return;
        }

        this.progressTotal = recipe.getTime();
        if (getInputTank().getFluidAmount() > 0 && getResultTank().getFluidAmount() < getResultTank().getCapacity()) {
            this.progress++;

            if (progress >= progressTotal) {
                brew(recipe, input);
                progress = 0;
                progressTotal = 0;
                markUpdated();
            }
        }
    }

    private void brew(BrewingBarrelRecipe recipe, BrewingRecipeInput input) {
        int availableSpace = getResultTank().getCapacity() - getResultTank().getFluidAmount();
        int amountToConvert = Math.min(availableSpace, input.getFluid().getAmount());

        if (amountToConvert <= 0) return;

        assert level != null;
        FluidStack newResult = recipe.finish(input, level.random);

        if (newResult != null && !newResult.isEmpty()) {

            FluidStack dummyExisting = getResultTank().getFluid().copy(), dummyNew = newResult.copy();
            dummyExisting.remove(AgresticDataComponents.QUALITY.get());
            dummyNew.remove(AgresticDataComponents.QUALITY.get());
            if (!getResultTank().isEmpty() && FluidStack.isSameFluidSameComponents(dummyExisting, dummyNew)) {
                getInputTank().drain(input.getFluid().copyWithAmount(amountToConvert), IFluidHandler.FluidAction.EXECUTE);
                float existingQuality = BrewingBarrelRecipe.getQuality(getResultTank().getFluid());
                newResult = BrewingBarrelRecipe.withQuality(newResult, existingQuality);
                getResultTank().forceFill(newResult.copyWithAmount(amountToConvert), IFluidHandler.FluidAction.EXECUTE);
            } else {
                getInputTank().drain(input.getFluid().copyWithAmount(amountToConvert), IFluidHandler.FluidAction.EXECUTE);
                getResultTank().forceFill(newResult.copyWithAmount(amountToConvert), IFluidHandler.FluidAction.EXECUTE);
            }
            markUpdated();
        }
    }

    private void handleContainerInteraction(int slot, int outSlot, AgresticFluidTank tank) {
        ItemStack inStack = getContainer().getStackInSlot(slot);
        if (inStack.isEmpty()) return;
        if (FluidHelper.tryEmptyItemIntoTank(level, getContainer(), slot, outSlot, inStack, this, tank))
            return;
        if (FluidHelper.tryFillItemFromTank(level, getContainer(), slot, outSlot, inStack, this, tank))
            return;
    }

    protected void onInputChanged(FluidStack newFluids) {
        if (inputTank != null) {
            inputTank.setCapacity(8000);
            if (inputTank.getSpace() < 0) inputTank.drain(-inputTank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }
        markUpdated();
    }

    protected void onOutputChanged(FluidStack newFluids) {
        if (resultTank != null) {
            resultTank.setCapacity(8000);
            if (resultTank.getSpace() < 0) resultTank.drain(-resultTank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }
        markUpdated();
    }

    protected void onAuxiliaryChanged(FluidStack newFluids) {
        if (auxiliaryTank != null) {
            auxiliaryTank.setCapacity(1000);
            if (auxiliaryTank.getSpace() < 0) auxiliaryTank.drain(-auxiliaryTank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }
        markUpdated();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.inputTank.readFromNBT(registries, tag.getCompound("InputTank"));
        this.resultTank.readFromNBT(registries, tag.getCompound("ResultTank"));
        this.auxiliaryTank.readFromNBT(registries, tag.getCompound("AuxiliaryTank"));
        this.container.deserializeNBT(registries, tag.getCompound("Container"));
        this.progress = tag.getInt("Progress");
        this.progressTotal = tag.getInt("ProgressTotal");
        if (customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(customName, registries));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("InputTank", this.inputTank.writeToNBT(registries, new CompoundTag()));
        tag.put("ResultTank", this.resultTank.writeToNBT(registries, new CompoundTag()));
        tag.put("AuxiliaryTank", this.auxiliaryTank.writeToNBT(registries, new CompoundTag()));
        tag.put("Container", this.container.serializeNBT(registries));
        tag.putInt("Progress", this.progress);
        tag.putInt("ProgressTotal", this.progressTotal);
        if (tag.contains("CustomName", 8)) {
            customName = Component.Serializer.fromJson(tag.getString("CustomName"), registries);
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

    public List<ItemStack> getAllStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < getContainer().getSlots(); i++) {
            ItemStack contained = getContainer().getStackInSlot(i);
            if (!contained.isEmpty()) {
                stacks.add(contained);
            }
        }
        return stacks;
    }

    @Override
    public int getContainerSize() {
        return this.getContainer().getSlots();
    }

    @Override
    public boolean isEmpty() {
        return this.getContainer().isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.getContainer().getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack itemstack = this.getContainer().extractItem(index, count, false);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return this.getContainer().extractItem(index, getContainer().getStackInSlot(index).getMaxStackSize(), false);
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        this.getContainer().insertItem(index, itemStack, false);
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.getContainer().clear();
        this.markUpdated();
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        this.customName = componentInput.get(DataComponents.CUSTOM_NAME);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CUSTOM_NAME, this.customName);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("CustomName");
    }

    @Override
    public Component getName() {
        return customName != null ? customName : Component.translatable(Agrestic.location("brewing_barrel").toLanguageKey("container"));
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    @Nullable
    public Component getCustomName() {
        return customName;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
        return new BrewingBarrelMenu(syncId, inventory, this, this.data);
    }

    private ContainerData createIntArray() {
        return new ContainerData()
        {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BrewingBarrelBlockEntity.this.progress;
                    case 1 -> BrewingBarrelBlockEntity.this.progressTotal;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BrewingBarrelBlockEntity.this.progress = value;
                    case 1 -> BrewingBarrelBlockEntity.this.progressTotal = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public ItemStack tryFillContainer(ItemStack heldStack) {
        if (ItemFilling.canItemBeFilled(level, heldStack) && !getResultTank().isEmpty()) {
            int requiredAmount = ItemFilling.getRequiredAmountForItem(level, heldStack, getResultTank().getFluid());
           return ItemFilling.fillItem(level, requiredAmount, heldStack, getResultTank().getFluid());
        }
        return ItemStack.EMPTY;
    }
}
