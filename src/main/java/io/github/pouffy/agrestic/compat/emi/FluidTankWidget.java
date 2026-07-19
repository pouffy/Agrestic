package io.github.pouffy.agrestic.compat.emi;

import com.mojang.datafixers.util.Either;
import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class FluidTankWidget extends SlotWidget {
    private final long capacity;
    protected final float fluidFullness;
    protected final @Nullable Either<FluidStack, EmiIngredient> fluid;
    public int WIDTH = 18;
    public int HEIGHT = 34;
    public int FLUID_AREA_WIDTH = 16;
    public int FLUID_AREA_HEIGHT = 32;
    public EmiTexture background = AgresticEmiPlugin.TANK;

    public FluidTankWidget(@Nullable Either<FluidStack, EmiIngredient> fluid, int x, int y, long capacity) {
        super(map(fluid), x, y);
        int amount = fluid == null ? 0 : fluid.map(FluidStack::getAmount, EmiIngredient::getAmount).intValue();
        this.capacity = capacity;
        this.fluidFullness = Math.min((float) ((double) amount / (double) capacity), 1.0f);
        this.fluid = fluid;
    }

    public static FluidTankWidget input(EmiIngredient ingredient, int x, int y, long capacity) {
        return new FluidTankWidget(Either.right(ingredient), x, y, capacity);
    }

    public static FluidTankWidget result(FluidStack fluidStack, int x, int y, long capacity) {
        return new FluidTankWidget(Either.left(fluidStack), x, y, capacity);
    }

    public FluidTankWidget size(int width, int height, int areaWidth, int areaHeight, EmiTexture background) {
        WIDTH = width;
        HEIGHT = height;
        FLUID_AREA_WIDTH = areaWidth;
        FLUID_AREA_HEIGHT = areaHeight;
        this.background = background;
        return this;
    }

    private static EmiIngredient map(@Nullable Either<FluidStack, EmiIngredient> fluid) {
        if (fluid == null) {
            return EmiStack.EMPTY;
        }
        return fluid.map(stack -> EmiStack.of(stack.getFluid(), stack.getComponentsPatch(), stack.getAmount()), ingredient -> ingredient);
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, WIDTH, HEIGHT);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (this.fluid != null)
            this.fluid.ifLeft(fluid -> renderStack(fluid, context, mouseX, mouseY, delta)).ifRight(ingredient -> renderIngredient(ingredient, context, mouseX, mouseY, delta));
    }

    private void renderStack(FluidStack fluid, GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (drawBack) {
            background.render(context, x, y, delta);
        }

        if (fluid != null) {
            EmiUIUtils.renderFluid(context.pose(), fluid, x + 1, y + 1, FLUID_AREA_HEIGHT, fluidFullness * FLUID_AREA_HEIGHT, FLUID_AREA_WIDTH);
        }

        if (this.catalyst) {
            EmiRender.renderCatalystIcon(this.getStack(), context, x + 2, y + 4);
        }

        Bounds bounds = getBounds();
        if (bounds.contains(mouseX, mouseY)) {
            EmiUIUtils.drawSlotHightlight(context, bounds.x() + 1, bounds.y() + 1, bounds.width() - 2,
                    bounds.height() - 2);
        }
    }

    private void renderIngredient(EmiIngredient ingredient, GuiGraphics context, int mouseX, int mouseY, float delta) {
        for (EmiStack stack : ingredient.getEmiStacks()) {
            if (stack.getKey() instanceof Fluid fluid) {
                FluidStack fluidStack = new FluidStack(fluid.builtInRegistryHolder(), (int) ingredient.getAmount(), stack.getComponentChanges());
                renderStack(fluidStack, context, mouseX, mouseY, delta);
            }
        }
    }
}
