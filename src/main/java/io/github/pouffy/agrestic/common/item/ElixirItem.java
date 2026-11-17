package io.github.pouffy.agrestic.common.item;

import io.github.pouffy.agrestic.init.AgresticEffects;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.*;

public class ElixirItem extends Item {
    public ElixirItem(Properties properties) {
        super(properties);
    }

    public static ItemStack withEffect(Holder<MobEffect> effect, int duration, int amplifier) {
        ItemStack stack = AgresticItems.ELIXIR.stack();
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.of(Potions.WATER),
                Optional.empty(), List.of(new MobEffectInstance(effect, duration, amplifier))));
        return stack;
    }

    public ItemStack getDefaultInstance() {
        ItemStack itemstack = super.getDefaultInstance();
        itemstack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        return itemstack;
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        Player player = entityLiving instanceof Player ? (Player)entityLiving : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
        }

        if (!level.isClientSide) {
            PotionContents potioncontents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            potioncontents.forEachEffect((instance) -> {
                if (instance.getEffect().value().isInstantenous()) {
                    instance.getEffect().value().applyInstantenousEffect(player, player, entityLiving, instance.getAmplifier(), 1.0F);
                } else {
                    entityLiving.addEffect(instance);
                }

            });
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);
        }

        if (player == null || !player.hasInfiniteMaterials()) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (player != null) {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        entityLiving.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public Component getName(ItemStack stack) {
        MutableComponent name = Component.translatable("agrestic.elixir.prefix");
        int i = 0;
        PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);

        if (potionContents != null) {
            for (MobEffectInstance effect : potionContents.customEffects()) {
                name.append(Component.translatable(effect.getDescriptionId()));
                if (i < potionContents.customEffects().size() - 1) {
                    name.append(Component.translatable("agrestic.elixir.separator"));
                }
                i++;
            }
        }
        return name;
    }

    public String getDescriptionId(ItemStack stack) {
        return Potion.getName(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion(), this.getDescriptionId() + ".effect.");
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);
        if (potioncontents != null) {
            Objects.requireNonNull(tooltipComponents);
            potioncontents.addPotionTooltip(tooltipComponents::add, 1.0F, context.tickRate());
        }
    }

    //TODO: Dynamically build this list from recipes
    public static List<ItemStack> getElixirs() {
        List<ItemStack> elixirs = new ArrayList<>();
        elixirs.add(withEffect(MobEffects.HEAL, 0, 0));
        elixirs.add(withEffect(MobEffects.HEAL, 0, 1));
        elixirs.add(withEffect(MobEffects.REGENERATION, 900, 0));
        elixirs.add(withEffect(MobEffects.REGENERATION, 1800, 0));
        elixirs.add(withEffect(MobEffects.REGENERATION, 440, 1));
        elixirs.add(withEffect(MobEffects.WITHER, 900, 0));
        elixirs.add(withEffect(MobEffects.WITHER, 1800, 0));
        elixirs.add(withEffect(MobEffects.WITHER, 440, 1));
        elixirs.add(withEffect(MobEffects.NIGHT_VISION, 3600, 0));
        elixirs.add(withEffect(MobEffects.NIGHT_VISION, 9600, 0));
        elixirs.add(withEffect(MobEffects.MOVEMENT_SPEED, 3600, 0));
        elixirs.add(withEffect(MobEffects.MOVEMENT_SPEED, 9600, 0));
        elixirs.add(withEffect(MobEffects.MOVEMENT_SPEED, 1800, 1));
        elixirs.add(withEffect(MobEffects.FIRE_RESISTANCE, 3600, 0));
        elixirs.add(withEffect(MobEffects.FIRE_RESISTANCE, 9600, 0));
        elixirs.add(withEffect(MobEffects.HEALTH_BOOST, 3600, 0));
        elixirs.add(withEffect(MobEffects.HEALTH_BOOST, 9600, 0));
        elixirs.add(withEffect(MobEffects.HEALTH_BOOST, 1800, 1));
        elixirs.add(withEffect(MobEffects.DIG_SPEED, 3600, 0));
        elixirs.add(withEffect(MobEffects.DIG_SPEED, 9600, 0));
        elixirs.add(withEffect(MobEffects.DIG_SPEED, 1800, 1));
        elixirs.add(withEffect(MobEffects.DAMAGE_BOOST, 3600, 0));
        elixirs.add(withEffect(MobEffects.DAMAGE_BOOST, 9600, 0));
        elixirs.add(withEffect(MobEffects.DAMAGE_BOOST, 1800, 1));
        elixirs.add(withEffect(AgresticEffects.IRONSKIN, 3600, 0));
        elixirs.add(withEffect(AgresticEffects.IRONSKIN, 9600, 0));
        elixirs.add(withEffect(AgresticEffects.IRONSKIN, 1800, 1));
        elixirs.add(withEffect(MobEffects.SLOW_FALLING, 3600, 0));
        elixirs.add(withEffect(MobEffects.SLOW_FALLING, 9600, 0));
        elixirs.add(withEffect(AgresticEffects.BLAZING_TRAIL, 3600, 0));
        elixirs.add(withEffect(AgresticEffects.BLAZING_TRAIL, 9600, 0));

        return elixirs;
    }
}
