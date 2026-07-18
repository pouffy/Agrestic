package io.github.pouffy.agrestic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.CommonHooks;

public class AppleLeavesBlock extends LeavesBlock implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public AppleLeavesBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(DISTANCE, 7).setValue(PERSISTENT, false).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }

    public int getMaxAge() {
        return 3;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !isFullyGrown(state) || state.getValue(DISTANCE) == 7 && !state.getValue(PERSISTENT);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        int i = state.getValue(AGE);

        if (i < this.getMaxAge() && isAirAdjacent(level, pos)) {
            float f = getGrowthChance(this, level, pos);

            if (CommonHooks.canCropGrow(level, pos, state, random.nextInt((int) (50.0F / f) + 1) == 0)) {
                level.setBlock(pos, state.setValue(AGE, (i + 1)), 2);
                CommonHooks.fireCropGrowPost(level, pos, state);
            }
        }
    }

    protected static float getGrowthChance(Block blockIn, ServerLevel worldIn, BlockPos pos) {
        return 1F;
    }

    protected static boolean isAirAdjacent(LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).isAir() || world.getBlockState(pos.north()).isAir() || world.getBlockState(pos.south()).isAir() || world.getBlockState(pos.west()).isAir() || world.getBlockState(pos.east()).isAir();
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return !isFullyGrown(blockState) && isAirAdjacent(levelReader, blockPos);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return !isFullyGrown(blockState);
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        if (isAirAdjacent(serverLevel, blockPos)) {
            int newAge = Math.min(blockState.getValue(AGE) + 1, getMaxAge());
            serverLevel.setBlock(blockPos, blockState.setValue(AGE, newAge), Block.UPDATE_NEIGHBORS);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (isFullyGrown(state)) {
            harvestFruit(world, pos, state);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.FAIL;
    }

    protected void harvestFruit(Level world, BlockPos pos, BlockState state) {
        Block.popResource(world, pos, new ItemStack(Items.APPLE));

        world.setBlock(pos, state.setValue(AGE, 0), Block.UPDATE_NEIGHBORS);
        world.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    protected boolean isFullyGrown(BlockState state) {
        return state.getValue(AGE) == getMaxAge();
    }
}
