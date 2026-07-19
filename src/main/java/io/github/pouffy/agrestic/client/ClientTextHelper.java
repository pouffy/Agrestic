package io.github.pouffy.agrestic.client;

import com.mojang.authlib.GameProfile;
import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class ClientTextHelper {

    public static Component getQualityTooltip(float quality) {
        int qualityVariant = 0;

        Minecraft mc = Minecraft.getInstance();
        UUID playerId = null;
        if (mc.player != null) {
            playerId = mc.player.getUUID();
        } else {
            GameProfile profile = mc.getGameProfile();
            if (profile.getId() != null) {
                playerId = profile.getId();
            }
        }
        if (playerId != null) {
            if (playerId.equals(Agrestic.KRYSTAL_UUID)) {
                qualityVariant = 7;
            } else {
                qualityVariant = playerId.hashCode() & 7;
            }
        }

        String tier;
        ChatFormatting qualityColor;

        if (quality >= 0.89999F) {
            tier = "highest";
            qualityColor = ChatFormatting.GOLD;
        } else if (quality >= 0.69999F) {
            tier = "high";
            qualityColor = ChatFormatting.LIGHT_PURPLE;
        } else if (quality >= 0.5F) {
            tier = "highish";
            qualityColor = ChatFormatting.AQUA;
        } else if (quality >= 0.35F) {
            tier = "lowish";
            qualityColor = ChatFormatting.YELLOW;
        } else if (quality >= 0.2F) {
            tier = "low";
            qualityColor = ChatFormatting.DARK_PURPLE;
        } else {
            tier = "lowest";
            qualityColor = ChatFormatting.DARK_RED;
        }

        return Component.translatable("ui.agrestic.tooltip.quality.%s.%s".formatted(tier, qualityVariant), String.format("%.0f%%", quality * 100)).withStyle(qualityColor);
    }
}
