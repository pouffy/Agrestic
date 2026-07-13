package io.github.pouffy.agrestic;

import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Items;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class AgresticEnumParams {

    public static final EnumProxy<Boat.Type> OLIVE_BOAT_TYPE = new EnumProxy<>(
            Boat.Type.class, AgresticBlocks.OLIVE.planks(), Agrestic.MODID + ":olive", AgresticItems.OLIVE_BOAT, AgresticItems.OLIVE_CHEST_BOAT, Items.STICK, false
    );

    public static final EnumProxy<Boat.Type> IRONWOOD_BOAT_TYPE = new EnumProxy<>(
            Boat.Type.class, AgresticBlocks.IRONWOOD.planks(), Agrestic.MODID + ":ironwood", AgresticItems.IRONWOOD_BOAT, AgresticItems.IRONWOOD_CHEST_BOAT, Items.STICK, false
    );

}
