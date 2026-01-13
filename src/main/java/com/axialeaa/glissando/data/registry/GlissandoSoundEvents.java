package com.axialeaa.glissando.data.registry;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundEvent;

import java.util.concurrent.CompletableFuture;

import static com.axialeaa.glissando.Glissando.id;

public interface GlissandoSoundEvents {

    String MUSIC_DISC_SCRATCH_PATH = "music_disc_scratch";
    SoundEvent MUSIC_DISC_SCRATCH = SoundEvent.of(id(MUSIC_DISC_SCRATCH_PATH));

    SoundEvent INTERNAL = SoundEvent.of(id(RegistryUtils.INTERNAL_PATH));

    class Provider extends FabricSoundsProvider {

        public Provider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registryLookup, SoundExporter exporter) {
            exporter.add(MUSIC_DISC_SCRATCH, SoundTypeBuilder.of()
                .subtitle("glissando.subtitles." + MUSIC_DISC_SCRATCH_PATH)
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(id("music_disc/scratch")))
            );
            exporter.add(INTERNAL, SoundTypeBuilder.of()
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(id(RegistryUtils.INTERNAL_PATH)))
            );
        }

        @Override
        public String getName() {
            return "Sounds Provider";
        }

    }

}
