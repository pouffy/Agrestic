package io.github.pouffy.agrestic.init;

import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemDefinition;
import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemRegistryHelper;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.item.AgresticFoodItem;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.Level;

public class AgresticItems {
    public static final ItemRegistryHelper HELPER = Agrestic.INSTANCE.itemRegistryHelper;

    public static final ItemDefinition<SignItem> OLIVE_SIGN = HELPER.register("olive_sign", () -> new SignItem(new Item.Properties().stacksTo(16), AgresticBlocks.OLIVE.sign().get(), AgresticBlocks.OLIVE.wallSign().get()));
    public static final ItemDefinition<HangingSignItem> OLIVE_HANGING_SIGN = HELPER.register("olive_hanging_sign", () -> new HangingSignItem(AgresticBlocks.OLIVE.hangingSign().get(), AgresticBlocks.OLIVE.hangingWallSign().get(), new Item.Properties().stacksTo(16)));

    public static final ItemDefinition<SignItem> IRONWOOD_SIGN = HELPER.register("ironwood_sign", () -> new SignItem(new Item.Properties().stacksTo(16), AgresticBlocks.IRONWOOD.sign().get(), AgresticBlocks.IRONWOOD.wallSign().get()));
    public static final ItemDefinition<HangingSignItem> IRONWOOD_HANGING_SIGN = HELPER.register("ironwood_hanging_sign", () -> new HangingSignItem(AgresticBlocks.IRONWOOD.hangingSign().get(), AgresticBlocks.IRONWOOD.hangingWallSign().get(), new Item.Properties().stacksTo(16)));

    public static final ItemDefinition<Item> TALLOW = HELPER.register("tallow", Item::new);
    public static final ItemDefinition<Item> TINY_IRON_DUST = HELPER.register("tiny_iron_dust", Item::new);
    public static final ItemDefinition<Item> GOLD_DUST = HELPER.register("gold_dust", Item::new);

    //public static final ItemDefinition<AgresticFoodItem> IRONBERRIES = HELPER.register("ironberries", (p) -> new AgresticFoodItem(p.food(AgresticFoodValues.IRONBERRIES)));
    public static final ItemDefinition<AgresticFoodItem> OLIVES = HELPER.register("olives", (p) -> new AgresticFoodItem(p.food(AgresticFoodValues.OLIVES)));
    public static final ItemDefinition<AgresticFoodItem> CHILLI_PEPPER = HELPER.register("chilli_pepper", (p) -> new AgresticFoodItem(p.food(AgresticFoodValues.CHILLI_PEPPER)) {
        @Override
        public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
            if (!world.isClientSide()) {
                RandomSource random = world.getRandom();
                if (random.nextInt(24) == 0) {
                    user.hurt(world.damageSources().onFire(), 1.0F);
                }
            }
            return super.finishUsingItem(stack, world, user);
        }
    });

    public static void staticInit() {}
}
