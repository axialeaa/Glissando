package com.axialeaa.glissando.util;

import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.gui.widget.note_key.NoteKeyTextures;
import net.minecraft.text.Text;

/**
 * A simple enumerator storing the list of all possible notes from C through B, along with their {@link NoteKeyTextures textures} .
 */
public enum Note implements SimpleEnumStringIdentifiable {

    C       (NoteKeyTextures.NATURAL_LEFT),
    C_SHARP (NoteKeyTextures.ACCIDENTAL),
    D       (NoteKeyTextures.NATURAL),
    D_SHARP (NoteKeyTextures.ACCIDENTAL),
    E       (NoteKeyTextures.NATURAL_RIGHT),
    F       (NoteKeyTextures.NATURAL_LEFT),
    F_SHARP (NoteKeyTextures.ACCIDENTAL),
    G       (NoteKeyTextures.NATURAL),
    G_SHARP (NoteKeyTextures.ACCIDENTAL),
    A       (NoteKeyTextures.NATURAL),
    A_SHARP (NoteKeyTextures.ACCIDENTAL),
    B       (NoteKeyTextures.NATURAL_RIGHT);

    public final NoteKeyTextures textures;

    public static final Note[] KEYBOARD_NOTES = new Note[] {
        F_SHARP, G, G_SHARP, A, A_SHARP, B,
        C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B,
        C, C_SHARP, D, D_SHARP, E, F, F_SHARP,
    };

    Note(NoteKeyTextures textures) {
        this.textures = textures;
    }

    public boolean isTall() {
        return this.textures.height() == NoteKeyTextures.TALL_KEY_HEIGHT;
    }

    public boolean isAccidental() {
        return this.textures == NoteKeyTextures.ACCIDENTAL;
    }

    public Text getTranslation(int pitch, DataNoteBlockInstrument instrument) {
        return Text.translatable("glissando.note." + this.asString(), instrument.getSubscriptOctaveOf(pitch));
    }

    public static Note byPitch(int pitch) {
        return KEYBOARD_NOTES[pitch];
    }

}