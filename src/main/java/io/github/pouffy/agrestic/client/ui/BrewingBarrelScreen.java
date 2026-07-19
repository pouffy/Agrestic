package io.github.pouffy.agrestic.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.pouffy.agrestic.client.AgresticSprites;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BrewingBarrelScreen extends AbstractContainerScreen<BrewingBarrelMenu> {
    private static final int[] BUBBLE_PROGRESS = new int[]{0, 6, 13, 18, 22, 26, 32};

    public BrewingBarrelScreen(BrewingBarrelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        addWidget(FluidWidget.builder(menu.getBlockEntity().getInputTank())
                .bounds(this.getXSize() + 62, this.getYSize() + 27, 16, 32)
                .posSupplier(() -> menu.getBlockEntity().getBlockPos())
                .build());

        addWidget(FluidWidget.builder(menu.getBlockEntity().getResultTank())
                .bounds(this.getXSize() + 116, this.getYSize() + 27, 16, 32)
                .posSupplier(() -> menu.getBlockEntity().getBlockPos())
                .build());

        addWidget(FluidWidget.builder(menu.getBlockEntity().getAuxiliaryTank())
                .bounds(this.getXSize() + 26, this.getYSize() + 35, 16, 16)
                .posSupplier(() -> menu.getBlockEntity().getBlockPos())
                .build());
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, AgresticSprites.BREWING_BARREL.location);

        int x = (width - AgresticSprites.BREWING_BARREL.width) / 2;
        int y = (height - AgresticSprites.BREWING_BARREL.height) / 2;

        guiGraphics.blit(AgresticSprites.BREWING_BARREL.location, x, y, 0, 0, AgresticSprites.BREWING_BARREL.width, AgresticSprites.BREWING_BARREL.height);

        renderProgress(guiGraphics, x, y);
    }

    private void renderProgress(GuiGraphics guiGraphics, int x, int y) {
        int m = this.menu.getBrewingTime();
        if (m > 0) {
            guiGraphics.blit(AgresticSprites.ARROW.location,
                    x + 85, y + 35, 0, 0, menu.getScaledArrowProgress(), 16, AgresticSprites.ARROW.width, AgresticSprites.ARROW.height);

            guiGraphics.blit(AgresticSprites.TINY_ARROW.location,
                    x + 45, y + 37, 0, 0, menu.getScaledAuxArrowProgress(), 10, AgresticSprites.TINY_ARROW.width, AgresticSprites.TINY_ARROW.height);

            int n = BUBBLE_PROGRESS[m / 3 % 7];
            if (n > 0) {
                guiGraphics.blitSprite(AgresticSprites.BUBBLES.location, 12, 29, 0, 29 - n, x + 138, y + 28 + (29 - n), AgresticSprites.BUBBLES.width, n);
            }
        }
    }
}
