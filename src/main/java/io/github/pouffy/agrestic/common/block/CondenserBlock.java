package io.github.pouffy.agrestic.common.block;

import com.mojang.serialization.MapCodec;
import io.github.pouffy.agrestic.common.block.entity.CondenserBlockEntity;
import io.github.pouffy.agrestic.init.AgresticBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class CondenserBlock extends BaseEntityBlock {
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape CHIMNEY_SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(4, 4, 4, 12, 8, 12)
    );

    private static final VoxelShape TOP_SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(4, 4, 4, 12, 8, 12),
            Block.box(0, -16, 0, 16, 0, 16)
    );

    private static final VoxelShape BOTTOM_SHAPE = Shapes.or(
            Block.box(0, 16, 0, 16, 20, 16),
            Block.box(4, 20, 4, 12, 24, 12),
            Block.box(0, 0, 0, 16, 16, 16)
    );

    public CondenserBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == Half.TOP) {
            return TOP_SHAPE;
        }
        return BOTTOM_SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == Half.TOP) {
            return CHIMNEY_SHAPE;
        }
        return super.getCollisionShape(state, level, pos, context);
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getValue(HALF) == Half.TOP) {
            return CHIMNEY_SHAPE;
        }
        return super.getInteractionShape(state, level, pos);
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getValue(HALF) == Half.TOP) {
            return CHIMNEY_SHAPE;
        }
        return super.getOcclusionShape(state, level, pos);
    }

    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected MapCodec<CondenserBlock> codec() {
        return simpleCodec(CondenserBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, HALF);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(HALF, Half.BOTTOM);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return super.canSurvive(state, level, pos) && level.getBlockState(pos.above()).isAir();
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        if (oldState.getBlock() == state.getBlock() || moved)
            return;

        if (level.getBlockEntity(pos) instanceof CondenserBlockEntity blockEntity && state.getValue(HALF) == Half.TOP) {
            blockEntity.setController(pos.below());
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.getBlock() instanceof CondenserBlock) {
            return;
        }

        asController(level, pos, (be) -> {
            for (ItemStack stack : be.getAllStacks()) {
                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
            }
        });

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return onBlockEntityUse(level, pos, (be) -> be.interactEmpty(player));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return onBlockEntityUseItemOn(level, pos, (be) -> be.interact(player, hand, hitResult));
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && (player.isCreative() || !player.hasCorrectToolForDrops(state, level, pos))) {
            preventDropFromBottomPart(level, pos, state, player);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        Half half = state.getValue(HALF);
        if (half == Half.BOTTOM) {
            return level.getBlockState(currentPos.above()).getBlock() instanceof CondenserBlock && level.getBlockState(currentPos.above()).getValue(HALF) == Half.TOP ? super.updateShape(state, facing, facingState, level, currentPos, facingPos) : Blocks.AIR.defaultBlockState();
        } else {
            return level.getBlockState(currentPos.below()).getBlock() instanceof CondenserBlock && level.getBlockState(currentPos.below()).getValue(HALF) == Half.BOTTOM ? super.updateShape(state, facing, facingState, level, currentPos, facingPos) : Blocks.AIR.defaultBlockState();
        }
    }

    protected static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
        Half half = state.getValue(HALF);
        if (half == Half.TOP) {
            BlockPos blockpos = pos.below();
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(state.getBlock()) && blockstate.getValue(HALF) == Half.BOTTOM) {
                BlockState blockstate1 = blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(blockpos, blockstate1, 35);
                level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
            }
        }

    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (state.getValue(HALF) == Half.BOTTOM) {
            BlockPos blockpos = pos.relative(Direction.UP);
            level.setBlock(blockpos, state.setValue(HALF, Half.TOP), 3);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(HALF) == Half.TOP) {
            return null;
        }
        if (!world.isClientSide()) {
            return createTickerHelper(type, AgresticBlockEntities.BASIC_CONDENSER.get(), (l, pos, st, be) -> be.serverTick());
        }
        return createTickerHelper(type, AgresticBlockEntities.BASIC_CONDENSER.get(), (l, pos, st, be) -> be.clientTick());
    }

    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CondenserBlockEntity(blockPos, blockState);
    }

    protected void asController(BlockGetter world, BlockPos pos, Consumer<CondenserBlockEntity> action) {
        getControllerBlockEntity(world, pos).ifPresent(action);
    }

    protected InteractionResult onBlockEntityUse(BlockGetter world, BlockPos pos, Function<CondenserBlockEntity, InteractionResult> action) {
        return getControllerBlockEntity(world, pos).map(action)
                .orElse(InteractionResult.PASS);
    }

    protected ItemInteractionResult onBlockEntityUseItemOn(BlockGetter world, BlockPos pos, Function<CondenserBlockEntity, ItemInteractionResult> action) {
        return getControllerBlockEntity(world, pos).map(action)
                .orElse(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
    }

    protected Optional<CondenserBlockEntity> getControllerBlockEntity(BlockGetter world, BlockPos pos) {
        return Optional.ofNullable(getBlockEntity(world, pos)).map(CondenserBlockEntity::getControllerBE);
    }

    @Nullable
    protected CondenserBlockEntity getBlockEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        Class<CondenserBlockEntity> expectedClass = CondenserBlockEntity.class;

        if (blockEntity == null)
            return null;
        if (!expectedClass.isInstance(blockEntity))
            return null;

        return (CondenserBlockEntity) blockEntity;
    }
}
