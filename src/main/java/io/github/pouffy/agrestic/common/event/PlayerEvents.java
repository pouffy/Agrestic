package io.github.pouffy.agrestic.common.event;

import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

@EventBusSubscriber(modid = Agrestic.MODID)
public class PlayerEvents {

    @SubscribeEvent
    public static void finishUsingItem(final LivingEntityUseItemEvent.Finish event) {
        if (event.getItem().is(Items.HONEY_BOTTLE)) {
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.REGENERATION, 75, 0, false, false, true));
        }
    }
}
