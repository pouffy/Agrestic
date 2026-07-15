package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.effect.IronSkin;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AgresticAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = Agrestic.getRegistryHelper().createRegister(NeoForgeRegistries.Keys.ATTACHMENT_TYPES);

    public static final Supplier<AttachmentType<IronSkin>> IRON_SKIN = ATTACHMENT_TYPES.register(
            "iron_skin", () -> AttachmentType.builder(() -> new IronSkin(false)).serialize(IronSkin.CODEC).sync(IronSkin.STREAM_CODEC).build()
    );

    public static void staticInit() {}
}
