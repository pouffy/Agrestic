package io.github.pouffy.rustic.datagen.server.tags;

import io.github.pouffy.rustic.Rustic;
import io.github.pouffy.rustic.init.RusticBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Rustic.MODID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        for(RusticBlocks.Woodset woodset : RusticBlocks.WOODSETS) {
            this.tag(BlockTags.STANDING_SIGNS).add(woodset.sign().block());
            this.tag(BlockTags.WALL_SIGNS).add(woodset.wallSign().block());
            this.tag(BlockTags.CEILING_HANGING_SIGNS).add(woodset.hangingSign().block());
            this.tag(BlockTags.WALL_HANGING_SIGNS).add(woodset.hangingWallSign().block());
            this.tag(BlockTags.PLANKS).add(woodset.planks().block());
            this.tag(Tags.Blocks.FENCE_GATES_WOODEN).add(woodset.fenceGate().block());
            this.tag(BlockTags.FENCE_GATES).add(woodset.fenceGate().block());
            this.tag(Tags.Blocks.FENCES_WOODEN).add(woodset.fence().block());
            this.tag(BlockTags.WOODEN_FENCES).add(woodset.fence().block());
            this.tag(BlockTags.WOODEN_STAIRS).add(woodset.stairs().block());
            this.tag(BlockTags.WOODEN_BUTTONS).add(woodset.button().block());
            this.tag(BlockTags.WOODEN_DOORS).add(woodset.door().block());
            this.tag(BlockTags.WOODEN_SLABS).add(woodset.slab().block());
            this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(woodset.pressurePlate().block());
            this.tag(BlockTags.WOODEN_TRAPDOORS).add(woodset.trapdoor().block());
            this.tag(BlockTags.LOGS).add(woodset.log().block());
            this.tag(Tags.Blocks.STRIPPED_LOGS).add(woodset.strippedLog().block());
            this.tag(Tags.Blocks.STRIPPED_WOODS).add(woodset.strippedWood().block());
        }
    }

    @SafeVarargs
    protected final void addToTags(Block block, TagKey<Block>... blockTags) {
        List.of(blockTags).forEach((blockTag) -> this.tag(blockTag).add(block));
    }

    @SafeVarargs
    protected final void addToTags(TagKey<Block> block, TagKey<Block>... blockTags) {
        List.of(blockTags).forEach((blockTag) -> this.tag(blockTag).addTag(block));
    }
}
