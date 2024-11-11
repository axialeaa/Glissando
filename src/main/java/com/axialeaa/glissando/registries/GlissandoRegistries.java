package com.axialeaa.glissando.registries;

import com.axialeaa.glissando.Glissando;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class GlissandoRegistries {

    public static final RegistryKey<Registry<NoteBlockInstrument>> NOTE_BLOCK_INSTRUMENT = register("equipment", NoteBlockInstrument.CODEC);

    private static <T> RegistryKey<Registry<T>> register(String name, Codec<T> codec) {
        RegistryKey<Registry<T>> key = RegistryKey.ofRegistry(Glissando.id(name));
        DynamicRegistries.registerSynced(key, codec);

        return key;
    }

    public static void init() {}

}
