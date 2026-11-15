package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.entity.AgresticBoat;
import io.github.pouffy.agrestic.common.entity.AgresticChestBoat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AgresticEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = Agrestic.getRegistryHelper().createRegister(Registries.ENTITY_TYPE);

    public static final Supplier<EntityType<AgresticBoat>> BOAT = ENTITIES.register("boat", () -> EntityType.Builder.<AgresticBoat>of(AgresticBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("boat"));
    public static final Supplier<EntityType<AgresticChestBoat>> CHEST_BOAT = ENTITIES.register("chest_boat", () -> EntityType.Builder.<AgresticChestBoat>of(AgresticChestBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("chest_boat"));

    public static void staticInit() {}
}
