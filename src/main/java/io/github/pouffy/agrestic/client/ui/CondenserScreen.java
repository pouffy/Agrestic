package io.github.pouffy.agrestic.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.pouffy.agrestic.client.AgresticSprites;
import io.github.pouffy.agrestic.core.IFluidTankScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.Nullable;

public class CondenserScreen extends AbstractContainerScreen<CondenserMenu> implements IFluidTankScreen {

    private FluidWidget fluid;

    public CondenserScreen(CondenserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderFg(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public void renderFg(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        fluid.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void init() {
        super.init();
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        fluid = FluidWidget.builder(menu.getBlockEntity().getTank(), this)
                .bounds(i + 133, j + 27, 16, 32)
                .posSupplier(() -> menu.getBlockEntity().getBlockPos())
                .build();
        addWidget(fluid);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, AgresticSprites.CONDENSER.location);

        AgresticSprites.BLANK_CONTAINER.blitWithInventory(guiGraphics, this.leftPos, this.topPos);
        AgresticSprites.ALCHEMY_ARROW_BASE.blit(guiGraphics, this.leftPos + 44, this.topPos + 29);
        AgresticSprites.LIT_BASE.blit(guiGraphics, this.leftPos + 66, this.topPos + 45);

        AgresticSprites.BIG_SLOT.blit(guiGraphics, this.leftPos + 100, this.topPos + 30);
        AgresticSprites.TANK.blit(guiGraphics, this.leftPos + 132, this.topPos + 26);

        AgresticSprites.SLOT.blit(guiGraphics, this.leftPos + 26, this.topPos + 22);
        AgresticSprites.SLOT.blit(guiGraphics, this.leftPos + 26, this.topPos + 46);

        AgresticSprites.SLOT.blit(guiGraphics, this.leftPos + 65, this.topPos + 61);
        AgresticSprites.SLOT.blit(guiGraphics, this.leftPos + 104, this.topPos + 6);

        renderProgress(guiGraphics, this.leftPos, this.topPos);
    }

    private void renderProgress(GuiGraphics guiGraphics, int x, int y) {
        int i = this.leftPos;
        int j = this.topPos;
        int m = this.menu.getCondensingTime();
        if (m > 0) {
            AgresticSprites.ALCHEMY_ARROW.blit(guiGraphics, x + 44, y + 28, 0, 0, menu.getScaledArrowProgress(), 28);
        }
        if (this.menu.isLit()) {
            int l = Mth.ceil(this.menu.getLitProgress() * 13.0F) + 1;
            AgresticSprites.LIT.blit(guiGraphics, this.leftPos + 66, this.topPos + 45 + 14 - l, 0, 14 - l, 14, l);
        }
    }

    @Override
    public @Nullable FluidLocation getFluidUnderMouse(int mouseX, int mouseY) {
        int checkX = mouseX - leftPos;
        int checkY = mouseY - topPos;
        return fluid.getFluidUnderMouse(checkX, checkY);
    }
}
