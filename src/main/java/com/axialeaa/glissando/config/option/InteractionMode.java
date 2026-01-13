package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.util.SimpleEnumStringIdentifiable;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

@SuppressWarnings("unused")
public enum InteractionMode implements SimpleEnumStringIdentifiable {

    SILENT, SOCIAL, RECLUSIVE;

    public static final Codec<InteractionMode> CODEC = StringIdentifiable.createCodec(InteractionMode::values);

}
