package io.github.pouffy.rustic.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalSoundsProvider;
import io.github.pouffy.rustic.Rustic;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModSoundsProvider extends KrystalSoundsProvider {

    public ModSoundsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Rustic.MODID, helper);
    }

    @Override
    public void registerSounds() {

    }
}
