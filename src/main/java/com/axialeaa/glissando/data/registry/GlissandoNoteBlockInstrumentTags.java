package com.axialeaa.glissando.data.registry;

import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GlissandoNoteBlockInstrumentTags {

    TagKey<DataNoteBlockInstrument> UNTUNABLE = RegistryUtils.tagOf(DataNoteBlockInstrument.REGISTRY_KEY, "untunable");

    static void loadClass() {}

    class Provider extends FabricTagProvider<DataNoteBlockInstrument> {

        private static final List<RegistryKey<DataNoteBlockInstrument>> UNTUNABLE_INSTRUMENT_KEYS = List.of(
            GlissandoNoteBlockInstruments.CREEPER,
            GlissandoNoteBlockInstruments.CUSTOM_HEAD,
            GlissandoNoteBlockInstruments.DRAGON,
            GlissandoNoteBlockInstruments.PIGLIN,
            GlissandoNoteBlockInstruments.SKELETON,
            GlissandoNoteBlockInstruments.WITHER_SKELETON,
            GlissandoNoteBlockInstruments.ZOMBIE
        );

        public Provider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, DataNoteBlockInstrument.REGISTRY_KEY, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            TagBuilder untunable = this.getTagBuilder(UNTUNABLE);

            for (RegistryKey<DataNoteBlockInstrument> key : UNTUNABLE_INSTRUMENT_KEYS)
                untunable.add(key.getValue());
        }

    }

}
