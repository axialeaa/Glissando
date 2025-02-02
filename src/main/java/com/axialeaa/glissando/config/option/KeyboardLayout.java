package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.config.GlissandoConfig;
import it.unimi.dsi.fastutil.chars.CharArrayList;

import java.util.Locale;

@SuppressWarnings("unused")
public enum KeyboardLayout implements GlissandoNameableEnum {

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
    );

    private final char[] chars;

    KeyboardLayout(char... chars) {
        this.chars = chars;
    }

    public char getChar(int pitch) {
        return this.chars[pitch];
    }

    public String getFormattedChar(int pitch) {
        return Character.valueOf(this.getChar(pitch)).toString().toUpperCase(Locale.ROOT);
    }

    public int getPitch(char chr) {
        return CharArrayList.wrap(this.chars).indexOf(chr);
    }

    @Override
    public String getOptionName() {
        return GlissandoConfig.KEYBOARD_LAYOUT;
    }

}
