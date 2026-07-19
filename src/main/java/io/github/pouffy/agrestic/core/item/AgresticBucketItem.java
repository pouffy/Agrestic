package io.github.pouffy.agrestic.core.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class AgresticBucketItem extends BucketItem {
    public final Supplier<? extends Fluid> content;

    public AgresticBucketItem(Supplier<? extends Fluid> content, Properties properties) {
        super(content.get(), properties);
        this.content = content;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, this.content.get() == Fluids.EMPTY ? net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY : net.minecraft.world.level.ClipContext.Fluid.NONE);
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = blockpos.relative(direction);
            if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos1, direction, itemstack)) {
                if (this.content.get() == Fluids.EMPTY) {
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    Block var15 = blockstate1.getBlock();
                    if (var15 instanceof BucketPickup) {
                        BucketPickup bucketpickup = (BucketPickup)var15;
                        ItemStack itemstack3 = bucketpickup.pickupBlock(player, level, blockpos, blockstate1);
                        if (!itemstack3.isEmpty()) {
                            player.awardStat(Stats.ITEM_USED.get(this));
                            bucketpickup.getPickupSound(blockstate1).ifPresent((p_150709_) -> player.playSound(p_150709_, 1.0F, 1.0F));
                            level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, player, itemstack3);
                            if (!level.isClientSide) {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, itemstack3);
                            }

                            return InteractionResultHolder.sidedSuccess(itemstack2, level.isClientSide());
                        }
                    }

                    return InteractionResultHolder.fail(itemstack);
                } else {
                    BlockState blockstate = level.getBlockState(blockpos);
                    BlockPos blockpos2 = this.canBlockContainFluid(player, level, blockpos, blockstate) ? blockpos : blockpos1;
                    if (this.emptyContents(player, level, blockpos2, blockhitresult, itemstack)) {
                        this.checkExtraContent(player, level, itemstack, blockpos2);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos2, itemstack);
                        }

                        player.awardStat(Stats.ITEM_USED.get(this));
                        ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, player, getEmptySuccessItem(itemstack, player));
                        return InteractionResultHolder.sidedSuccess(itemstack1, level.isClientSide());
                    } else {
                        return InteractionResultHolder.fail(itemstack);
                    }
                }
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }

    public boolean emptyContents(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult result, @Nullable ItemStack container) {
        Fluid var7 = this.content.get();
        if (!(var7 instanceof FlowingFluid flowingfluid)) {
            return false;
        } else {
            boolean $$8;
            BlockState blockstate;
            boolean flag2;
            Block var17;
            label78: {
                label77: {
                    blockstate = level.getBlockState(pos);
                    var17 = blockstate.getBlock();
                    $$8 = blockstate.canBeReplaced(this.content.get());
                    if (!blockstate.isAir() && !$$8) {
                        if (!(var17 instanceof LiquidBlockContainer)) {
                            break label77;
                        }

                        LiquidBlockContainer liquidblockcontainer = (LiquidBlockContainer)var17;
                        if (!liquidblockcontainer.canPlaceLiquid(player, level, pos, blockstate, this.content.get())) {
                            break label77;
                        }
                    }

                    flag2 = true;
                    break label78;
                }

                flag2 = false;
            }

            Optional<FluidStack> containedFluidStack = Optional.ofNullable(container).flatMap(FluidUtil::getFluidContained);
            if (!flag2) {
                return result != null && this.emptyContents(player, level, result.getBlockPos().relative(result.getDirection()), (BlockHitResult)null, container);
            } else if (containedFluidStack.isPresent() && this.content.get().getFluidType().isVaporizedOnPlacement(level, pos, (FluidStack)containedFluidStack.get())) {
                this.content.get().getFluidType().onVaporize(player, level, pos, (FluidStack)containedFluidStack.get());
                return true;
            } else if (level.dimensionType().ultraWarm() && this.content.get().is(FluidTags.WATER)) {
                int l = pos.getX();
                int i = pos.getY();
                int j = pos.getZ();
                level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);

                for(int k = 0; k < 8; ++k) {
                    level.addParticle(ParticleTypes.LARGE_SMOKE, (double)l + Math.random(), (double)i + Math.random(), (double)j + Math.random(), (double)0.0F, (double)0.0F, (double)0.0F);
                }

                return true;
            } else {
                if (var17 instanceof LiquidBlockContainer) {
                    LiquidBlockContainer liquidblockcontainer1 = (LiquidBlockContainer)var17;
                    if (liquidblockcontainer1.canPlaceLiquid(player, level, pos, blockstate, this.content.get())) {
                        liquidblockcontainer1.placeLiquid(level, pos, blockstate, flowingfluid.getSource(false));
                        this.playEmptySound(player, level, pos);
                        return true;
                    }
                }

                if (!level.isClientSide && $$8 && !blockstate.liquid()) {
                    level.destroyBlock(pos, true);
                }

                if (!level.setBlock(pos, this.content.get().defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource()) {
                    return false;
                } else {
                    this.playEmptySound(player, level, pos);
                    return true;
                }
            }
        }
    }

    protected void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos) {
        SoundEvent soundevent = this.content.get().getFluidType().getSound(player, level, pos, SoundActions.BUCKET_EMPTY);
        if (soundevent == null) {
            soundevent = this.content.get().is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        }

        level.playSound(player, pos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }

    protected boolean canBlockContainFluid(@Nullable Player player, Level worldIn, BlockPos posIn, BlockState blockstate) {
        return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer)blockstate.getBlock()).canPlaceLiquid(player, worldIn, posIn, blockstate, this.content.get());
    }
}
