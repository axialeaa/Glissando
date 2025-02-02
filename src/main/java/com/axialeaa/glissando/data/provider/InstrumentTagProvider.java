package com.axialeaa.glissando.data.provider;

import com.axialeaa.glissando.data.registry.VanillaNoteBlockInstrumentTags;
import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.data.registry.VanillaNoteBlockInstruments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class InstrumentTagProvider extends FabricTagProvider<SerializableNoteBlockInstrument> {

    private static final List<RegistryKey<SerializableNoteBlockInstrument>> TOP_INSTRUMENT_KEYS = List.of(
        VanillaNoteBlockInstruments.CREEPER,
        VanillaNoteBlockInstruments.CUSTOM_HEAD,
        VanillaNoteBlockInstruments.DRAGON,
        VanillaNoteBlockInstruments.PIGLIN,
        VanillaNoteBlockInstruments.SKELETON,
        VanillaNoteBlockInstruments.WITHER_SKELETON,
        VanillaNoteBlockInstruments.ZOMBIE
    );

    public InstrumentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, SerializableNoteBlockInstrument.REGISTRY_KEY, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        FabricTagBuilder top = this.getOrCreateTagBuilder(VanillaNoteBlockInstrumentTags.TOP);
        FabricTagBuilder untunable = this.getOrCreateTagBuilder(VanillaNoteBlockInstrumentTags.UNTUNABLE);

        for (RegistryKey<SerializableNoteBlockInstrument> key : TOP_INSTRUMENT_KEYS) {
            top.add(key);
            untunable.add(key);
        }
    }

}
