package io.github.pouffy.rustic;

import com.pouffydev.krystal_core.foundation.registry.CreativeTabRegistryHelper;
import com.pouffydev.krystal_core.foundation.registry.RegistryHelper;
import io.github.pouffy.rustic.init.RusticItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;

public class RusticCreativeTab {
    private static final CreativeTabRegistryHelper HELPER = Rustic.getRegistryHelper().getCreativeTabHelper();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = HELPER.registerTabSearchBar("main", Items.DIRT.builtInRegistryHolder(), (params, output) -> {
                for (var definition : Rustic.INSTANCE.itemRegistryHelper.ITEM_DEFINITIONS) {
                    if (definition.get() instanceof BlockItem) continue;
                    output.accept(definition.get());
                }
                for (var definition : Rustic.INSTANCE.blockRegistryHelper.BLOCK_DEFINITIONS) {
                    if (!definition.hasItem()) continue;
                    output.accept(definition.get());
                }
            },
            builder -> RegistryHelper.noAction());

    public static void staticInit() {

    }
}
