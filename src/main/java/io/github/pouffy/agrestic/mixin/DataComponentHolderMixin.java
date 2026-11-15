package io.github.pouffy.agrestic.mixin;

import io.github.pouffy.agrestic.init.AgresticDataComponents;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unchecked")
@Mixin(DataComponentHolder.class)
public interface DataComponentHolderMixin {

    @Inject(method = "get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;", at = @At("RETURN"), cancellable = true)
    default <T> void onGetComponent(DataComponentType<? extends T> component, CallbackInfoReturnable<T> cir) {
        if (component == DataComponents.FOOD && cir.getReturnValue() != null) {
            DataComponentHolder self = (DataComponentHolder) this;

            if (self instanceof ItemStack stack) {
                if (stack.has(AgresticDataComponents.OLIVE_OILED)) {
                    FoodProperties originalFood = (FoodProperties) cir.getReturnValue();
                    FoodProperties modifiedFood = agrestic$modifyFoodComponent(originalFood);
                    cir.setReturnValue((T) modifiedFood);
                }
            }
        }
    }

    @Unique
    default FoodProperties agrestic$modifyFoodComponent(FoodProperties original) {
        return new FoodProperties(original.nutrition() + 2,
                Math.max(0.1f, original.saturation() * 1.5f),
                original.canAlwaysEat(),
                original.eatSeconds(),
                original.usingConvertsTo(),
                original.effects()
        );
    }
}
