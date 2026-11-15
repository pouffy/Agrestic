package io.github.pouffy.agrestic.compat.appleskin;

import io.github.pouffy.agrestic.common.item.BoozeBottleItem;
import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import squeek.appleskin.api.event.FoodValuesEvent;

public class AppleskinIntegration {

    @SubscribeEvent
    public static void boozeFoodValues(FoodValuesEvent event) {
        Player player = event.player;
        ItemStack itemStack = event.itemStack;
        FoodProperties foodProperties = calculateBoozeProps(itemStack, player);
        if (foodProperties != null) {
            event.modifiedFoodProperties = foodProperties;
        }


    }

    @SubscribeEvent
    public static void oiledFoodValues(FoodValuesEvent event) {
        ItemStack stack = event.itemStack;

        FoodProperties properties = stack.get(DataComponents.FOOD);

        if (stack.has(AgresticDataComponents.OLIVE_OILED)) {
            if (properties != null) {
                event.modifiedFoodProperties = properties;
            }
        }
    }

    private static FoodProperties calculateBoozeProps(ItemStack stack, Player player) {
        if (stack.getItem() instanceof BoozeBottleItem boozeBottleItem) {
            return boozeBottleItem.getConsumptionProperties().apply(new BoozeBottleItem.BoozeConsumptionContext(player.level(), player, stack.getOrDefault(AgresticDataComponents.QUALITY, BrewingBarrelRecipe.DEFAULT_QUALITY)));
        }
        return null;
    }
}
