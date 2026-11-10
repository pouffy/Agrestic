package io.github.pouffy.agrestic.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.pouffy.agrestic.common.block.entity.FluidBarrelBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.neoforge.fluids.FluidStack;
import slimeknights.mantle.client.render.FluidCuboid;
import slimeknights.mantle.client.render.FluidRenderer;

import java.util.List;

public class FluidBarrelRenderer implements BlockEntityRenderer<FluidBarrelBlockEntity> {

    public FluidBarrelRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(FluidBarrelBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        int capacity = blockEntity.getTank().getCapacity();
        FluidStack fluid = blockEntity.getFluidStack();
        List<FluidCuboid> fluids = FluidCuboid.REGISTRY.get(blockEntity.getBlockState(), List.of());
        if (!fluid.isEmpty()) {
            FluidRenderer.renderScaledCuboid(ms, buffer, fluids.getFirst(), fluid, 0, capacity, light, false);
        }
    }
}
