package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.client.ui.BrewingBarrelMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AgresticMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = Agrestic.getRegistryHelper().createRegister(Registries.MENU);

    public static final Supplier<MenuType<BrewingBarrelMenu>> BREWING_BARREL = MENU_TYPES
            .register("brewing_barrel", () -> IMenuTypeExtension.create(BrewingBarrelMenu::new));

    public static void staticInit() {}
}
