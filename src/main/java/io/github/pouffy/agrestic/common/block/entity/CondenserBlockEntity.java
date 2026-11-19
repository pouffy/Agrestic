package io.github.pouffy.agrestic.common.block.entity;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.github.pouffy.agrestic.AgresticClient;
import io.github.pouffy.agrestic.client.ui.CondenserMenu;
import io.github.pouffy.agrestic.common.block.CondenserBlock;
import io.github.pouffy.agrestic.common.block.RetortBlock;
import io.github.pouffy.agrestic.common.recipe.BasicCondensingRecipe;
import io.github.pouffy.agrestic.common.recipe.CondensingRecipeInput;
import io.github.pouffy.agrestic.core.block.AgresticContainerBlockEntity;
import io.github.pouffy.agrestic.core.fluid.AgresticFluidTank;
import io.github.pouffy.agrestic.core.item.AgresticItemContainer;
import io.github.pouffy.agrestic.init.AgresticBlockEntities;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.block.GaugeBlock;
import slimeknights.mantle.fluid.FluidTransferHelper;

import java.util.ArrayList;
import java.util.List;

public class CondenserBlockEntity extends AgresticContainerBlockEntity implements RecipeCraftingHolder, StackedContentsCompatible, Clearable {

    @Getter
    protected final AgresticFluidTank tank;

    protected boolean updateCapability;
    protected IFluidHandler fluidCapability;
    protected IItemHandlerModifiable itemCapability;

    protected BlockPos controller;

    @Getter
    protected int progress;
    @Getter
    protected int progressTotal;
    @Getter
    int litTime;
    @Getter
    int litDuration;

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed;
    private final RecipeManager.CachedCheck<CondensingRecipeInput, BasicCondensingRecipe> quickCheck;
    @Getter
    private Pair<Boolean, Boolean> retorts;

    public CondenserBlockEntity(BlockPos pos, BlockState blockState) {
        super(AgresticBlockEntities.BASIC_CONDENSER.get(), pos, blockState);
        this.tank = new AgresticFluidTank(8000, this::onTankChanged);
        updateCapability = false;
        refreshCapability();
        this.recipesUsed = new Object2IntOpenHashMap<>();
        this.quickCheck = RecipeManager.createCheck(AgresticRecipeTypes.CONDENSING.get());
        this.retorts = Pair.of(false, false);
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
        if (!isController()) return;
        if (tank != null) {
            tank.setCapacity(8000);
            if (tank.getSpace() < 0) tank.drain(-tank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
            FluidStack fluidStack = this.tank.getFluid();
            boolean flag = !newFluids.isEmpty() && FluidStack.isSameFluidSameComponents(fluidStack, newFluids);
            if (!flag) {
                this.progressTotal = getTotalCookTime(this.level);
                this.progress = 0;
            }
        }
        markUpdated();
    }

    public InteractionResult interactEmpty(Player player) {
        if (level == null) return InteractionResult.PASS;
        if (!level.isClientSide) {
            if (hasRetorts()) {
                player.openMenu(this, worldPosition);
                return InteractionResult.SUCCESS;
            } else {
                player.displayClientMessage(Component.translatable("ui.agrestic.missing_retorts").withStyle(ChatFormatting.DARK_RED), true);
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public ItemInteractionResult interact(Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!hasRetorts()) {
            player.displayClientMessage(Component.translatable("ui.agrestic.missing_retorts").withStyle(ChatFormatting.DARK_RED), true);
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!FluidTransferHelper.interactWithTank(level, worldPosition, player, hand, hitResult)) {
            player.openMenu(this, worldPosition);
        }
        return ItemInteractionResult.SUCCESS;
    }

    private boolean hasRetorts() {
        if (level == null) return false;
        evaluate();
        return retorts.getFirst() && retorts.getSecond();
    }

    private void evaluate() {
        if (level == null) return;
        boolean left = false;
        boolean right = false;
        Direction facing = getBlockState().getValue(CondenserBlock.FACING);
        Direction leftDir = facing.getClockWise();
        Direction rightDir = facing.getCounterClockWise();
        if (level.getBlockState(worldPosition.relative(leftDir)).getBlock() == AgresticBlocks.BASIC_RETORT.block() && level.getBlockState(worldPosition.relative(leftDir)).getValue(RetortBlock.FACING) == leftDir) {
            left = true;
        }
        if (level.getBlockState(worldPosition.relative(rightDir)).getBlock() == AgresticBlocks.BASIC_RETORT.block() && level.getBlockState(worldPosition.relative(rightDir)).getValue(RetortBlock.FACING) == rightDir) {
            right = true;
        }
        retorts = Pair.of(left, right);
        markUpdated();
    }

    @Override
    public AgresticItemContainer newContainer() {
        return new AgresticItemContainer(5, this::onItemsChanged);
    }

    @Override
    public ContainerData createContainerData() {
        return new ContainerData() {

            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CondenserBlockEntity.this.progress;
                    case 1 -> CondenserBlockEntity.this.progressTotal;
                    case 2 -> {
                        if (CondenserBlockEntity.this.litDuration > 32767) {
                            yield Mth.floor((float) CondenserBlockEntity.this.litTime / CondenserBlockEntity.this.litDuration * 32767.0F);
                        }
                        yield CondenserBlockEntity.this.litTime;
                    }
                    case 3 -> Math.min(CondenserBlockEntity.this.litDuration, 32767);
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CondenserBlockEntity.this.progress = value;
                    case 1 -> CondenserBlockEntity.this.progressTotal = value;
                    case 2 -> CondenserBlockEntity.this.litTime = value;
                    case 3 -> CondenserBlockEntity.this.litDuration = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
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
        markUpdated();
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
            this.progress = tag.getInt("Progress");
            this.progressTotal = tag.getInt("ProgressTotal");
            this.litTime = tag.getInt("LitTime");
            this.litDuration = tag.getInt("LitDuration");
            CompoundTag usedRecipes = tag.getCompound("RecipesUsed");
            for(String s : usedRecipes.getAllKeys()) {
                this.recipesUsed.put(ResourceLocation.parse(s), usedRecipes.getInt(s));
            }
            CompoundTag retortsTag = tag.getCompound("Retorts");
            this.retorts = Pair.of(retortsTag.getBoolean("Left"), retortsTag.getBoolean("Right"));
        }
        updateCapability = true;
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (isController()) {
            tag.put("Tank", this.tank.writeToNBT(registries, new CompoundTag()));
            tag.putInt("Progress", this.progress);
            tag.putInt("ProgressTotal", this.progressTotal);
            tag.putInt("LitTime", this.litTime);
            tag.putInt("LitDuration", this.litDuration);
            CompoundTag usedRecipes = new CompoundTag();
            this.recipesUsed.forEach((key, amount) -> usedRecipes.putInt(key.toString(), amount));
            tag.put("RecipesUsed", usedRecipes);
            CompoundTag retortsTag = new CompoundTag();
            retortsTag.putBoolean("Left", this.retorts.getFirst());
            retortsTag.putBoolean("Right", this.retorts.getSecond());
            tag.put("Retorts", retortsTag);
        } else {
            tag.put("Controller", NbtUtils.writeBlockPos(controller));
        }
    }

    public List<ItemStack> getInputStacks() {
        if (!isController()) return List.of();
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(getContainer().getStackInSlot(0));
        stacks.add(getContainer().getStackInSlot(1));
        return stacks;
    }

    public boolean hasInputStacks() {
        if (!isController()) return false;
        List<ItemStack> stacks = new ArrayList<>(getInputStacks());
        stacks.removeIf(ItemStack::isEmpty);
        return !stacks.isEmpty();
    }

    public ItemStack getContainerStack() {
        if (!isController()) return ItemStack.EMPTY;
        return getContainer().getStackInSlot(3);
    }

    private int getTotalCookTime(Level level) {
        CondensingRecipeInput recipeInput = new CondensingRecipeInput(getTank().getFluid(), getInputStacks(), getContainerStack());
        return quickCheck.getRecipeFor(recipeInput, level).map((holder) -> (holder.value()).getTime()).orElse(400);
    }

    public void setItem(int index, ItemStack stack) {
        if (!isController()) return;
        ItemStack itemstack = this.container.getStackInSlot(index);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(itemstack, stack);
        this.container.setStackInSlot(index, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        if ((index == 0 || index == 1) && !flag) {
            this.progressTotal = getTotalCookTime(this.level);
            this.progress = 0;
            markUpdated();
        }
    }

    public void onItemsChanged(int slot, ItemStack stack) {
        if (!isController()) return;
        ItemStack itemstack = this.container.getStackInSlot(slot);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(itemstack, stack);
        if ((slot == 0 || slot == 1 || slot == 3) && !flag) {
            this.progressTotal = getTotalCookTime(this.level);
            this.progress = 0;
        }
        this.markUpdated();
    }

    public boolean canPlaceItem(int index, ItemStack stack) {
        if (index == 4) {
            return false;
        } else if (index != 2) {
            return true;
        } else {
            return stack.getBurnTime(AgresticRecipeTypes.CONDENSING.get()) > 0;
        }
    }

    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        if (!isController()) return;
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.id();
            this.recipesUsed.addTo(resourcelocation, 1);
        }
    }

    @Override
    public @Nullable RecipeHolder<?> getRecipeUsed() {
        return null;
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
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
        if (!isController()) return null;
        return new CondenserMenu(syncId, inventory, this, this.data);
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    @Override
    public void serverTick() {
        if (level == null) return;
        //Global shit
        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }

        //Everything under here is only done by the controller
        if (!isController()) return;
        if (!hasRetorts()) return;

        boolean update = false;

        if (isLit()) {
            --litTime;
        }

        ItemStack fuel = container.getStackInSlot(2);
        boolean hasFuel = !fuel.isEmpty();

        if (isLit() || hasFuel && hasInputStacks()) {
            RecipeHolder<BasicCondensingRecipe> recipeholder;
            if (hasInputStacks()) {
                recipeholder = quickCheck.getRecipeFor(new CondensingRecipeInput(getTank().getFluid(), getInputStacks(), getContainerStack()), level).orElse(null);
            } else {
                recipeholder = null;
            }

            if (!isLit() && canBurn(level.registryAccess(), recipeholder)) {
                litTime = getBurnDuration(fuel);
                litDuration = litTime;
                if (isLit()) {
                    if (fuel.hasCraftingRemainingItem()) {
                        container.setStackInSlot(2, fuel.getCraftingRemainingItem());
                    } else if (!fuel.isEmpty()) {
                        if (!container.getStackInSlot(2).isEmpty()) {
                            container.getStackInSlot(2).shrink(1);
                            if (container.getStackInSlot(2).isEmpty()) {
                                container.setStackInSlot(2, container.getStackInSlot(2).getCraftingRemainingItem());
                            }
                        }
                    }
                }
            }

            if (isLit() && canBurn(level.registryAccess(), recipeholder)) {
                ++progress;
                if (progress == progressTotal) {
                    progress = 0;
                    progressTotal = getTotalCookTime(level);
                    if (condense(level.registryAccess(), recipeholder)) {
                        setRecipeUsed(recipeholder);
                    }
                }
                update = true;
            } else {
                progress = 0;
            }
        } else if (!isLit() && progress > 0) {
            progress = Mth.clamp(progress - 2, 0, progressTotal);
        }

        if (update) {
            markUpdated();
        }
    }

    @Override
    public void clientTick() {
        if (level == null) return;
        if (!isController()) return;
        if (progress <= 0 || !hasRetorts()) return;
        if (level.getBlockState(worldPosition).getBlock() == AgresticBlocks.BASIC_CONDENSER.block()) {
            Direction blockFacing = level.getBlockState(worldPosition).getValue(CondenserBlock.FACING);
            double yVel = 0.125;
            if (blockFacing == Direction.NORTH || blockFacing == Direction.SOUTH) {
                AgresticClient.spawnAlchemySmokeFX(this.progress, this.worldPosition.getX() - 0.5D, this.worldPosition.getY() + 1.0625D, this.worldPosition.getZ() + 0.5D, 0, yVel, 0);
                AgresticClient.spawnAlchemySmokeFX(this.progress, this.worldPosition.getX() + 1.5D, this.worldPosition.getY() + 1.0625D, this.worldPosition.getZ() + 0.5D, 0, yVel, 0);
            } else if (blockFacing == Direction.EAST || blockFacing == Direction.WEST) {
                AgresticClient.spawnAlchemySmokeFX(this.progress, this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 1.0625D, this.worldPosition.getZ() - 0.5D, 0, yVel, 0);
                AgresticClient.spawnAlchemySmokeFX(this.progress, this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 1.0625D, this.worldPosition.getZ() + 1.5D, 0, yVel, 0);
            }
        }
    }

    private boolean canBurn(RegistryAccess registryAccess, @Nullable RecipeHolder<BasicCondensingRecipe> recipe) {
        if (hasInputStacks() && recipe != null) {
            ItemStack resultStack = recipe.value().assemble(new CondensingRecipeInput(getTank().getFluid(), getInputStacks(), getContainerStack()), registryAccess);
            if (resultStack.isEmpty()) {
                return false;
            } else {
                return container.canInsert(4, resultStack);
            }
        } else {
            return false;
        }
    }

    private boolean condense(RegistryAccess registryAccess, @Nullable RecipeHolder<BasicCondensingRecipe> recipe) {
        if (recipe != null && canBurn(registryAccess, recipe)) {
            ItemStack resultStack = recipe.value().assemble(new CondensingRecipeInput(getTank().getFluid(), getInputStacks(), getContainerStack()), registryAccess);
            ItemStack existingResult = container.getStackInSlot(4);
            if (existingResult.isEmpty()) {
                container.setStackInSlot(4, resultStack.copy());
            } else if (container.canInsert(4, resultStack)) {
                container.getStackInSlot(4).grow(resultStack.getCount());
            }

            container.getStackInSlot(0).shrink(1);
            container.getStackInSlot(1).shrink(1);
            getTank().drain(recipe.value().getBase().amount(), IFluidHandler.FluidAction.EXECUTE);
            if (recipe.value().getContainer().isPresent()) {
                container.getStackInSlot(3).shrink(1);
            }
            return true;
        } else {
            return false;
        }
    }

    protected int getBurnDuration(ItemStack fuel) {
        return fuel.isEmpty() ? 0 : fuel.getBurnTime(AgresticRecipeTypes.CONDENSING.get());
    }

    public void awardUsedRecipes(ServerPlayer player) {
        if (!isController()) return;
        List<RecipeHolder<?>> list = this.getRecipesToAward(player.serverLevel());
        player.awardRecipes(list);

        for(RecipeHolder<?> recipeholder : list) {
            if (recipeholder != null) {
                player.triggerRecipeCrafted(recipeholder, this.container.getStacks());
            }
        }

        this.recipesUsed.clear();
    }

    public List<RecipeHolder<?>> getRecipesToAward(ServerLevel level) {
        List<RecipeHolder<?>> list = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent(list::add);
        }

        return list;
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        if (!isController()) return;
        this.container.getStacks().forEach(stackedContents::accountStack);
    }

    @Override
    public void clearContent() {
        if (!isController()) return;
        getContainer().clear();
    }
}
