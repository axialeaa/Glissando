package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.config.GlissandoConfig;
import it.unimi.dsi.fastutil.chars.CharArrayList;

@SuppressWarnings("unused")
public enum KeyboardLayout implements GlissandoNameableEnum {

    QWERTY(CharArrayList.of(
        '1', 'q', '2', 'w', '3', 'e', 'r', '5', 't', '6', 'y', 'u', '8', 'i', '9', 'o', '0', 'p',
        'z', 's', 'x', 'd', 'c', 'v', 'g'
    )),
    QWERTZ(CharArrayList.of(
        '1', 'q', '2', 'w', '3', 'e', 'r', '5', 't', '6', 'z', 'u', '8', 'i', '9', 'o', '0', 'p',
        'y', 's', 'x', 'd', 'c', 'v', 'g'
    )),
    AZERTY(CharArrayList.of(
        '1', 'a', '2', 'z', '3', 'e', 'r', '5', 't', '6', 'y', 'u', '8', 'i', '9', 'o', '0', 'p',
        'w', 's', 'x', 'd', 'c', 'v', 'g'
    )),
    QUERTY(CharArrayList.of(
        '1', 'q', '2', 'ü', '3', 'e', 'r', '5', 't', '6', 'y', 'u', '8', 'i', '9', 'o', '0', 'p',
        'z', 's', 'x', 'd', 'c', 'v', 'g'
    )),
    AWERTY(CharArrayList.of(
        '1', 'ä', '2', 'w', '3', 'e', 'r', '5', 't', '6', 'y', 'u', '8', 'i', '9', 'o', '0', 'p',
        'z', 's', 'ü', 'd', 'ç', 'ý', 'g'
    ));

    private final CharArrayList chars;

    KeyboardLayout(CharArrayList chars) {
        this.chars = chars;
    }

    public char getChar(int pitch) {
        return this.chars.getChar(pitch);
    }

    public int getPitch(char chr) {
        return this.chars.indexOf(chr);
    }

    @Override
    public String getOptionName() {
        return GlissandoConfig.KEYBOARD_LAYOUT;
    }

}
