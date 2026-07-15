package io.github.pouffy.agrestic.common.effect;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticAttachmentTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import javax.annotation.Nullable;

public class IronskinEffect extends AgresticEffect {
    public IronskinEffect() {
        super(MobEffectCategory.BENEFICIAL, 16777148);
        addAttributeModifier(Attributes.ARMOR, Agrestic.location("ironskin/armor"), 3, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(Attributes.ARMOR_TOUGHNESS, Agrestic.location("ironskin/armor_toughness"), 2, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void onApplication(@Nullable MobEffectInstance effectInstance, @Nullable Entity source, LivingEntity entity, int amplifier) {
        entity.setData(AgresticAttachmentTypes.IRON_SKIN, new IronSkin(true));
    }

    @Override
    public void onExpiry(MobEffectInstance effectInstance, LivingEntity entity) {
        entity.removeData(AgresticAttachmentTypes.IRON_SKIN);
    }
}
