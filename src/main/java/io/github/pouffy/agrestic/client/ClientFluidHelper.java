package io.github.pouffy.agrestic.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static net.minecraft.client.renderer.RenderStateShard.*;

public class ClientFluidHelper {
    public static final float FLUID_PATCH_WIDTH = 16f;
    public static final float FLUID_PATCH_HEIGHT = 16f;
    private static final Cache<ResourceLocation, Material> CACHED_MATERIALS = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .build();

    //cached materials
    public static Material get(ResourceLocation bockTexture) {
        try {
            return CACHED_MATERIALS.get(bockTexture, () -> new Material(InventoryMenu.BLOCK_ATLAS, bockTexture));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static Material get(FluidStack fluid) {
        ResourceLocation id = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture(fluid);
        return get(id);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(FluidStack fluid) {
        ResourceLocation id = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture(fluid);
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(id);
    }

    @OnlyIn(Dist.CLIENT)
    public static TextureAtlasSprite getStillTextureOrMissing(FluidStack fluid) {
        TextureAtlasSprite texture = getStillTexture(fluid);
        if (texture != null)
            return texture;

        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation());
    }

    @OnlyIn(Dist.CLIENT)
    public static TextureAtlasSprite[] getSpritesOrMissing(FluidStack fluid) {
        ResourceLocation stillTexture = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture(fluid);
        ResourceLocation flowTexture = IClientFluidTypeExtensions.of(fluid.getFluid()).getFlowingTexture(fluid);
        TextureAtlasSprite still = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        TextureAtlasSprite flow = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(flowTexture);
        TextureAtlasSprite toRetStill = still != null ? still : Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation());
        TextureAtlasSprite toRetFlow = flow != null ? flow : Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation());
        return new TextureAtlasSprite[]{toRetStill, toRetFlow};
    }

    @OnlyIn(Dist.CLIENT)
    public static int getColor(FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        Fluid fluid = stack.getFluid();
        IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(fluid);
        if (level == null || pos == null)
            return extension.getTintColor(stack);
        return extension.getTintColor(fluid.defaultFluidState(), level, pos);
    }

    private static final RenderType FLUID =
            RenderType.create(Agrestic.MODID + ":fluid", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_TRANSLUCENT_SHADER)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setCullState(NO_CULL)
                    .setOutputState(MAIN_TARGET)
                    .createCompositeState(true));

    public static RenderType fluid() {
        return FLUID;
    }

    public static void renderStillTiledFace(Direction dir, float left, float down, float right, float up, float depth, VertexConsumer builder, PoseStack ms, int light, int color, TextureAtlasSprite texture) {
        renderTiledFace(dir, left, down, right, up, depth, builder, ms, light, color, texture, 1);
    }

    public static void renderTiledFace(Direction dir, float left, float down, float right, float up, float depth, VertexConsumer builder, PoseStack ms, int light, int color, TextureAtlasSprite texture, float textureScale) {
        boolean positive = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE;
        boolean horizontal = dir.getAxis().isHorizontal();
        boolean x = dir.getAxis() == Direction.Axis.X;

        float shrink = texture.uvShrinkRatio() * 0.25f * textureScale;
        float centerU = texture.getU0() + (texture.getU1() - texture.getU0()) * 0.5f * textureScale;
        float centerV = texture.getV0() + (texture.getV1() - texture.getV0()) * 0.5f * textureScale;

        float f;
        float x2 = 0;
        float y2 = 0;
        float u1, u2;
        float v1, v2;
        for (float x1 = left; x1 < right; x1 = x2) {
            f = Mth.floor(x1);
            x2 = Math.min(f + 1, right);
            if (dir == Direction.NORTH || dir == Direction.EAST) {
                f = Mth.ceil(x2);
                u1 = texture.getU((f - x2) * textureScale);
                u2 = texture.getU((f - x1) * textureScale);
            } else {
                u1 = texture.getU((x1 - f) * textureScale);
                u2 = texture.getU((x2 - f) * textureScale);
            }
            u1 = Mth.lerp(shrink, u1, centerU);
            u2 = Mth.lerp(shrink, u2, centerU);
            for (float y1 = down; y1 < up; y1 = y2) {
                f = Mth.floor(y1);
                y2 = Math.min(f + 1, up);
                if (dir == Direction.UP) {
                    v1 = texture.getV((y1 - f) * textureScale);
                    v2 = texture.getV((y2 - f) * textureScale);
                } else {
                    f = Mth.ceil(y2);
                    v1 = texture.getV((f - y2) * textureScale);
                    v2 = texture.getV((f - y1) * textureScale);
                }
                v1 = Mth.lerp(shrink, v1, centerV);
                v2 = Mth.lerp(shrink, v2, centerV);

                if (horizontal) {
                    if (x) {
                        putVertex(builder, ms, depth, y2, positive ? x2 : x1, color, u1, v1, dir, light);
                        putVertex(builder, ms, depth, y1, positive ? x2 : x1, color, u1, v2, dir, light);
                        putVertex(builder, ms, depth, y1, positive ? x1 : x2, color, u2, v2, dir, light);
                        putVertex(builder, ms, depth, y2, positive ? x1 : x2, color, u2, v1, dir, light);
                    } else {
                        putVertex(builder, ms, positive ? x1 : x2, y2, depth, color, u1, v1, dir, light);
                        putVertex(builder, ms, positive ? x1 : x2, y1, depth, color, u1, v2, dir, light);
                        putVertex(builder, ms, positive ? x2 : x1, y1, depth, color, u2, v2, dir, light);
                        putVertex(builder, ms, positive ? x2 : x1, y2, depth, color, u2, v1, dir, light);
                    }
                } else {
                    putVertex(builder, ms, x1, depth, positive ? y1 : y2, color, u1, v1, dir, light);
                    putVertex(builder, ms, x1, depth, positive ? y2 : y1, color, u1, v2, dir, light);
                    putVertex(builder, ms, x2, depth, positive ? y2 : y1, color, u2, v2, dir, light);
                    putVertex(builder, ms, x2, depth, positive ? y1 : y2, color, u2, v1, dir, light);
                }
            }
        }
    }

    private static void putVertex(VertexConsumer builder, PoseStack ms, float x, float y, float z, int color, float u, float v, Direction face, int light) {
        Vec3i normal = face.getNormal();
        PoseStack.Pose peek = ms.last();
        int a = color >> 24 & 0xff;
        int r = color >> 16 & 0xff;
        int g = color >> 8 & 0xff;
        int b = color & 0xff;

        int lu = light & '\uffff';
        int lv = light >> 16 & '\uffff';

        builder.addVertex(peek.pose(), x, y, z)
                .setColor(r, g, b, a)
                .setUv(u, v)
                .setUv1(0, 10)
                .setUv2(lu, lv)
                .setLight(light)
                .setNormal(peek.copy(), normal.getX(), normal.getY(), normal.getZ())
        ;
    }

    public static void renderTiledSprite(GuiGraphics guiGraphics, TextureAtlasSprite sprite, int x, int y, int width, int height, float red, float green, float blue, float alpha) {
        int spriteWidth = sprite.contents().width();
        int spriteHeight = sprite.contents().height();

        int xCount = Mth.floor((float) width / spriteWidth);
        int yCount = Mth.floor((float) height / spriteHeight);
        int xRemainder = width % spriteWidth;
        int yRemainder = height % spriteHeight;

        for (int i = 0; i < xCount; i++) {
            for (int j = 0; j < yCount; j++) {
                int x1 = x + (i * spriteWidth);
                int y1 = y + (j * spriteHeight);

                guiGraphics.blit(x1, y1, 0, spriteWidth, spriteHeight, sprite, red, green, blue, alpha);
            }

            if(yRemainder > 0) {
                int x1 = x + (i * spriteWidth);
                int y1 = y + (yCount * spriteHeight);

                guiGraphics.blit(x1, y1, 0, spriteWidth, yRemainder, sprite, red, green, blue, alpha);
            }
        }

        if(xRemainder > 0) {
            for (int j = 0; j < yCount; j++) {
                int x1 = x + (xCount * spriteWidth);
                int y1 = y + (j * spriteHeight);

                guiGraphics.blit(x1, y1, 0, xRemainder, spriteHeight, sprite, red, green, blue, alpha);
            }

            if(yRemainder > 0) {
                int x1 = x + (xCount * spriteWidth);
                int y1 = y + (yCount * spriteHeight);

                guiGraphics.blit(x1, y1, 0, xRemainder, yRemainder, sprite, red, green, blue, alpha);
            }
        }
    }

    public static void renderUIFluid(PoseStack matrices, FluidStack fluid, float red, float green, float blue, float alpha, int x, int areaY, float areaHeight, float fluidHeight, float fluidWidth) {
        renderUIFluid(matrices, getSpritesOrMissing(fluid), red, green, blue, alpha, x, areaY, areaHeight, fluidHeight, fluidWidth);
    }

    public static void renderUIFluid(PoseStack matrices, TextureAtlasSprite[] sprites, float red, float green, float blue, float alpha, int x, int areaY, float areaHeight, float fluidHeight, float fluidWidth) {
        if (sprites == null || sprites.length < 1 || sprites[0] == null) {
            return;
        }
        TextureAtlasSprite sprite = sprites[0];
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, sprite.atlasLocation());
        Matrix4f model = matrices.last().pose();
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        int fluidStripCount = (int) (fluidHeight / FLUID_PATCH_HEIGHT);
        for (int i = 0; i < fluidStripCount; i++) {
            buildFluidHorizontalStrip(bufferBuilder, model, sprite, (float) x,
                    (float) areaY + areaHeight - FLUID_PATCH_HEIGHT * (i + 1), fluidWidth, FLUID_PATCH_HEIGHT, red,
                    green, blue, alpha);
        }
        float stripRemainder = fluidHeight % FLUID_PATCH_HEIGHT;
        buildFluidHorizontalStrip(bufferBuilder, model, sprite, (float) x,
                (float) areaY + areaHeight - FLUID_PATCH_HEIGHT * fluidStripCount - stripRemainder, fluidWidth,
                stripRemainder, red, green, blue, alpha);

        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }

    private static void buildFluidHorizontalStrip(BufferBuilder bufferBuilder, Matrix4f model, TextureAtlasSprite sprite, float x0, float y0, float width, float height, float r, float g, float b, float alpha) {
        int fluidPatchCount = (int) (width / FLUID_PATCH_WIDTH);
        for (int i = 0; i < fluidPatchCount; i++) {
            buildFluidPatch(bufferBuilder, model, sprite, x0 + FLUID_PATCH_WIDTH * i, y0, FLUID_PATCH_WIDTH, height, r,
                    g, b, alpha);
        }
        float patchRemainder = width % FLUID_PATCH_WIDTH;
        buildFluidPatch(bufferBuilder, model, sprite, x0 + FLUID_PATCH_WIDTH * fluidPatchCount, y0, patchRemainder,
                height, r, g, b, alpha);
    }

    private static void buildFluidPatch(BufferBuilder bufferBuilder, Matrix4f model, TextureAtlasSprite sprite, float x0, float y0, float width, float height, float r, float g, float b, float alpha) {
        float x1 = x0 + width;
        float y1 = y0 + height;
        float uMax = sprite.getU1();
        float vMax = sprite.getV1();
        float spriteWidth = sprite.getU1() - sprite.getU0();
        float spriteHeight = sprite.getV1() - sprite.getV0();
        float uMin = uMax - spriteWidth * width / 16f;
        float vMin = vMax - spriteHeight * height / 16f;
        bufferBuilder.addVertex(model, x0, y1, 1.0F).setUv(uMin, vMax).setColor(r, g, b, alpha);
        bufferBuilder.addVertex(model, x1, y1, 1.0F).setUv(uMax, vMax).setColor(r, g, b, alpha);
        bufferBuilder.addVertex(model, x1, y0, 1.0F).setUv(uMax, vMin).setColor(r, g, b, alpha);
        bufferBuilder.addVertex(model, x0, y0, 1.0F).setUv(uMin, vMin).setColor(r, g, b, alpha);
    }
}
