package io.github.pouffy.agrestic.common.block;

import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChilliPepperBlock extends CropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    private static final int CHILLI_BEARING_AGE = 7;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public ChilliPepperBlock(Properties properties) {
        super(properties.offsetType(BlockBehaviour.OffsetType.XZ));
        this.defaultBlockState().setValue(this.getAgeProperty(), 0);
    }

    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    public int getMaxAge() {
        return 7;
    }

    public int getAge(BlockState state) {
        return state.getValue(AGE);
    }

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    public BlockState withAge(int age) {
        return this.defaultBlockState().setValue(AGE, age);
    }

    protected int getBonemealAgeIncrease(Level worldIn) {
        return Mth.nextInt(worldIn.random, 2, 5);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(AgresticItems.CHILLI_PEPPER_SEEDS.item());
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int i = state.getValue(AGE);
        boolean flag = i == 3;
        return !flag && stack.is(Items.BONE_MEAL) ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        int age = state.getValue(AGE);
        boolean isMature = age == CHILLI_BEARING_AGE;
        if (isMature) {
            int j = 1 + level.random.nextInt(2);
            popResource(level, pos, new ItemStack(AgresticItems.CHILLI_PEPPER.get(), j));

            if (level.random.nextFloat() < 0.05) {
                popResource(level, pos, new ItemStack(AgresticItems.CHILLI_PEPPER.get()));
            }

            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            level.setBlock(pos, state.setValue(AGE, CHILLI_BEARING_AGE - 2), 2);
            return InteractionResult.SUCCESS;
        } else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }
}
