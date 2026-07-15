package io.github.pouffy.agrestic.network;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.effect.FirePowerEffect;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record FirePowerAttackPayload() implements CustomPacketPayload {
    public static final Type<FirePowerAttackPayload> TYPE = new Type<>(Agrestic.location("fire_power_attack"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FirePowerAttackPayload> STREAM_CODEC = StreamCodec.unit(new FirePowerAttackPayload());

    public void handle(IPayloadContext context) {
        Player player = context.player();
        context.enqueueWork(() -> FirePowerEffect.doFirePowerAttack(player));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
