package io.github.pouffy.rustic.datagen.server.tags;

import io.github.pouffy.rustic.Rustic;
import io.github.pouffy.rustic.init.RusticBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Rustic.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for(RusticBlocks.Woodset woodset : RusticBlocks.WOODSETS) {
            this.tag(ItemTags.SIGNS).add(woodset.sign().item());
            this.tag(ItemTags.HANGING_SIGNS).add(woodset.hangingSign().item());
            this.tag(ItemTags.PLANKS).add(woodset.planks().item());
            this.tag(Tags.Items.FENCE_GATES_WOODEN).add(woodset.fenceGate().item());
            this.tag(Tags.Items.FENCES_WOODEN).add(woodset.fence().item());
            this.tag(ItemTags.WOODEN_FENCES).add(woodset.fence().item());
            this.tag(ItemTags.WOODEN_STAIRS).add(woodset.stairs().item());
            this.tag(ItemTags.WOODEN_BUTTONS).add(woodset.button().item());
            this.tag(ItemTags.WOODEN_DOORS).add(woodset.door().item());
            this.tag(ItemTags.WOODEN_SLABS).add(woodset.slab().item());
            this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(woodset.pressurePlate().item());
            this.tag(ItemTags.WOODEN_TRAPDOORS).add(woodset.trapdoor().item());
            this.tag(ItemTags.LOGS).add(woodset.log().item());
            this.tag(Tags.Items.STRIPPED_LOGS).add(woodset.strippedLog().item());
            this.tag(Tags.Items.STRIPPED_WOODS).add(woodset.strippedWood().item());
        }
    }

    @SafeVarargs
    protected final void addToTags(Item item, TagKey<Item>... itemTags) {
        List.of(itemTags).forEach((itemTag) -> this.tag(itemTag).add(item));
    }

    @SafeVarargs
    protected final void addToTags(TagKey<Item> item, TagKey<Item>... itemTags) {
        List.of(itemTags).forEach((itemTag) -> this.tag(itemTag).addTag(item));
    }
}
