package io.github.pouffy.agrestic.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.client.ClientFluidHelper;
import io.github.pouffy.agrestic.core.GuiUtil;
import io.github.pouffy.agrestic.core.IFluidTankScreen;
import io.github.pouffy.agrestic.core.fluid.AgresticFluidTank;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.IAbstractWidgetExtension;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.Mantle;
import slimeknights.mantle.fluid.tooltip.FluidTooltipHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidWidget implements Renderable, GuiEventListener, LayoutElement, IAbstractWidgetExtension, NarratableEntry, IFluidTankScreen {
    private final AgresticFluidTank fluidStorage;
    private final AbstractContainerScreen<?> screen;
    private final Supplier<BlockPos> posSupplier;

    private final int width, height;
    private int x, y;
    private final Rect2i fluidLoc;

    private Component message;
    @Getter
    protected boolean isHovered;
    @Getter
    public boolean active = true;
    @Getter
    public boolean visible = true;
    private boolean focused;

    private final BiConsumer<Integer,List<Component>> formatter;

    public FluidWidget(AgresticFluidTank fluidStorage, AbstractContainerScreen<?> screen, int x, int y, int width, int height, Supplier<BlockPos> posSupplier) {
        this.fluidStorage = fluidStorage;
        this.screen = screen;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fluidLoc = new Rect2i(x, y, width, height);
        this.posSupplier = posSupplier;
        this.formatter = (amount, tooltip) -> FluidTooltipHandler.appendNamedList(Agrestic.location("tank"), amount, tooltip);
    }

    public static Builder builder(AgresticFluidTank fluidStorage, AbstractContainerScreen<?> screen) {
        return new Builder(fluidStorage, screen);
    }

    @Override
    public final void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.visible) {
            return;
        }
        this.isHovered = guiGraphics.containsPointInScissor(mouseX, mouseY) && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
        long fluidAmount = this.fluidStorage.getFluidAmount();

        if (fluidAmount > 0) {
            long fluidCapacity = this.fluidStorage.getCapacity();
            int fluidHeight = Math.round(((float) fluidAmount / fluidCapacity) * this.height);

            BlockPos pos = this.posSupplier.get();
            Level world = Minecraft.getInstance().level;
            if(world == null)
                return;

            int tintColor = ClientFluidHelper.getColor(this.fluidStorage.getFluid(), world, pos);
            float red = (tintColor >> 16 & 0xFF) / 255f;
            float green = (tintColor >> 8 & 0xFF) / 255f;
            float blue = (tintColor & 0xFF) / 255f;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            ClientFluidHelper.renderUIFluid(guiGraphics.pose(), this.fluidStorage.getFluid(), red, green, blue, 1.0f, this.x, this.y, this.height, fluidHeight, this.width);
        }

        if(isMouseOver(mouseX, mouseY)) {
            renderSlotHighlight(guiGraphics, -2130706433);
            drawTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    public void renderSlotHighlight(GuiGraphics guiGraphics, int color) {
        guiGraphics.fillGradient(RenderType.guiOverlay(), x, y, x + width, y + height, color, color, 0);
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {}

    @Override
    public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent event) {
        if (this.active && this.visible) {
            return !this.isFocused() ? ComponentPath.leaf(this) : null;
        } else {
            return null;
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.active && this.visible && mouseX >= (double)this.getX() && mouseY >= (double)this.getY() && mouseX < (double)(this.getX() + this.width) && mouseY < (double)(this.getY() + this.height);
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }

    @Override
    public ScreenRectangle getRectangle() {
        return GuiEventListener.super.getRectangle();
    }

    @Override
    public NarrationPriority narrationPriority() {
        if (this.isFocused()) {
            return NarrationPriority.FOCUSED;
        } else {
            return this.isHovered ? NarrationPriority.HOVERED : NarrationPriority.NONE;
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    public boolean isHoveredOrFocused() {
        return this.isHovered() || this.isFocused();
    }

    @Override
    public @Nullable FluidLocation getFluidUnderMouse(int mouseX, int mouseY) {
        if (isHovered()) {
            return new FluidLocation(this.fluidStorage.getFluid(), this.fluidLoc);
        }
        return null;
    }


    public static class Builder {
        private final AgresticFluidTank fluidStorage;
        private final AbstractContainerScreen<?> screen;
        private Supplier<BlockPos> posSupplier = () -> null;
        private int x, y;
        private int width, height;

        public Builder(AgresticFluidTank fluidStorage, AbstractContainerScreen<?> screen) {
            this.fluidStorage = fluidStorage;
            this.screen = screen;
        }

        public Builder bounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder posSupplier(Supplier<BlockPos> posSupplier) {
            this.posSupplier = posSupplier;
            return this;
        }

        public FluidWidget build() {
            return new FluidWidget(this.fluidStorage, this.screen, this.x, this.y, this.width, this.height, this.posSupplier);
        }
    }

    protected void drawTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (isHovered()) {
            FluidStack fluid = this.fluidStorage.getFluid();
            int fluidAmount = this.fluidStorage.getFluidAmount();
            int fluidCapacity = this.fluidStorage.getCapacity();

            Font font = Minecraft.getInstance().font;

            final List<Component> tooltip;
            if (fluidCapacity > 0) {
                tooltip = FluidTooltipHandler.getFluidTooltip(fluid);
            } else {
                BiConsumer<Integer, List<Component>> formatter = Screen.hasShiftDown()
                        ? FluidTooltipHandler.BUCKET_FORMATTER
                        : this.formatter;

                // add tooltips
                tooltip = new ArrayList<>();
                tooltip.add(Agrestic.makeTranslation("gui", "tank.capacity"));
                if (fluidCapacity == 0) {
                    tooltip.add(Component.translatable(Mantle.makeDescriptionId("gui", "fluid.millibucket"), 0).withStyle(ChatFormatting.GRAY));
                } else {
                    formatter.accept(fluidCapacity, tooltip);
                    if (fluidCapacity != fluidAmount) {
                        tooltip.add(Agrestic.makeTranslation("gui", "tank.available"));
                        formatter.accept(fluidCapacity - fluidAmount, tooltip);
                    }
                    // add shift message
                    if (formatter != FluidTooltipHandler.BUCKET_FORMATTER) {
                        FluidTooltipHandler.appendShift(tooltip);
                    }
                }
            }

            guiGraphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
        }



        //if(!fluid.isEmpty() && fluidAmount > 0) {
        //    List<Component> tooltipLines = new ArrayList<>();
//
        //    tooltipLines.add(fluid.getHoverName());
        //    DataComponentPatch patch = this.fluidStorage.getFluid().getComponentsPatch();
        //    if (!patch.isEmpty()) {
        //        try {
        //            Optional<?> qualityOpt = patch.get(AgresticDataComponents.QUALITY.get());
//
        //            if (qualityOpt != null && qualityOpt.isPresent() && qualityOpt.get() instanceof Float quality) {
        //                tooltipLines.add(ClientTextHelper.getQualityTooltip(quality));
        //            }
        //        } catch (Exception ignored) {
//
        //        }
        //    }
        //    tooltipLines.add(Component.literal("%s/%s mB".formatted(fluidAmount, fluidCapacity)).withStyle(ChatFormatting.GRAY));
        //    guiGraphics.renderTooltip(font, tooltipLines, Optional.empty(), mouseX, mouseY);
        //} else {
        //    List<Component> tooltipLines = new ArrayList<>();
        //    tooltipLines.add(Component.literal("0/%s mB".formatted(fluidCapacity)).withStyle(ChatFormatting.GRAY));
        //    guiGraphics.renderTooltip(font, tooltipLines, Optional.empty(), mouseX, mouseY);
        //}
    }
}
