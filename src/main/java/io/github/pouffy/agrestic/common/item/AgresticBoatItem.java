package io.github.pouffy.agrestic.common.item;

import io.github.pouffy.agrestic.common.entity.AgresticBoat;
import io.github.pouffy.agrestic.common.entity.AgresticChestBoat;
import io.github.pouffy.agrestic.common.entity.CustomBoat;
import io.github.pouffy.agrestic.mixin.BoatItemAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;

public class AgresticBoatItem extends BoatItem {

    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

    private final CustomBoat.Type woodType;
    private final boolean hasChest;

    public AgresticBoatItem(boolean hasChest, CustomBoat.Type woodType, Properties properties) {
        super(hasChest, Boat.Type.OAK, properties);
        this.woodType = woodType;
        this.hasChest = hasChest;
    }

    public void dispense() {
        DispenserBlock.registerBehavior(this, (source, stack) -> {
            Direction direction = source.state().getValue(DispenserBlock.FACING);
            ServerLevel serverlevel = source.level();
            Vec3 vec3 = source.center();
            double width = (double)0.5625F + (double) EntityType.BOAT.getWidth() / (double)2.0F;
            double x = vec3.x() + (double)direction.getStepX() * width;
            double y = vec3.y() + (double)((float)direction.getStepY() * 1.125F);
            double z = vec3.z() + (double)direction.getStepZ() * width;
            BlockPos blockpos = source.pos().relative(direction);
            Boat boat = this.hasChest ? new AgresticChestBoat(serverlevel, x, y, z) : new AgresticBoat(serverlevel, x, y, z);
            EntityType.createDefaultStackConfig(serverlevel, stack, null).accept(boat);
            ((CustomBoat)boat).setWoodType(woodType);
            boat.setYRot(direction.toYRot());
            double yOffset;
            if (boat.canBoatInFluid(serverlevel.getFluidState(blockpos))) {
                yOffset = 1.0F;
            } else {
                if (!serverlevel.getBlockState(blockpos).isAir() || !boat.canBoatInFluid(serverlevel.getFluidState(blockpos.below()))) {
                    return new DefaultDispenseItemBehavior().dispense(source, stack);
                }
                yOffset = 0.0F;
            }
            boat.setPos(x, y + yOffset, z);
            serverlevel.addFreshEntity(boat);
            stack.shrink(1);
            return stack;
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        HitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);

        if (hitResult.getType() != HitResult.Type.MISS) {
            Vec3 vec3 = player.getViewVector(1.0F);
            List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(vec3.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);

            if (!list.isEmpty()) {
                Vec3 vec31 = player.getEyePosition();

                for (Entity entity : list) {
                    if (entity.getBoundingBox().inflate(entity.getPickRadius()).contains(vec31)) {
                        return InteractionResultHolder.pass(stack);
                    }
                }
            }

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                CustomBoat customBoat = this.getBoat(level, hitResult);
                customBoat.setWoodType(this.woodType);

                if (!(customBoat instanceof Boat boat)) {
                    return InteractionResultHolder.pass(stack);
                }

                boat.setYRot(player.getYRot());

                if (!level.noCollision(boat, boat.getBoundingBox())) {
                    return InteractionResultHolder.fail(stack);
                } else {
                    if (!level.isClientSide()) {
                        level.addFreshEntity(boat);
                        level.gameEvent(player, GameEvent.ENTITY_PLACE, hitResult.getLocation());

                        stack.consume(1, player);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
                }
            }

        }
        return InteractionResultHolder.pass(stack);
    }

    private CustomBoat getBoat(Level level, HitResult hitResult) {
        return this.hasChest ? new AgresticChestBoat(level, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z) : new AgresticBoat(level, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
    }
}
