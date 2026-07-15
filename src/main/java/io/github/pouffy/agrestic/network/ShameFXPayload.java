package io.github.pouffy.agrestic.network;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ShameFXPayload(int count, Vec3 pos, Vec3 movement, float width, float height) implements CustomPacketPayload {
    public static RandomSource rand = RandomSource.create();

    public static final Type<ShameFXPayload> TYPE = new Type<>(Agrestic.location("shame_fx"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShameFXPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ShameFXPayload::count,
            ByteBufCodecs.fromCodec(Vec3.CODEC),
            ShameFXPayload::pos,
            ByteBufCodecs.fromCodec(Vec3.CODEC),
            ShameFXPayload::movement,
            ByteBufCodecs.FLOAT,
            ShameFXPayload::width,
            ByteBufCodecs.FLOAT,
            ShameFXPayload::height,
            ShameFXPayload::new
    );

    public void handle(IPayloadContext context) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world.isClientSide()) {
            for (int i = 0; i < this.count; i++) {
                double x = this.pos.x + this.movement.x;
                double y = this.pos.y + (this.height * Math.random()) + this.movement.y;
                double z = this.pos.z + this.movement.z;
                switch (ShameFXPayload.rand.nextInt(4)) {
                    case 3:
                        x += this.width * (ShameFXPayload.rand.nextDouble() - 0.5);
                        z -= this.width * 0.5;
                        break;
                    case 2:
                        x += this.width * (ShameFXPayload.rand.nextDouble() - 0.5);
                        z += this.width * 0.5;
                        break;
                    case 1:
                        z += this.width * (ShameFXPayload.rand.nextDouble() - 0.5);
                        x -= this.width * 0.5;
                        break;
                    default:
                        z += this.width * (ShameFXPayload.rand.nextDouble() - 0.5);
                        x += this.width * 0.5;
                        break;
                }
                world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, AgresticItems.TOMATO.stack()), true, x, y, z, this.movement().x(), this.movement().y(), this.movement().z());
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
