package io.github.pouffy.agrestic.client;

import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.resources.ResourceLocation;

public enum AgresticSprites {
    // Widgets
    TANK("tank", 18, 34),
    SLOT("slot", 18, 18),
    CHANCE_SLOT("chance_slot", 18, 18),

    // Animated
    ARROW_BASE("animated/arrow_base", 24, 17),
    ARROW("animated/arrow", 24, 17),
    TINY_ARROW_BASE("animated/tiny_arrow_base", 12, 11),
    TINY_ARROW("animated/tiny_arrow", 12, 11),
    BUBBLES_BASE("animated/bubbles_base", 16, 32),
    BUBBLES("animated/bubbles", 16, 32),

    // Inventories
    BREWING_BARREL("container/brewing_barrel", 176, 166)
    ;

    public final ResourceLocation location;
    public final int width, height;

    AgresticSprites(ResourceLocation location, int width, int height) {
        this.location = location;
        this.width = width;
        this.height = height;
    }

    AgresticSprites(String location, int width, int height) {
        this.location = Agrestic.location("textures/gui/widgets/" + location + ".png");
        this.width = width;
        this.height = height;
    }
}
