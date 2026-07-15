package io.github.pouffy.agrestic.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticAttachmentTypes;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

public class FullmetalRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public FullmetalRenderLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int pPackedLight, T living, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (living.hasData(AgresticAttachmentTypes.IRON_SKIN.get()) && living.getData(AgresticAttachmentTypes.IRON_SKIN.get()).render()) {
            poseStack.pushPose();
            poseStack.scale(1.3f, 1.3f, 1.3f);
            poseStack.popPose();
            this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(Agrestic.location("textures/misc/fullmetal.png"))), pPackedLight, OverlayTexture.NO_OVERLAY, new Color(1F, 1F, 1F).getRGB());
        }
    }

}
