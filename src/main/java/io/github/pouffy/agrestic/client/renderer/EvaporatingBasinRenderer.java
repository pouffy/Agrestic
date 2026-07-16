package io.github.pouffy.agrestic.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.pouffy.agrestic.client.ClientFluidHelper;
import io.github.pouffy.agrestic.client.helper.VecHelper;
import io.github.pouffy.agrestic.client.helper.pose.TransformStack;
import io.github.pouffy.agrestic.common.block.entity.CrushingTubBlockEntity;
import io.github.pouffy.agrestic.common.block.entity.EvaporatingBasinBlockEntity;
import io.github.pouffy.agrestic.core.item.DisplayedItemStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Random;

import static io.github.pouffy.agrestic.client.renderer.CrushingTubRenderer.renderItem;

public class EvaporatingBasinRenderer implements BlockEntityRenderer<EvaporatingBasinBlockEntity> {

    public EvaporatingBasinRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(EvaporatingBasinBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        IItemHandler itemStackHandler = blockEntity.getContainer();
        var msr = TransformStack.of(ms);
        if (itemStackHandler.getSlots() > 0 && blockEntity.hasStack() && blockEntity.getLevel() != null) {
            DisplayedItemStack stack = blockEntity.getContainer().getDisplayedInSlot(0);
            ms.pushPose();
            msr.nudge(0);
            int angle = stack.angle;
            Random r = new Random(0);
            ms.translate(0.5f, 4/16f, 0.5f);
            renderItem(ms, buffer, light, overlay, stack.stack, angle, r, VecHelper.getCenterOf(blockEntity.getBlockPos()));
            ms.popPose();
        }
        int capacity = blockEntity.getTank().getCapacity();
        FluidStack fluid = blockEntity.getFluidStack();
        if (fluid != null && !fluid.isEmpty()){
            int blockLightIn = (light >> 4) & 0xF;
            int luminosity = Math.max(blockLightIn, fluid.getFluidType().getLightLevel());
            light = (light & 0xF00000) | luminosity << 4;
            TextureAtlasSprite fluidTexture = ClientFluidHelper.getStillTextureOrMissing(fluid);
            int color = ClientFluidHelper.getColor(fluid, blockEntity.getLevel(), blockEntity.getBlockPos());
            ms.pushPose();
            float min = 2f / 16f;
            float max = 14 / 16f;
            float depth = (2 / 16f) + ((fluid.getAmount()) / (float)capacity) * (4 / 16f);
            ClientFluidHelper.renderStillTiledFace(Direction.UP, min, min, max, max, depth, buffer.getBuffer(RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS)), ms, light, color, fluidTexture);
            ms.popPose();
        }
    }
}
