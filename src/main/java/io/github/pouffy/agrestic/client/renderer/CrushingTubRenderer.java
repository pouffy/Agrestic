package io.github.pouffy.agrestic.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.pouffy.agrestic.client.helper.VecHelper;
import io.github.pouffy.agrestic.client.helper.pose.TransformStack;
import io.github.pouffy.agrestic.common.block.entity.CrushingTubBlockEntity;
import io.github.pouffy.agrestic.core.item.DisplayedItemStack;
import io.github.pouffy.agrestic.init.AgresticTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.joml.Vector3f;
import slimeknights.mantle.client.render.FluidCuboid;
import slimeknights.mantle.client.render.FluidRenderer;

import java.util.Random;

public class CrushingTubRenderer implements BlockEntityRenderer<CrushingTubBlockEntity> {

    public CrushingTubRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(CrushingTubBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
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
        if (!fluid.isEmpty()) {
            light = FluidRenderer.withBlockLight(light, fluid.getFluid().getFluidType().getLightLevel(fluid));
            IClientFluidTypeExtensions client = IClientFluidTypeExtensions.of(fluid.getFluid());
            TextureAtlasSprite still = FluidRenderer.getBlockSprite(client.getStillTexture(fluid));
            TextureAtlasSprite flowing = FluidRenderer.getBlockSprite(client.getFlowingTexture(fluid));
            int color = client.getTintColor(fluid);
            Vector3f from = new Vector3f(2f / 16f, 2f / 16f, 2f / 16f);
            Vector3f to = new Vector3f(14f / 16f, (2f / 16f) + ((fluid.getAmount()) / (float)capacity) * (6f / 16f), 14f / 16f);
            ms.pushPose();
            FluidRenderer.renderCuboid(ms, buffer.getBuffer(RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS)), FluidCuboid.builder().face(Direction.UP).build(), still, flowing, from, to, color, light, false);
            ms.popPose();
        }
    }

    public static void renderItem(PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack itemStack, int angle, Random r, Vec3 itemPosition) {
        ItemRenderer itemRenderer = Minecraft.getInstance()
                .getItemRenderer();
        var msr = TransformStack.of(ms);
        int count = Mth.log2((itemStack.getCount()));
        BakedModel bakedModel = itemRenderer.getModel(itemStack, null, null, 0);
        boolean blockItem = bakedModel.isGui3d();
        boolean renderUpright = itemStack.is(AgresticTags.RENDER_UPRIGHT);

        ms.pushPose();
        msr.rotateYDegrees(angle);

        if (renderUpright) {
            Vec3 cameraPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            Vec3 diff = itemPosition.subtract(cameraPosition);
            float yRot = (float) (Mth.atan2(diff.x, diff.z) + Math.PI);
            ms.mulPose(Axis.YP.rotation(yRot));
            ms.translate(0, 3 / 32d, -1 / 16f);
        }

        for (int i = 0; i <= count; i++) {
            ms.pushPose();
            if (blockItem && r != null)
                ms.translate(r.nextFloat() * .0625f * i, 0, r.nextFloat() * .0625f * i);

            if (blockItem && renderUpright) {
                ms.translate(0, 1 / 16f, 0);
                ms.scale(.755f, .755f, .755f);
            } else
                ms.scale(.5f, .5f, .5f);

            if (!blockItem && !renderUpright) {
                ms.translate(0, -3 / 16f, 0);
                msr.rotateXDegrees(90);
            }
            itemRenderer.render(itemStack, ItemDisplayContext.FIXED, false, ms, buffer, light, overlay, bakedModel);
            ms.popPose();

            if (!renderUpright) {
                if (!blockItem)
                    msr.rotateYDegrees(10);
                ms.translate(0, blockItem ? 1 / 64d : 1 / 16d, 0);
            } else
                ms.translate(0, 0, -1 / 16f);
        }

        ms.popPose();
    }
}
