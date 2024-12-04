package com.axialeaa.glissando.util;

import com.axialeaa.glissando.Glissando;
import net.minecraft.util.Identifier;

public class GlissandoConstants {

    public static final int
        NOTES_IN_OCTAVE = 12,
        FIRST_C_ORDINAL = 6,
        SECOND_C_ORDINAL = FIRST_C_ORDINAL + NOTES_IN_OCTAVE,
        KEYBOARD_WIDTH = 196,
        KEYBOARD_HEIGHT = 65,
        NATURAL_KEY_WIDTH = 13,
        ACCIDENTAL_KEY_WIDTH = 11,
        NATURAL_KEY_HEIGHT = 32,
        ACCIDENTAL_KEY_HEIGHT = 32,
        KEY_PADDING = 1,
        SEMITONE_OFFSET = (ACCIDENTAL_KEY_WIDTH - KEY_PADDING) / 2,
        TALL_KEY_HEIGHT_DIFF = KEYBOARD_HEIGHT - NATURAL_KEY_HEIGHT,
        NATURAL_KEY_Y_POS = 120,
        TALL_KEY_Y_POS = NATURAL_KEY_Y_POS - TALL_KEY_HEIGHT_DIFF,
        BUTTON_HEIGHT = 144,
        CONFIG_BUTTON_SIZE = 20,
        //? >1.20.1
        CONFIG_BUTTON_TEXTURE_SIZE = 16,
        DEFAULT_DONE_BUTTON_WIDTH = 200,
        DEFAULT_DONE_BUTTON_OFFSET = -(DEFAULT_DONE_BUTTON_WIDTH / 2),
        OFFSET_DONE_BUTTON_WIDTH = DEFAULT_DONE_BUTTON_WIDTH - CONFIG_BUTTON_SIZE;

    public static final Identifier
        TUNE_NOTE_BLOCK_PAYLOAD = Glissando.id("tune_note_block"),
        NOTE_BLOCK_INSTRUMENT_REGISTRY = Glissando.id("note_block_instrument"),
        UPSCALE_RESOURCE_PACK = Glissando.id("32x_upscale"),
        CONFIG_BUTTON_TEXTURE = Glissando.id(
            //? if >1.20.1 {
            "note_block/config"
            //?} else
            /*"textures/gui/sprites/note_block/config_button.png"*/
        );

}
