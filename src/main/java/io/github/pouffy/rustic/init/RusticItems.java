package io.github.pouffy.rustic.init;

import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemDefinition;
import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemRegistryHelper;
import io.github.pouffy.rustic.Rustic;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;

public class RusticItems {
    public static final ItemRegistryHelper HELPER = Rustic.getRegistryHelper().getItemHelper();

    public static final ItemDefinition<SignItem> OLIVE_SIGN = HELPER.register("olive_sign", () -> new SignItem(new Item.Properties().stacksTo(16), RusticBlocks.OLIVE.sign().get(), RusticBlocks.OLIVE.wallSign().get()));
    public static final ItemDefinition<HangingSignItem> OLIVE_HANGING_SIGN = HELPER.register("olive_hanging_sign", () -> new HangingSignItem(RusticBlocks.OLIVE.hangingSign().get(), RusticBlocks.OLIVE.hangingWallSign().get(), new Item.Properties().stacksTo(16)));

    public static final ItemDefinition<SignItem> IRONWOOD_SIGN = HELPER.register("ironwood_sign", () -> new SignItem(new Item.Properties().stacksTo(16), RusticBlocks.IRONWOOD.sign().get(), RusticBlocks.IRONWOOD.wallSign().get()));
    public static final ItemDefinition<HangingSignItem> IRONWOOD_HANGING_SIGN = HELPER.register("ironwood_hanging_sign", () -> new HangingSignItem(RusticBlocks.IRONWOOD.hangingSign().get(), RusticBlocks.IRONWOOD.hangingWallSign().get(), new Item.Properties().stacksTo(16)));


    public static void staticInit() {}
}
