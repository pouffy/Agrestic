package io.github.pouffy.agrestic.common.event;

import com.pouffydev.krystal_core.foundation.event.AddBoatTypesEvent;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.fluid.transfer.FluidTransferSyncPacket;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticFluids;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = Agrestic.MODID)
public class RegistryEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

    }

    @SubscribeEvent
    public static void registerExtra(RegisterEvent event) {
        register(event, Registries.FLUID, AgresticFluids::registerAll);
        register(event, NeoForgeRegistries.Keys.FLUID_TYPES, AgresticFluids.Types::registerAll);
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Agrestic.MODID).versioned("1.0");

        registrar.playToClient(FluidTransferSyncPacket.TYPE, FluidTransferSyncPacket.STREAM_CODEC, FluidTransferSyncPacket::handle);
    }

    public static <T> void register(RegisterEvent event, ResourceKey<Registry<T>> registry, Runnable registerMethod) {
        if (event.getRegistryKey() == registry)
            registerMethod.run();
    }
}
