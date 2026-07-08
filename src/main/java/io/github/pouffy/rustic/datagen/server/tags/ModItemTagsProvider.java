package io.github.pouffy.rustic.datagen.server.tags;

import io.github.pouffy.rustic.Rustic;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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
