package io.github.pouffy.agrestic.client.ui.slot;

import io.github.pouffy.agrestic.common.block.entity.CondenserBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class CondenserResultSlot extends SlotItemHandler {
    private final Player player;
    private int removeCount;

    public CondenserResultSlot(IItemHandler container, int slot, int x, int y, Player player) {
        super(container, slot, x, y);
        this.player = player;
    }

    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(player, stack);
    }

    protected void onQuickCraft(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.checkTakeAchievements(stack);
    }

    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player.level(), this.player, this.removeCount);
        if (this.player instanceof ServerPlayer serverplayer) {
            if (this.container instanceof CondenserBlockEntity condenserBlockEntity) {
                condenserBlockEntity.awardUsedRecipes(serverplayer);
            }
        }
        this.removeCount = 0;
    }
}
