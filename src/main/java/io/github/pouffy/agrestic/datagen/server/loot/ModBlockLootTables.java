package io.github.pouffy.agrestic.datagen.server.loot;

import com.pouffydev.krystal_core.foundation.data.loot.*;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockDefinition;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.block.HerbBlock;
import io.github.pouffy.agrestic.core.block.DoorBlockLootType;
import io.github.pouffy.agrestic.core.block.SlabBlockLootType;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ModBlockLootTables extends BlockLootSubProvider {
    private final Set<Block> generatedLootTables = new HashSet<>();

    public ModBlockLootTables(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    protected void generate() {
        for(BlockDefinition<?> definition : AgresticBlocks.BLOCK_DEFINITIONS) {
            Block block = definition.get();
            BlockLootType lootType = definition.lootType();
            if (lootType instanceof SelfBlockLootType) {
                this.dropSelf(block);
            } else if (lootType instanceof OtherBlockLootType otherBlockLootType) {
                this.add(block, this.createSingleItemTable(otherBlockLootType.getBlock()));
            } else if (lootType instanceof ShearsBlockLootType) {
                this.add(block, createShearsOnlyDrop(block));
            } else if (lootType instanceof OtherShearsBlockLootType otherShearsBlockLootType) {
                this.add(block, createShearsOnlyDrop(otherShearsBlockLootType.getBlock()));
            } else if (lootType instanceof SlabBlockLootType) {
                this.add(block, this.createSlabItemTable(block));
            } else if (lootType instanceof DoorBlockLootType) {
                this.add(block, this.createDoorTable(block));
            }
        }
        bearingLeaves(AgresticBlocks.OLIVE.leaves().block(), AgresticBlocks.OLIVE_SAPLING.block(), AgresticItems.OLIVES.asItem(), new float[]{0.2f, 0.3f, 0.15f}, 0.05f, 0.0625f, 0.083333336f, 0.1f);
        bearingLeaves(AgresticBlocks.IRONWOOD.leaves().block(), AgresticBlocks.IRONWOOD_SAPLING.block(), AgresticItems.IRONBERRIES.asItem(), new float[]{0.12f, 0.05f, 0.1f}, 0.05f, 0.0625f, 0.083333336f, 0.1f);

        LootItemCondition.Builder aloeVeraCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.ALOE_VERA.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.ALOE_VERA.block(), this.createCropDrops(AgresticBlocks.ALOE_VERA.block(), AgresticBlocks.ALOE_VERA.item(), AgresticBlocks.ALOE_VERA.item(), aloeVeraCondition));

        LootItemCondition.Builder horsetailCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.HORSETAIL.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.HORSETAIL.block(), this.createCropDrops(AgresticBlocks.HORSETAIL.block(), AgresticBlocks.HORSETAIL.item(), AgresticBlocks.HORSETAIL.item(), horsetailCondition));

        LootItemCondition.Builder cohoshCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.COHOSH.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.COHOSH.block(), this.createCropDrops(AgresticBlocks.COHOSH.block(), AgresticBlocks.COHOSH.item(), AgresticBlocks.COHOSH.item(), cohoshCondition));

        LootItemCondition.Builder chamomileCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.CHAMOMILE.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.CHAMOMILE.block(), this.createCropDrops(AgresticBlocks.CHAMOMILE.block(), AgresticBlocks.CHAMOMILE.item(), AgresticBlocks.CHAMOMILE.item(), chamomileCondition));

        LootItemCondition.Builder cloudsbluffCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.CLOUDSBLUFF.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.CLOUDSBLUFF.block(), this.createCropDrops(AgresticBlocks.CLOUDSBLUFF.block(), AgresticItems.CLOUDSBLUFF.item(), AgresticItems.CLOUDSBLUFF.item(), cloudsbluffCondition));

        LootItemCondition.Builder bloodOrchidCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.BLOOD_ORCHID.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.BLOOD_ORCHID.block(), this.createCropDrops(AgresticBlocks.BLOOD_ORCHID.block(), AgresticBlocks.BLOOD_ORCHID.item(), AgresticBlocks.BLOOD_ORCHID.item(), bloodOrchidCondition));

        LootItemCondition.Builder ginsengCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.GINSENG.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.GINSENG.block(), this.createCropDrops(AgresticBlocks.GINSENG.block(), AgresticBlocks.GINSENG.item(), AgresticBlocks.GINSENG.item(), ginsengCondition));

        LootItemCondition.Builder marshallowCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.MARSH_MALLOW.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.MARSH_MALLOW.block(), this.createCropDrops(AgresticBlocks.MARSH_MALLOW.block(), AgresticItems.MARSH_MALLOW.item(), AgresticItems.MARSH_MALLOW.item(), marshallowCondition));

        LootItemCondition.Builder vantaLilyCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.VANTA_LILY.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.VANTA_LILY.block(), this.createCropDrops(AgresticBlocks.VANTA_LILY.block(), AgresticBlocks.VANTA_LILY.item(), AgresticBlocks.VANTA_LILY.item(), vantaLilyCondition));

        LootItemCondition.Builder windThistleCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.WIND_THISTLE.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.WIND_THISTLE.block(), this.createCropDrops(AgresticBlocks.WIND_THISTLE.block(), AgresticBlocks.WIND_THISTLE.item(), AgresticBlocks.WIND_THISTLE.item(), windThistleCondition));

        LootItemCondition.Builder mooncapMushroomCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.MOONCAP.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.MOONCAP.block(), this.createCropDrops(AgresticBlocks.MOONCAP.block(), AgresticBlocks.MOONCAP.item(), AgresticBlocks.MOONCAP.item(), mooncapMushroomCondition));

        LootItemCondition.Builder deathstalkMushroomCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.DEATHSTALK.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.DEATHSTALK.block(), this.createCropDrops(AgresticBlocks.DEATHSTALK.block(), AgresticBlocks.DEATHSTALK.item(), AgresticBlocks.DEATHSTALK.item(), deathstalkMushroomCondition));

        LootItemCondition.Builder coreRootCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AgresticBlocks.CORE_ROOT.block())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HerbBlock.AGE, HerbBlock.MAX_AGE));
        this.add(AgresticBlocks.CORE_ROOT.block(), this.createCropDrops(AgresticBlocks.CORE_ROOT.block(), AgresticItems.CORE_ROOT.item(), AgresticItems.CORE_ROOT.item(), coreRootCondition));
    }

    private void bearingLeaves(Block leaves, Block sapling, @NotNull Item fruit, float[] fruitChances, float... chances) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        this.add(leaves, this.createSilkTouchOrShearsDispatchTable(leaves, this.applyExplosionCondition(leaves, LootItem.lootTableItem(sapling)).when(BonusLevelTableCondition.bonusLevelFlatChance(registrylookup.getOrThrow(Enchantments.FORTUNE), chances))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(HAS_SHEARS.or(this.hasSilkTouch()).invert()).add(this.applyExplosionDecay(leaves, LootItem.lootTableItem(fruit))).when(BonusLevelTableCondition.bonusLevelFlatChance(registrylookup.getOrThrow(Enchantments.FORTUNE), fruitChances))));
    }

    protected void dropNamedContainer(Block block) {
        add(block, this::createNameableBlockEntityTable);
    }

    protected void dropSelf(Supplier<Block>... block) {
        for (Supplier<Block> b : block) {
            dropSelf(b.get());
        }
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        this.generatedLootTables.add(block);
        this.map.put(block.getLootTable(), builder);
    }

    protected void otherWhenSilkTouch(Block pBlock, Block pOther) {
        this.add(pBlock, createSilkTouchOnlyTable(pOther));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return generatedLootTables;
    }
}
