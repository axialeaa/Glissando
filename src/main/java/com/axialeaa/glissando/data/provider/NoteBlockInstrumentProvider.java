package com.axialeaa.glissando.data.provider;

import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.*;

import java.util.concurrent.CompletableFuture;

public class NoteBlockInstrumentProvider extends FabricDynamicRegistryProvider {

    public NoteBlockInstrumentProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.addAll(registries. /*$ get_wrapper >>*/ getWrapperOrThrow (SerializableNoteBlockInstrument.REGISTRY_KEY));
    }

    @Override
    public String getName() {
        return "Note Block Instruments";
    }

}