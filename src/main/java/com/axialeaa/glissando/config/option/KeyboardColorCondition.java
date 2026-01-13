package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.util.SimpleEnumStringIdentifiable;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

@SuppressWarnings("unused")
public enum KeyboardColorCondition implements SimpleEnumStringIdentifiable {

    ALL, NONE, PRESSED;

    public static final Codec<KeyboardColorCondition> CODEC = StringIdentifiable.createCodec(KeyboardColorCondition::values);

    public boolean canDrawColorable(boolean pressed) {
        return this == ALL || this == PRESSED && pressed;
    }

}
