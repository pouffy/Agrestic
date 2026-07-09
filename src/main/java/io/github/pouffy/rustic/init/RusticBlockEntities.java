package io.github.pouffy.rustic.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

public class RusticBlockEntities {


    public static void addBlockEntities(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.SIGN, RusticBlocks.OLIVE.sign().get(), RusticBlocks.OLIVE.wallSign().get(), RusticBlocks.IRONWOOD.sign().get(), RusticBlocks.IRONWOOD.wallSign().get());
        event.modify(BlockEntityType.HANGING_SIGN, RusticBlocks.OLIVE.hangingSign().get(), RusticBlocks.OLIVE.hangingWallSign().get(), RusticBlocks.IRONWOOD.hangingSign().get(), RusticBlocks.IRONWOOD.hangingWallSign().get());
    }
}
