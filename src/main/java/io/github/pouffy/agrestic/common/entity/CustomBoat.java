package io.github.pouffy.agrestic.common.entity;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticItems;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BoatItem;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public interface CustomBoat {
    void setWoodType(Type woodType);
    Type getWoodType();

    enum Type implements StringRepresentable {
        OLIVE("olive", AgresticItems.OLIVE_BOAT, AgresticItems.OLIVE_CHEST_BOAT),
        IRONWOOD("ironwood", AgresticItems.IRONWOOD_BOAT, AgresticItems.IRONWOOD_CHEST_BOAT)
        ;

        private final String name;
        @Getter
        private final Supplier<BoatItem> boatItem;
        @Getter
        private final Supplier<BoatItem> chestBoatItem;

        Type(String name, Supplier<BoatItem> boatItem, Supplier<BoatItem> chestBoatItem) {
            this.name = name;
            this.boatItem = boatItem;
            this.chestBoatItem = chestBoatItem;
        }

        @Nonnull
        @Override
        public String getSerializedName() {
            return this.name;
        }

        public ResourceLocation getTexture(boolean hasChest) {
            if (hasChest) {
                return Agrestic.location("textures/entity/chest_boat/" + this.name + ".png");
            }
            return Agrestic.location("textures/entity/boat/" + this.name + ".png");
        }

        public String getModelLocation() {
            return "boat/" + this.name;
        }

        public String getChestModelLocation() {
            return "chest_boat/" + this.name;
        }

        public static Type byId(int id) {
            Type[] values = values();
            if (id < 0 || id >= values.length) {
                id = 0;
            }

            return values[id];
        }

        public static Type byName(String name) {
            Type[] values = values();

            for (Type value : values) {
                if (value.getSerializedName().equals(name)) {
                    return value;
                }
            }

            return values[0];
        }
    }
}
