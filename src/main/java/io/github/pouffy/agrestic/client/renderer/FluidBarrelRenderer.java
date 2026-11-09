package io.github.pouffy.agrestic.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.pouffy.agrestic.common.block.entity.FluidBarrelBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3f;
import slimeknights.mantle.client.render.FluidCuboid;
import slimeknights.mantle.client.render.FluidRenderer;

public class FluidBarrelRenderer implements BlockEntityRenderer<FluidBarrelBlockEntity> {

    public FluidBarrelRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(FluidBarrelBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        int capacity = blockEntity.getTank().getCapacity();
        FluidStack fluid = blockEntity.getFluidStack();
        if (!fluid.isEmpty()) {
            light = FluidRenderer.withBlockLight(light, fluid.getFluid().getFluidType().getLightLevel(fluid));
            IClientFluidTypeExtensions client = IClientFluidTypeExtensions.of(fluid.getFluid());
            TextureAtlasSprite still = FluidRenderer.getBlockSprite(client.getStillTexture(fluid));
            TextureAtlasSprite flowing = FluidRenderer.getBlockSprite(client.getFlowingTexture(fluid));
            int color = client.getTintColor(fluid);
            Vector3f from = new Vector3f(4f / 16f, 2f / 16f, 4f / 16f);
            Vector3f to = new Vector3f(12f / 16f, (2f / 16f) + ((fluid.getAmount()) / (float)capacity) * (12f / 16f), 12f / 16f);
            ms.pushPose();
            FluidRenderer.renderCuboid(ms, buffer.getBuffer(RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS)), FluidCuboid.builder().face(Direction.UP).build(), still, flowing, from, to, color, light, false);
            ms.popPose();
        }
    }
}
