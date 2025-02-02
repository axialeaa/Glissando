package com.axialeaa.glissando.data;

import com.axialeaa.glissando.data.provider.BlockTagProvider;
import com.axialeaa.glissando.data.provider.InstrumentTagProvider;
import com.axialeaa.glissando.data.provider.NoteBlockInstrumentProvider;
import com.axialeaa.glissando.data.registry.VanillaNoteBlockInstruments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import org.jetbrains.annotations.Nullable;

public class VanillaEffectiveDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(InstrumentTagProvider::new);
        pack.addProvider(NoteBlockInstrumentProvider::new);
    }

    @Override
    public @Nullable String getEffectiveModId() {
        return "minecraft";
    }

    @Override
    public void buildRegistry(RegistryBuilder builder) {
        builder.addRegistry(SerializableNoteBlockInstrument.REGISTRY_KEY, VanillaNoteBlockInstruments::bootstrap);
    }

}
