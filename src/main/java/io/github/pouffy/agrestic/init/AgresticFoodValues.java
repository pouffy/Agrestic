package io.github.pouffy.agrestic.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class AgresticFoodValues {
    public static final FoodProperties OLIVES = new FoodProperties.Builder().nutrition(1).saturationModifier(0.4f).effect(new MobEffectInstance(MobEffects.CONFUSION,200,1,true, false, false),0.95f).build();
    public static final FoodProperties CHILLI_PEPPER = new FoodProperties.Builder().nutrition(3).saturationModifier(0.4f).alwaysEdible().effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,400,0,true, false, true),0.95f).build();
}
