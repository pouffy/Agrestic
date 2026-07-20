package io.github.pouffy.agrestic.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface BoozeConsumptionEffect {
    void onDrank(Level level, Player player, ItemStack stack);
}
