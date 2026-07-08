package io.github.pouffy.rustic.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalLanguageProvider;
import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalSoundsProvider;
import io.github.pouffy.rustic.Rustic;
import net.minecraft.data.PackOutput;

public class ModLanguageProvider extends KrystalLanguageProvider {

    public ModLanguageProvider(PackOutput output, KrystalSoundsProvider soundsProvider) {
        super(output, Rustic.MODID, "en_us", soundsProvider);
    }

    @Override
    protected void extraTranslations() {
        add("itemGroup.example", "Rustic Tab");
    }
}
