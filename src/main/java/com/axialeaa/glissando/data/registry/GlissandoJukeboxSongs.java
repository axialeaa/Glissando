package com.axialeaa.glissando.data.registry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;

import java.util.concurrent.CompletableFuture;

public interface GlissandoJukeboxSongs {

    RegistryKey<JukeboxSong> INTERNAL = RegistryUtils.keyOf(RegistryKeys.JUKEBOX_SONG, RegistryUtils.INTERNAL_PATH);

    static void bootstrap(Registerable<JukeboxSong> registerable) {
        registerable.register(INTERNAL, new JukeboxSong(RegistryEntry.of(GlissandoSoundEvents.INTERNAL), ScreenTexts.EMPTY, 0, 0));
    }

    static void loadClass() {}

    class Provider extends FabricDynamicRegistryProvider {

        public Provider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            entries.add(registries.getOrThrow(RegistryKeys.JUKEBOX_SONG), INTERNAL);
        }

        @Override
        public String getName() {
            return "Jukebox Songs";
        }

    }

}
