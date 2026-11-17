package io.github.pouffy.agrestic;

import io.github.pouffy.agrestic.client.ClientTextHelper;
import io.github.pouffy.agrestic.client.renderer.AgresticBoatRenderer;
import io.github.pouffy.agrestic.client.renderer.FullmetalRenderLayer;
import io.github.pouffy.agrestic.client.ui.BrewingBarrelScreen;
import io.github.pouffy.agrestic.client.ui.CondenserScreen;
import io.github.pouffy.agrestic.common.entity.CustomBoat;
import io.github.pouffy.agrestic.common.item.BoozeBottleItem;
import io.github.pouffy.agrestic.common.item.BoozeItemColor;
import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
import io.github.pouffy.agrestic.init.*;
import mezz.jei.library.color.ColorHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import slimeknights.mantle.fluid.MantleFluids;
import slimeknights.mantle.fluid.UnplaceableFluid;
import slimeknights.mantle.fluid.texture.ClientTextureFluidType;
import slimeknights.mantle.registration.object.FluidObject;

import java.util.List;

@Mod(value = Agrestic.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Agrestic.MODID, value = Dist.CLIENT)
public class AgresticClient {

    public AgresticClient(ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        RenderType translucent = RenderType.translucent();
        AgresticFluids.REGISTERED_FLUIDS.forEach((fluid) -> {
            ItemBlockRenderTypes.setRenderLayer(fluid.get(), translucent);
        });
    }

    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        AgresticFluids.REGISTERED_FLUIDS.forEach((fluid) -> textureExtension(event, fluid));
    }

    @SubscribeEvent
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        addPlayerLayer(event, PlayerSkin.Model.WIDE);
        addPlayerLayer(event, PlayerSkin.Model.SLIM);

        BuiltInRegistries.ENTITY_TYPE.forEach((type) -> {
            EntityRenderer renderer = event.getRenderer(type);
            if(renderer instanceof LivingEntityRenderer livingEntityRenderer)
                livingEntityRenderer.addLayer(new FullmetalRenderLayer(livingEntityRenderer));
        });
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (CustomBoat.Type type : CustomBoat.Type.values()) {
            var layer = new ModelLayerLocation(Agrestic.location(type.getModelLocation()), "main");
            var chestLayer = new ModelLayerLocation(Agrestic.location(type.getChestModelLocation()), "main");
            event.registerLayerDefinition(layer, BoatModel::createBodyModel);
            event.registerLayerDefinition(chestLayer, ChestBoatModel::createBodyModel);
        }
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AgresticEntities.BOAT.get(), context -> new AgresticBoatRenderer(context, false));
        event.registerEntityRenderer(AgresticEntities.CHEST_BOAT.get(), context -> new AgresticBoatRenderer(context, true));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addPlayerLayer(EntityRenderersEvent.AddLayers event, PlayerSkin.Model skin) {
        EntityRenderer<? extends Player> renderer = event.getSkin(skin);
        if (renderer instanceof LivingEntityRenderer livingRenderer) {
            livingRenderer.addLayer(new FullmetalRenderLayer(livingRenderer));
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item.TooltipContext context = event.getContext();
        List<Component> tooltip = event.getToolTip();
        TooltipFlag flag = event.getFlags();
        if (stack.getCapability(Capabilities.FluidHandler.ITEM) instanceof FluidBucketWrapper wrapper) {
            tooltip.add(Component.translatable("ui.agrestic.tooltip.fluid", wrapper.getFluid().getHoverName(), wrapper.getFluid().getAmount(), 1000));
        }
        if (stack.has(AgresticDataComponents.QUALITY)) {
            tooltip.add(ClientTextHelper.getQualityTooltip(stack.getOrDefault(AgresticDataComponents.QUALITY, BrewingBarrelRecipe.DEFAULT_QUALITY)));
        }
        if (stack.is(Items.HONEY_BOTTLE)) {
            var effect = new MobEffectInstance(MobEffects.REGENERATION, 75, 0, false, false, true);
            MutableComponent text = Component.translatable(effect.getDescriptionId());

            if (effect.getAmplifier() > 0) {
                text = Component.translatable("potion.withAmplifier", text, Component.translatable("potion.potency." + effect.getAmplifier()));
            }

            if (!effect.endsWithin(20)) {
                text = Component.translatable("potion.withDuration", text, MobEffectUtil.formatDuration(effect, 1.0F, context.tickRate()));
            }

            text.withStyle(effect.getEffect().value().getCategory().getTooltipFormatting());
            tooltip.add(text);
        }
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(AgresticMenuTypes.BREWING_BARREL.get(), BrewingBarrelScreen::new);
        event.register(AgresticMenuTypes.BASIC_CONDENSER.get(), CondenserScreen::new);
    }


    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {BlockState blockstate = ((BlockItem)stack.getItem()).getBlock().defaultBlockState();return event.getBlockColors().getColor(blockstate, null, null, tintIndex);},
                AgresticBlocks.APPLE_LEAVES.get()
        );

        event.register(
                (stack, index) -> index == 1 ? stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor() : -1,
                AgresticItems.ELIXIR
        );

        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof BoozeBottleItem) {
                event.register(new BoozeItemColor(), item);
            }
        }
    }

    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, tintGetter, pos, tintIndex) -> tintGetter != null && pos != null ? BiomeColors.getAverageFoliageColor(tintGetter, pos) : FoliageColor.getDefaultColor(),
                AgresticBlocks.APPLE_LEAVES.get()
        );
    }

    private static void textureExtension(RegisterClientExtensionsEvent event, FluidObject<?> fluid) {
        if (fluid.isBound()) {
            FluidType type = fluid.getType();
            event.registerFluidType(new ClientTextureFluidType(type), type);
        }
    }

    public static void spawnAlchemySmokeFX(int brewTime, double x, double y, double z, double xVel, double yVel, double zVel) {
        Particle smoke = Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.SMOKE, x, y, z, xVel, yVel, zVel);
        float colorScale = 0.4F;
        float r = colorScale * ((Mth.sin(30 + brewTime / 16F) * 0.5F) + 0.5F);
        float g = colorScale * ((Mth.sin(brewTime / 16F) * 0.5F) + 0.5F);
        float b = colorScale * ((Mth.sin(60 + brewTime / 16F) * 0.5F) + 0.5F);
        smoke.setColor(r, g, b);
        smoke.setLifetime(10);
        Minecraft.getInstance().particleEngine.add(smoke);
    }
}
