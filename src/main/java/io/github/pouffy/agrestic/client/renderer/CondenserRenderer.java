package io.github.pouffy.agrestic.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.pouffy.agrestic.AgresticClient;
import io.github.pouffy.agrestic.common.block.CondenserBlock;
import io.github.pouffy.agrestic.common.block.RetortBlock;
import io.github.pouffy.agrestic.common.block.entity.CondenserBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CondenserRenderer implements BlockEntityRenderer<CondenserBlockEntity> {

    public CondenserRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(CondenserBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        Level world = blockEntity.getLevel();
        if (world == null) return;
        if (!blockEntity.isController()) return;
        BlockState state = blockEntity.getBlockState();
        BlockPos pos = blockEntity.getBlockPos();
        BlockPos playerPos = Minecraft.getInstance().player.blockPosition();
        int dx = playerPos.getX() - pos.getX();
        int dz = playerPos.getZ() - pos.getZ();
        VertexConsumer vertexBuilder = buffer.getBuffer(AgresticClient.ERROR_BLOCK);
        if ((dx * dx + dz * dz) < 512) {
            if (!blockEntity.getRetorts().getFirst()) {
                BlockPos leftPos = pos.relative(state.getValue(CondenserBlock.FACING).getClockWise());
                LevelRenderer.renderVoxelShape(ms, vertexBuilder, RetortBlock.SHAPE, leftPos.getX() - pos.getX(), leftPos.getY() - pos.getY(), leftPos.getZ() - pos.getZ(), 1f, 0f, 0f, 0.5f, true);
            }
            if (!blockEntity.getRetorts().getSecond()) {
                BlockPos rightPos = pos.relative(state.getValue(CondenserBlock.FACING).getCounterClockWise());
                LevelRenderer.renderVoxelShape(ms, vertexBuilder, RetortBlock.SHAPE, rightPos.getX() - pos.getX(), rightPos.getY() - pos.getY(), rightPos.getZ() - pos.getZ(), 1f, 0f, 0f, 0.5f, true);
            }
        }
    }

    @Override
    public boolean shouldRenderOffScreen(CondenserBlockEntity blockEntity) {
        return true;
    }

    @Override
    public boolean shouldRender(CondenserBlockEntity blockEntity, Vec3 cameraPos) {
        return Vec3.atCenterOf(blockEntity.getBlockPos()).multiply(3, 3, 3).closerThan(cameraPos.multiply(3, 3, 3), this.getViewDistance());
    }
}
