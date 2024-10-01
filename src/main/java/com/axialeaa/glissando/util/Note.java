package com.axialeaa.glissando.util;

import static com.axialeaa.glissando.util.NoteKeyTextures.*;

/**
 * A simple enumerator storing the list of all possible notes from C through B, along with their textures.
 */
public enum Note {

    C       (false, NATURAL_LEFT),
    C_SHARP (true),
    D       (false),
    D_SHARP (true),
    E       (false, NATURAL_RIGHT),
    F       (false, NATURAL_LEFT),
    F_SHARP (true),
    G       (false),
    G_SHARP (true),
    A       (false),
    A_SHARP (true),
    B       (false, NATURAL_RIGHT);

    public final boolean accidental;
    public final NoteKeyTextures textures;

    Note(boolean accidental) {
        this(accidental, accidental ? ACCIDENTAL : NATURAL);
    }

    Note(boolean accidental, NoteKeyTextures textures) {
        this.accidental = accidental;
        this.textures = textures;
    }

    public int getWidth() {
        return this.textures.width();
    }

    public int getHeight() {
        return this.textures.height();
    }

    public boolean isTall() {
        return this.getHeight() == GlissandoUtils.KEYBOARD_HEIGHT;
    }

}