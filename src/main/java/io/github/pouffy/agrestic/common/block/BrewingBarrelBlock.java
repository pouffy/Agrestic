package io.github.pouffy.agrestic.common.block;

import com.mojang.serialization.MapCodec;
import io.github.pouffy.agrestic.common.block.entity.BrewingBarrelBlockEntity;
import io.github.pouffy.agrestic.common.block.entity.CrushingTubBlockEntity;
import io.github.pouffy.agrestic.init.AgresticBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BrewingBarrelBlock extends BaseEntityBlock {
    public static final MapCodec<BrewingBarrelBlock> CODEC = simpleCodec(BrewingBarrelBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_Z = Shapes.or(
            Block.box(0, 0, 2, 16, 2, 5),
            Block.box(0, 0, 11, 16, 2, 14),
            Block.box(1, 2, 0, 15, 16, 16)
    );
    private static final VoxelShape SHAPE_X = Shapes.or(
            Block.box(2, 0, 0, 5, 2, 16),
            Block.box(11, 0, 0, 14, 2, 16),
            Block.box(0, 2, 1, 16, 16, 15)
    );

    public BrewingBarrelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? SHAPE_X : SHAPE_Z;
    }

    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.getBlock() instanceof BrewingBarrelBlock) {
            return;
        }

        if (level.getBlockEntity(pos) instanceof BrewingBarrelBlockEntity blockEntity) {
            for (ItemStack stack : blockEntity.getAllStacks()) {
                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
            }

        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof BrewingBarrelBlockEntity brewingBarrel) {
                ItemStack filledStack = brewingBarrel.tryFillContainer(heldStack);
                if (filledStack != ItemStack.EMPTY) {
                    if (!player.getInventory().add(filledStack)) {
                        player.drop(filledStack, false);
                    }
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    player.openMenu(brewingBarrel, pos);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClientSide()) {
            return createTickerHelper(type, AgresticBlockEntities.BREWING_BARREL.get(), (l, pos, st, be) -> be.serverTick());
        }
        return createTickerHelper(type, AgresticBlockEntities.BREWING_BARREL.get(), (l, pos, st, be) -> be.clientTick());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BrewingBarrelBlockEntity(blockPos, blockState);
    }
}
