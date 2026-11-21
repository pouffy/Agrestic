package io.github.pouffy.agrestic.client;

import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public enum AgresticSprites {
    // Widgets
    TANK("tank", 18, 34),
    SLOT("slot", 18, 18),
    CHANCE_SLOT("chance_slot", 18, 18),
    BIG_SLOT("big_slot", 26, 26),

    // Animated
    ARROW_BASE("animated/arrow_base", 24, 17),
    ARROW("animated/arrow", 24, 17),
    TINY_ARROW_BASE("animated/tiny_arrow_base", 12, 11),
    TINY_ARROW("animated/tiny_arrow", 12, 11),
    BUBBLES_BASE("animated/bubbles_base", 16, 32),
    BUBBLES("animated/bubbles", 16, 32),
    ALCHEMY_ARROW_BASE("animated/alchemy_arrow_base", 50, 27),
    ALCHEMY_ARROW("animated/alchemy_arrow", 50, 28),
    LIT_BASE("animated/lit_base", 14, 14),
    LIT("animated/lit", 14, 14),

    ADV_ALCHEMY_ARROW_BASE("animated/adv_alchemy_arrow_base", 50, 52),
    ADV_ALCHEMY_ARROW_BASE_MOD("animated/adv_alchemy_arrow_base_mod", 50, 52),
    ADV_ALCHEMY_ARROW_BASE_TWO("animated/adv_alchemy_arrow_base_two", 50, 52),
    ADV_ALCHEMY_ARROW_BASE_THREE("animated/adv_alchemy_arrow_base_three", 50, 52),
    ADV_ALCHEMY_ARROW("animated/adv_alchemy_arrow", 50, 53),
    ADV_ALCHEMY_ARROW_MOD("animated/adv_alchemy_arrow_mod", 50, 53),
    ADV_ALCHEMY_ARROW_TWO("animated/adv_alchemy_arrow_two", 50, 53),
    ADV_ALCHEMY_ARROW_THREE("animated/adv_alchemy_arrow_three", 50, 53),

    // Inventories
    PLAYER_INVENTORY("container/player_inventory", 256, 256, 176, 84),
    BLANK_CONTAINER("container/blank", 256, 256, 176, 83),
    BREWING_BARREL("container/brewing_barrel", 176, 166),
    CONDENSER("container/condenser", 176, 166)
    ;

    public final ResourceLocation location;
    public final int width, height, actualWidth, actualHeight;

    AgresticSprites(String location, int width, int height) {
        this(location, width, height, width, height);
    }

    AgresticSprites(String location, int width, int height, int actualWidth, int actualHeight) {
        this.location = Agrestic.location("textures/gui/widgets/" + location + ".png");
        this.width = width;
        this.height = height;
        this.actualWidth = actualWidth;
        this.actualHeight = actualHeight;
    }

    public void blit(GuiGraphics guiGraphics, int x, int y, float uOffset, float vOffset, int width, int height) {
        guiGraphics.blit(location, x, y, uOffset, vOffset, width, height, this.width, this.height);
    }

    public void blit(GuiGraphics guiGraphics, int x, int y) {
        blit(guiGraphics, x, y, 0, 0, this.width, this.height);
    }

    public void blitWithInventory(GuiGraphics guiGraphics, int x, int y) {
        PLAYER_INVENTORY.blit(guiGraphics, x, y + this.actualHeight, 0, 0, PLAYER_INVENTORY.width, PLAYER_INVENTORY.height);
        blit(guiGraphics, x, y, 0, 0, this.width, this.height);
    }
}
