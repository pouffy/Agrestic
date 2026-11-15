package io.github.pouffy.agrestic.client.renderer;

import com.mojang.datafixers.util.Pair;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.entity.CustomBoat;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;


import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class AgresticBoatRenderer extends BoatRenderer {

    private final Map<CustomBoat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources = new HashMap<>();

    public AgresticBoatRenderer(EntityRendererProvider.Context context, boolean hasChest) {
        super(context, hasChest);

        for(CustomBoat.Type type : CustomBoat.Type.values()) {
            this.boatResources.put(type, Pair.of(type.getTexture(hasChest), this.createBoatModel(context, type, hasChest)));
        }
    }

    private ListModel<Boat> createBoatModel(EntityRendererProvider.Context context, CustomBoat.Type type, boolean hasChest) {
        ModelLayerLocation layerLocation = new ModelLayerLocation(Agrestic.location(hasChest ? type.getChestModelLocation() : type.getModelLocation()), "main");
        ModelPart part = context.bakeLayer(layerLocation);

        return hasChest ? new ChestBoatModel(part) : new BoatModel(part);
    }

    @Nonnull
    @Override
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(@Nonnull Boat boat) {
        if (boat instanceof CustomBoat customBoat) {
            return this.boatResources.get(customBoat.getWoodType());
        }
        return super.getModelWithLocation(boat);
    }
}
