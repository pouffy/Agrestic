package io.github.pouffy.agrestic.common.entity;

import io.github.pouffy.agrestic.init.AgresticEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class AgresticChestBoat extends ChestBoat implements CustomBoat {
    private static final EntityDataAccessor<Integer> DATA_ID_WOOD_TYPE = SynchedEntityData.defineId(AgresticChestBoat.class, EntityDataSerializers.INT);

    public AgresticChestBoat(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    public AgresticChestBoat(Level level, double x, double y, double z) {
        this(AgresticEntities.CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_WOOD_TYPE, 0);
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Type", 8)) {
            this.setWoodType(CustomBoat.Type.byName(tag.getString("Type")));
        }
    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Type", this.getWoodType().getSerializedName());
    }

    @Nonnull
    @Override
    public Item getDropItem() {
        return this.getWoodType().getChestBoatItem().get();
    }

    @Override
    public CustomBoat.Type getWoodType() {
        return CustomBoat.Type.byId(this.entityData.get(DATA_ID_WOOD_TYPE));
    }

    @Override
    public void setWoodType(CustomBoat.Type type) {
        this.entityData.set(DATA_ID_WOOD_TYPE, type.ordinal());
    }

    @Nonnull
    @Override
    protected Component getTypeName() {
        return EntityType.CHEST_BOAT.getDescription();
    }
}
