package com.axialeaa.glissando.data.instrument_formation;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.data.registry.RegistryUtils;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public interface InstrumentFormationType<T extends InstrumentFormation> {

    RegistryKey<Registry<InstrumentFormationType<?>>> REGISTRY_KEY = RegistryKey.ofRegistry(Glissando.id("instrument_formation_type"));
    Registry<InstrumentFormationType<?>> REGISTRY = FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();

    InstrumentFormationType<MultiBlockInstrumentFormation> MULTI_BLOCK = of("multi_block", MultiBlockInstrumentFormation.CODEC);
    InstrumentFormationType<SimpleInstrumentFormation> SIMPLE = of("simple", SimpleInstrumentFormation.CODEC);
    InstrumentFormationType<CustomHeadInstrumentFormation> CUSTOM_HEAD = of("custom_head", CustomHeadInstrumentFormation.CODEC);

    MapCodec<T> codec();

    static <T extends InstrumentFormation> InstrumentFormationType<T> of(String path, MapCodec<T> codec) {
        return RegistryUtils.of(REGISTRY, path, () -> codec);
    }

    static void loadClass() {}

}
