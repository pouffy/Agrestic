package io.github.pouffy.rustic.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalItemModelProvider;
import io.github.pouffy.rustic.Rustic;
import io.github.pouffy.rustic.init.RusticBlocks;
import io.github.pouffy.rustic.init.RusticItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends KrystalItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Rustic.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RusticBlocks.Woodset woodset : RusticBlocks.WOODSETS) {
            String name = woodset.planks().getId().getPath().replace("_planks", "");
            this.basicItem(woodset.door()::asItem, "wooden/%s/door".formatted(name));
        }
        this.basicItem(RusticItems.OLIVE_SIGN, "wooden/olive/sign");
        this.basicItem(RusticItems.OLIVE_HANGING_SIGN, "wooden/olive/hanging_sign");
        this.basicItem(RusticItems.IRONWOOD_SIGN, "wooden/ironwood/sign");
        this.basicItem(RusticItems.IRONWOOD_HANGING_SIGN, "wooden/ironwood/hanging_sign");
    }
}
