package io.github.pouffy.agrestic.mixin.appleskin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.pouffy.agrestic.common.item.BoozeBottleItem;
import io.github.pouffy.agrestic.mixin.plugin.annotation.IfModPresent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import squeek.appleskin.helpers.FoodHelper;

@IfModPresent("appleskin")
@Mixin(FoodHelper.class)
public class FoodHelperMixin {

    @WrapMethod(method = "isFood(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)Z")
    private static boolean isFood(ItemStack itemStack, Player player, Operation<Boolean> original) {
        if (itemStack.getItem() instanceof BoozeBottleItem) return true;
        return original.call(itemStack, player);
    }
}
