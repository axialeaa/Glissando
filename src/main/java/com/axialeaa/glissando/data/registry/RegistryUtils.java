package com.axialeaa.glissando.data.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;

import static com.axialeaa.glissando.Glissando.id;

public class RegistryUtils {

    public static final String INTERNAL_PATH = "internal/internal";

    public static <V, T extends V> T of(Registry<V> registry, String path, T entry) {
        return Registry.register(registry, id(path), entry);
    }

    public static <T> TagKey<T> tagOf(RegistryKey<Registry<T>> registryKey, String path) {
        return TagKey.of(registryKey, id(path));
    }

    public static <T> RegistryKey<T> keyOf(RegistryKey<? extends Registry<T>> registryRegistryKey, String path) {
        return RegistryKey.of(registryRegistryKey, id(path));
    }

}
