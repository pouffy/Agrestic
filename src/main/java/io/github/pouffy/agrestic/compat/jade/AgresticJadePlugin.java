package io.github.pouffy.agrestic.compat.jade;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.block.BrewingBarrelBlock;
import io.github.pouffy.agrestic.common.block.EvaporatingBasinBlock;
import io.github.pouffy.agrestic.compat.jade.provider.BrewingBarrelProgressProvider;
import io.github.pouffy.agrestic.compat.jade.provider.EvaporatingBasinProvider;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class AgresticJadePlugin implements IWailaPlugin {
    public AgresticJadePlugin() {
    }

    public static final ResourceLocation brewingBarrel = Agrestic.location("brewing_barrel");
    public static final ResourceLocation evaporatingBasin = Agrestic.location("evaporating_basin");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(BrewingBarrelProgressProvider.INSTANCE, BrewingBarrelBlock.class);
        registration.registerBlockDataProvider(EvaporatingBasinProvider.INSTANCE, EvaporatingBasinBlock.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(BrewingBarrelProgressProvider.INSTANCE, BrewingBarrelBlock.class);
        registration.registerBlockComponent(EvaporatingBasinProvider.INSTANCE, EvaporatingBasinBlock.class);
    }
}
