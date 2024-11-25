package com.axialeaa.glissando.data.provider;

import com.axialeaa.glissando.data.NoteBlockInstrumentTags;
import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.data.VanillaNoteBlockInstruments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class InstrumentTagProvider extends FabricTagProvider<SerializableNoteBlockInstrument> {

    public InstrumentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, SerializableNoteBlockInstrument.REGISTRY_KEY, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(NoteBlockInstrumentTags.TOP_INSTRUMENTS)
            .add(VanillaNoteBlockInstruments.CREEPER)
            .add(VanillaNoteBlockInstruments.CUSTOM_HEAD)
            .add(VanillaNoteBlockInstruments.DRAGON)
            .add(VanillaNoteBlockInstruments.PIGLIN)
            .add(VanillaNoteBlockInstruments.WITHER_SKELETON)
            .add(VanillaNoteBlockInstruments.ZOMBIE);
    }

}
