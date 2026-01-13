package com.axialeaa.glissando.data;

import com.axialeaa.glissando.data.provider.ItemTagProvider;
import com.axialeaa.glissando.data.provider.ModelProvider;
import com.axialeaa.glissando.data.registry.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class GlissandoDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(GlissandoBlockTags.Provider::new);
        pack.addProvider(GlissandoNoteBlockInstrumentTags.Provider::new);
        pack.addProvider(GlissandoNoteBlockInstruments.Provider::new);
        pack.addProvider(GlissandoJukeboxSongs.Provider::new);
        pack.addProvider(GlissandoSoundEvents.Provider::new);
        pack.addProvider(GlissandoRecipes.Provider::new);
        pack.addProvider(ItemTagProvider::new);
        pack.addProvider(ModelProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder builder) {
        builder.addRegistry(DataNoteBlockInstrument.REGISTRY_KEY, GlissandoNoteBlockInstruments::bootstrap);
        builder.addRegistry(RegistryKeys.JUKEBOX_SONG, GlissandoJukeboxSongs::bootstrap);
    }

}
