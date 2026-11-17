package io.github.pouffy.agrestic.common.block.entity;

import com.google.common.collect.Lists;
import io.github.pouffy.agrestic.AgresticClient;
import io.github.pouffy.agrestic.client.ui.CondenserMenu;
import io.github.pouffy.agrestic.common.block.CondenserBlock;
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
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
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

    public CondenserBlockEntity(BlockPos pos, BlockState blockState) {
        super(AgresticBlockEntities.BASIC_CONDENSER.get(), pos, blockState);
        this.tank = new AgresticFluidTank(8000, this::onTankChanged);
        updateCapability = false;
        refreshCapability();
        this.recipesUsed = new Object2IntOpenHashMap<>();
        this.quickCheck = RecipeManager.createCheck(AgresticRecipeTypes.CONDENSING.get());
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
        if (level == null) return InteractionResult.PASS;
        if (!level.isClientSide) {
            player.openMenu(this, worldPosition);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public ItemInteractionResult interact(Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!FluidTransferHelper.interactWithTank(level, worldPosition, player, hand, hitResult)) {
            player.openMenu(this, worldPosition);
        }
        return ItemInteractionResult.SUCCESS;
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
            this.progress = tag.getInt("Progress");
            this.progressTotal = tag.getInt("ProgressTotal");
            this.litTime = tag.getInt("LitTime");
            this.litDuration = tag.getInt("LitDuration");
            CompoundTag usedRecipes = tag.getCompound("RecipesUsed");
            for(String s : usedRecipes.getAllKeys()) {
                this.recipesUsed.put(ResourceLocation.parse(s), usedRecipes.getInt(s));
            }
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

    private static int getTotalCookTime(Level level, CondenserBlockEntity blockEntity) {
        CondensingRecipeInput recipeInput = new CondensingRecipeInput(blockEntity.getTank().getFluid(), blockEntity.getInputStacks());
        return blockEntity.quickCheck.getRecipeFor(recipeInput, level).map((holder) -> (holder.value()).getTime()).orElse(400);
    }

    public void setItem(int index, ItemStack stack) {
        if (!isController()) return;
        ItemStack itemstack = this.container.getStackInSlot(index);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(itemstack, stack);
        this.container.setStackInSlot(index, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        if ((index == 0 || index == 1) && !flag) {
            this.progressTotal = getTotalCookTime(this.level, this);
            this.progress = 0;
            this.setChanged();
        }
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
        //Global shit
        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }

        //Everything under here is only done by the controller
        if (!isController()) return;

        if (isLit()) {
            --litTime;
        }
    }

    @Override
    public void clientTick() {
        if (level == null) return;
        if (!isController()) return;
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
