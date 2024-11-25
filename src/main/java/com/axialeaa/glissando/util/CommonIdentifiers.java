package com.axialeaa.glissando.util;

import com.axialeaa.glissando.Glissando;
import net.minecraft.util.Identifier;

public class CommonIdentifiers {

    public static final Identifier TUNE_NOTE_BLOCK_PAYLOAD = Glissando.id("tune_note_block");
    public static final Identifier NOTE_BLOCK_INSTRUMENT_REGISTRY = Glissando.id("note_block_instrument");
    public static final Identifier UPSCALE_RESOURCE_PACK = Glissando.id("32x_upscale");
    public static final Identifier CONFIG_BUTTON_TEXTURE = Glissando.id(
        //? if >1.20.1 {
        "note_block/config"
        //?} else
        /*"textures/gui/sprites/note_block/config_button.png"*/
    );

}
