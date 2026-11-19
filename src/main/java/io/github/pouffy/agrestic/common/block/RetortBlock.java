package io.github.pouffy.agrestic.common.block;

import com.mojang.serialization.MapCodec;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RetortBlock extends HorizontalDirectionalBlock {

    public static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 16, 12);

    public RetortBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected MapCodec<RetortBlock> codec() {
        return simpleCodec(RetortBlock::new);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockState condenser = level.getBlockState(pos.relative(state.getValue(FACING).getOpposite()));
        if (condenser.getBlock() == AgresticBlocks.BASIC_CONDENSER.block()) {
            return condenser.useWithoutItem(level, player, hitResult.withPosition(pos.relative(state.getValue(FACING).getOpposite())));
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockState condenser = level.getBlockState(pos.relative(state.getValue(FACING).getOpposite()));
        if (condenser.getBlock() == AgresticBlocks.BASIC_CONDENSER.block()) {
            return condenser.useItemOn(stack, level, player, hand, hitResult.withPosition(pos.relative(state.getValue(FACING).getOpposite())));
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
