package io.github.pouffy.agrestic.common.block;

import com.mojang.serialization.MapCodec;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AppleCropBlock extends CropBlock {
    public static final MapCodec<AppleCropBlock> CODEC = simpleCodec(AppleCropBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
            Block.box(6.0F, 0.0F, 6.0F, 10.0F, 6.0F, 10.0F),
            Block.box(3.0F, 0.0F, 3.0F, 13.0F,12.0F, 13.0F),
            Block.box(2.0F, 0.0F, 2.0F, 14.0F,12.0F, 14.0F),
            Block.box(2.0F, 0.0F, 2.0F, 14.0F,12.0F, 14.0F) };

    @Override
    public MapCodec<AppleCropBlock> codec() {
        return CODEC;
    }

    public AppleCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(AGE) < MAX_AGE && random.nextInt(5) == 0 && level.getLightEngine().getRawBrightness(pos, 0) >= 9) {
            growCrops(level, pos, state);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public void growCrops(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(AGE) < 3) {
            int i = level.random.nextBoolean() ? 1 : 2;
            int j = Math.min(state.getValue(AGE) + i, getMaxAge());

            if (state.getValue(AGE) + i >= MAX_AGE) {
                transformToSapling(level, pos);
            } else {
                level.setBlock(pos, state.setValue(AGE, j), Block.UPDATE_NEIGHBORS);
            }
        } else {
            transformToSapling(level, pos);
        }
    }

    private void transformToSapling(Level level, BlockPos pos) {
        level.setBlock(pos, AgresticBlocks.APPLE_SAPLING.block().defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return 1;
    }
}
