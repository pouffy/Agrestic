package io.github.pouffy.agrestic;

import com.pouffydev.krystal_core.foundation.registry.CreativeTabRegistryHelper;
import com.pouffydev.krystal_core.foundation.registry.RegistryHelper;
import com.pouffydev.krystal_core.foundation.utility.CreativeTabManager;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import io.github.pouffy.agrestic.init.AgresticFluids;
import io.github.pouffy.agrestic.init.AgresticItems;
import lombok.Setter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AgresticCreativeTab {
    private static final CreativeTabRegistryHelper HELPER = Agrestic.getRegistryHelper().getCreativeTabHelper();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AGRICULTURE = registerTab("agriculture", () -> AgresticItems.OLIVES, AgresticCreativeTab::popAgriculture, RegistryHelper.noAction());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DECORATION = registerTab("decoration", AgresticBlocks.OLIVE::planks, AgresticCreativeTab::popDecoration, RegistryHelper.noAction());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ALCHEMY = registerTab("alchemy", () -> AgresticBlocks.ALOE_VERA, AgresticCreativeTab::popAlchemy, RegistryHelper.noAction());

    public static void staticInit() {}

    private static void popAgriculture(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        Consumer<ItemStack> consumer = output::accept;
        ///consumer.accept(AgresticItems.CATALOG.stack());
        ///consumer.accept(AgresticBlocks.FERTILE_SOIL.stack());

        for (AgresticBlocks.Woodset woodset : AgresticBlocks.WOODSETS) {
            consumer.accept(woodset.log().stack());
            consumer.accept(woodset.wood().stack());
            consumer.accept(woodset.strippedLog().stack());
            consumer.accept(woodset.strippedWood().stack());
            consumer.accept(woodset.leaves().stack());
        }
        consumer.accept(AgresticBlocks.APPLE_LEAVES.stack());
        consumer.accept(AgresticBlocks.APPLE_SAPLING.stack());
        consumer.accept(AgresticBlocks.OLIVE_SAPLING.stack());
        consumer.accept(AgresticBlocks.IRONWOOD_SAPLING.stack());
        consumer.accept(AgresticBlocks.CRUSHING_TUB.stack());
        consumer.accept(AgresticBlocks.EVAPORATING_BASIN.stack());
        consumer.accept(AgresticBlocks.FLUID_BARREL.stack());
        ///consumer.accept(AgresticBlocks.UNFIRED_DRYING_BASIN.stack());
        ///consumer.accept(AgresticBlocks.STAKE.stack());
        ///consumer.accept(AgresticBlocks.ROPE.stack());
        //Seeds
        consumer.accept(AgresticItems.CHILLI_PEPPER_SEEDS.stack());
        ///consumer.accept(AgresticItems.TOMATO_SEEDS.stack());
        ///consumer.accept(AgresticItems.GRAPE_SEEDS.stack());
        consumer.accept(AgresticBlocks.APPLE_SEEDS.stack());
        //Crops
        consumer.accept(AgresticItems.OLIVES.stack());
        consumer.accept(AgresticItems.IRONBERRIES.stack());
        consumer.accept(AgresticItems.GRAPES.stack());
        consumer.accept(AgresticItems.TOMATO.stack());
        consumer.accept(AgresticItems.CHILLI_PEPPER.stack());
        consumer.accept(AgresticItems.GHOST_PEPPER.stack());
        //Ingredients
        consumer.accept(AgresticItems.TALLOW.stack());
        consumer.accept(AgresticItems.GOLD_DUST.stack());
        consumer.accept(AgresticItems.TINY_GOLD_DUST.stack());
        consumer.accept(AgresticItems.IRON_DUST.stack());
        consumer.accept(AgresticItems.TINY_IRON_DUST.stack());
        ///consumer.accept(AgresticItems.TINY_GLOWSTONE_DUST.stack());
        //Drinks
        consumer.accept(AgresticItems.IRONBERRY_JUICE_BOTTLE.stack());
        consumer.accept(AgresticItems.VANTA_OIL_BOTTLE.stack());
        consumer.accept(AgresticItems.ALE_WORT_BOTTLE.stack());
        consumer.accept(AgresticItems.OLIVE_OIL_BOTTLE.stack());
        consumer.accept(AgresticItems.GOLDEN_APPLE_JUICE_BOTTLE.stack());
        consumer.accept(AgresticItems.APPLE_JUICE_BOTTLE.stack());
        consumer.accept(AgresticItems.SWEET_BERRY_JUICE_BOTTLE.stack());
        consumer.accept(AgresticItems.GRAPE_JUICE_BOTTLE.stack());
        //Buckets
        consumer.accept(AgresticFluids.IRONBERRY_JUICE.getBucket().getDefaultInstance());
        consumer.accept(AgresticFluids.VANTA_OIL.getBucket().getDefaultInstance());
        consumer.accept(AgresticFluids.ALE_WORT.getBucket().getDefaultInstance());
        consumer.accept(AgresticFluids.OLIVE_OIL.getBucket().getDefaultInstance());
        consumer.accept(AgresticFluids.GOLDEN_APPLE_JUICE.getBucket().getDefaultInstance());
        consumer.accept(AgresticFluids.APPLE_JUICE.getBucket().getDefaultInstance());
        consumer.accept(AgresticFluids.SWEET_BERRY_JUICE.getBucket().getDefaultInstance());
        consumer.accept(AgresticFluids.GRAPE_JUICE.getBucket().getDefaultInstance());
        consumer.accept(AgresticBlocks.BREWING_BARREL.stack());
        generateBooze(output, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
    }

    private static void popDecoration(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        Consumer<ItemStack> consumer = output::accept;
        for (AgresticBlocks.Woodset woodset : AgresticBlocks.WOODSETS) {
            consumer.accept(woodset.planks().stack());
            consumer.accept(woodset.stairs().stack());
            consumer.accept(woodset.slab().stack());
            consumer.accept(woodset.fence().stack());
            consumer.accept(woodset.fenceGate().stack());
            consumer.accept(woodset.door().stack());
            consumer.accept(woodset.trapdoor().stack());
            consumer.accept(woodset.pressurePlate().stack());
            consumer.accept(woodset.button().stack());
        }
        consumer.accept(AgresticBlocks.OLIVE.button().stack());
        consumer.accept(AgresticItems.OLIVE_SIGN.stack());
        consumer.accept(AgresticItems.OLIVE_HANGING_SIGN.stack());
        consumer.accept(AgresticItems.OLIVE_BOAT.stack());
        consumer.accept(AgresticItems.OLIVE_CHEST_BOAT.stack());
        consumer.accept(AgresticBlocks.IRONWOOD.button().stack());
        consumer.accept(AgresticItems.IRONWOOD_SIGN.stack());
        consumer.accept(AgresticItems.IRONWOOD_HANGING_SIGN.stack());
        consumer.accept(AgresticItems.IRONWOOD_BOAT.stack());
        consumer.accept(AgresticItems.IRONWOOD_CHEST_BOAT.stack());
    }

    private static void popAlchemy(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        Consumer<ItemStack> consumer = output::accept;
        consumer.accept(AgresticBlocks.ALOE_VERA.stack());
        consumer.accept(AgresticBlocks.BLOOD_ORCHID.stack());
        consumer.accept(AgresticBlocks.CHAMOMILE.stack());
        consumer.accept(AgresticItems.CLOUDSBLUFF.stack());
        consumer.accept(AgresticBlocks.COHOSH.stack());
        consumer.accept(AgresticBlocks.HORSETAIL.stack());
        consumer.accept(AgresticBlocks.VANTA_LILY.stack());
        consumer.accept(AgresticBlocks.WIND_THISTLE.stack());
        consumer.accept(AgresticItems.CORE_ROOT.stack());
        consumer.accept(AgresticItems.GINSENG.stack());
        consumer.accept(AgresticItems.MARSH_MALLOW.stack());
        consumer.accept(AgresticBlocks.MOONCAP.stack());
        consumer.accept(AgresticBlocks.DEATHSTALK.stack());
        ///consumer.accept(AgresticBlocks.BASIC_CONDENSER.stack());
        ///consumer.accept(AgresticBlocks.BASIC_RETORT.stack());
        ///consumer.accept(AgresticBlocks.ADVANCED_CONDENSER.stack());
        ///consumer.accept(AgresticBlocks.ADVANCED_RETORT.stack());
    }

    private static void generateBooze(CreativeModeTab.Output output, CreativeModeTab.TabVisibility tabVisibility) {
        Set<ItemStack> set = ItemStackLinkedSet.createTypeAndComponentsSet();
        var ironWine = new ItemStack(AgresticItems.IRON_WINE_BOTTLE.asItem());
        ironWine.set(AgresticDataComponents.QUALITY, 0.75f);
        var ale = new ItemStack(AgresticItems.ALE_BOTTLE.asItem());
        ale.set(AgresticDataComponents.QUALITY, 0.75f);
        var cider = new ItemStack(AgresticItems.CIDER_BOTTLE.asItem());
        cider.set(AgresticDataComponents.QUALITY, 0.75f);
        var mead = new ItemStack(AgresticItems.MEAD_BOTTLE.asItem());
        mead.set(AgresticDataComponents.QUALITY, 0.75f);
        var ambrosia = new ItemStack(AgresticItems.AMBROSIA_BOTTLE.asItem());
        ambrosia.set(AgresticDataComponents.QUALITY, 0.75f);
        var sweetBerryWine = new ItemStack(AgresticItems.SWEET_BERRY_WINE_BOTTLE.asItem());
        sweetBerryWine.set(AgresticDataComponents.QUALITY, 0.75f);
        var wine = new ItemStack(AgresticItems.WINE_BOTTLE.asItem());
        wine.set(AgresticDataComponents.QUALITY, 0.75f);
        set.add(ironWine);
        set.add(ale);
        set.add(cider);
        set.add(mead);
        set.add(ambrosia);
        set.add(sweetBerryWine);
        set.add(wine);
        output.acceptAll(set, tabVisibility);
    }

    public static DeferredHolder<CreativeModeTab, CreativeModeTab> registerTab(String name, Supplier<ItemLike> icon, BiConsumer<CreativeModeTab.ItemDisplayParameters, CreativeModeTab.Output> displayItems, Consumer<CreativeModeTab.Builder> additionalProperties) {
        return HELPER.TABS.register(name, (id) -> {
            CreativeModeTab.Builder builder = CreativeModeTab.builder();
            CreativeModeTab.Builder var10000 = builder.title(Component.translatable(id.toLanguageKey("itemGroup"))).icon(() -> new ItemStack(icon.get()));
            Objects.requireNonNull(displayItems);
            var10000.displayItems(displayItems::accept);
            additionalProperties.accept(builder);
            return builder.build();
        });
    }
}
