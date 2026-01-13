package com.axialeaa.glissando.util;

import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public interface SimpleEnumStringIdentifiable extends StringIdentifiable {

    @Override
    default String asString() {
        return ((Enum<?>) this).name().toLowerCase(Locale.ROOT);
    }

}
