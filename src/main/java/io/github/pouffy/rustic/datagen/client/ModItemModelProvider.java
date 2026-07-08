package io.github.pouffy.rustic.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalItemModelProvider;
import io.github.pouffy.rustic.Rustic;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends KrystalItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Rustic.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
