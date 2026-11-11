package io.github.pouffy.agrestic.core;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class GuiUtil {

    public static boolean isHovered(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public static void renderHighlight(GuiGraphics graphics, int x, int y, int width, int height) {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        graphics.fillGradient(RenderType.guiOverlay(), x, y, x + width, y + height, -2130706433, -2130706433, 0);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }

    public static void renderFluidTank(PoseStack matrices, AbstractContainerScreen<?> screen, FluidStack stack, int capacity, int x, int y, int width, int height, int depth) {
        renderFluidTank(matrices, screen, stack, stack.getAmount(), capacity, x, y, width, height, depth);
    }

    public static void renderFluidTank(PoseStack matrices, AbstractContainerScreen<?> screen, FluidStack stack, int amount, int capacity, int x, int y, int width, int height, int depth) {
        if(!stack.isEmpty() && capacity > 0) {
            int maxY = y + height;
            int fluidHeight = Math.min(height * amount / capacity, height);
            renderTiledFluid(matrices, screen, stack, x, maxY - fluidHeight, width, fluidHeight, depth);
        }
    }

    public static void renderTiledFluid(PoseStack matrices, AbstractContainerScreen<?> screen, FluidStack stack, int x, int y, int width, int height, int depth) {
        if (!stack.isEmpty()) {
            IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(stack.getFluid());
            TextureAtlasSprite fluidSprite = screen.getMinecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(clientFluid.getStillTexture(stack));
            RenderUtils.setColorRGBA(clientFluid.getTintColor(stack));
            renderTiledTextureAtlas(matrices, screen, fluidSprite, x, y, width, height, depth, stack.getFluid().getFluidType().isLighterThanAir());
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public static void renderTiledTextureAtlas(PoseStack matrices, AbstractContainerScreen<?> screen, TextureAtlasSprite sprite, int x, int y, int width, int height, int depth, boolean upsideDown) {
        // start drawing sprites
        RenderUtils.bindTexture(sprite.atlasLocation());
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        // tile vertically
        float u1 = sprite.getU0();
        float v1 = sprite.getV0();
        int spriteHeight = sprite.contents().height();
        int spriteWidth = sprite.contents().width();
        int startX = x + screen.getGuiLeft();
        int startY = y + screen.getGuiTop();
        do {
            int renderHeight = Math.min(spriteHeight, height);
            height -= renderHeight;
            float v2 = sprite.getV((16f * renderHeight) / spriteHeight);

            // we need to draw the quads per width too
            int x2 = startX;
            int widthLeft = width;
            Matrix4f matrix = matrices.last().pose();
            // tile horizontally
            do {
                int renderWidth = Math.min(spriteWidth, widthLeft);
                widthLeft -= renderWidth;

                float u2 = sprite.getU((16f * renderWidth) / spriteWidth);
                if(upsideDown) {
                    buildSquare(matrix, builder, x2, x2 + renderWidth, startY, startY + renderHeight, depth, u1, u2, v2, v1);
                } else {
                    buildSquare(matrix, builder, x2, x2 + renderWidth, startY, startY + renderHeight, depth, u1, u2, v1, v2);
                }
                x2 += renderWidth;
            } while(widthLeft > 0);

            startY += renderHeight;
        } while(height > 0);

        // finish drawing sprites
        BufferUploader.drawWithShader(builder.buildOrThrow());
        // RenderSystem.enableAlphaTest();
        RenderSystem.enableDepthTest();
    }

    private static void buildSquare(Matrix4f matrix, BufferBuilder builder, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2) {
        builder.addVertex(matrix, x1, y2, z).setUv(u1, v2);
        builder.addVertex(matrix, x2, y2, z).setUv(u2, v2);
        builder.addVertex(matrix, x2, y1, z).setUv(u2, v1);
        builder.addVertex(matrix, x1, y1, z).setUv(u1, v1);
    }
}
