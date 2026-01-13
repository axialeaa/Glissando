package com.axialeaa.glissando.config.option;

// import com.axialeaa.glissando.config.groups.behaviors.InputsGroup;
import com.axialeaa.glissando.util.SimpleEnumStringIdentifiable;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

@SuppressWarnings("unused")
public enum KeyboardLayout implements SimpleEnumStringIdentifiable {

    QWERTY (
        '1', 'q', '2', 'w', '3', 'e', 'r', '5', 't', '6', 'y', 'u', '8', 'i', '9', 'o', '0', 'p',
        'z', 's', 'x', 'd', 'c', 'v', 'g'
    ),
    QWERTZ (
        '1', 'q', '2', 'w', '3', 'e', 'r', '5', 't', '6', 'z', 'u', '8', 'i', '9', 'o', '0', 'p',
        'y', 's', 'x', 'd', 'c', 'v', 'g'
    ),
    AZERTY (
        '1', 'a', '2', 'z', '3', 'e', 'r', '5', 't', '6', 'y', 'u', '8', 'i', '9', 'o', '0', 'p',
        'w', 's', 'x', 'd', 'c', 'v', 'g'
    ),
    QUERTY (
        '1', 'q', '2', 'ü', '3', 'e', 'r', '5', 't', '6', 'y', 'u', '8', 'i', '9', 'o', '0', 'p',
        'z', 's', 'x', 'd', 'c', 'v', 'g'
    ),
    AWERTY (
        '1', 'ä', '2', 'w', '3', 'e', 'r', '5', 't', '6', 'y', 'u', '8', 'i', '9', 'o', '0', 'p',
        'z', 's', 'ü', 'd', 'ç', 'ý', 'g'
    ),
    DVORAK (
        '1', '\'', '2', ',', '3', '.', 'p', '5', 'y', '6', 'f', 'g', '8', 'c', '9', 'r', '0', 'l',
        ';', 'o', 'q', 'e', 'j', 'k', 'i'
    );

    public static final Codec<KeyboardLayout> CODEC = StringIdentifiable.createCodec(KeyboardLayout::values);
    public static final int INVALID_CHAR_FLAG = -1;

    private final char[] chars;

    KeyboardLayout(char... chars) {
        this.chars = chars;
    }

    public char getChar(int pitch) {
        return this.chars[pitch];
    }

    public String getFormattedChar(int pitch) {
        return Character.toString(this.getChar(pitch)).toUpperCase(Locale.ROOT);
    }

    public int getPitch(char chr) {
        return CharArrayList.wrap(this.chars).indexOf(chr); // returns -1 if chr isn't present in the array
    }

/*    @Override
    public String getOptionName() {
        return InputsGroup.KEYBOARD_LAYOUT;
    }*/

}
