package io.github.pouffy.agrestic.compat.jei.util;

import io.github.pouffy.agrestic.core.IFluidTankScreen;
import mezz.jei.api.gui.builder.IClickableIngredientFactory;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Optional;

public class GuiContainerTankHandler<C extends AbstractContainerMenu, T extends AbstractContainerScreen<C> & IFluidTankScreen> implements IGuiContainerHandler<T> {

    @Override
    public Optional<? extends IClickableIngredient<?>> getClickableIngredientUnderMouse(IClickableIngredientFactory factory, T containerScreen, double mouseX, double mouseY) {
        IFluidTankScreen.FluidLocation fluid = containerScreen.getFluidUnderMouse((int)mouseX, (int)mouseY);
        if (fluid != null) {
            return factory.createBuilder(NeoForgeTypes.FLUID_STACK, fluid.fluid()).buildWithArea(fluid.location());
        }
        return Optional.empty();
    }
}
