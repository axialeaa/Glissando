package com.axialeaa.glissando.util;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;

import java.util.Locale;

import static com.axialeaa.glissando.util.NoteKeyTextures.*;

/**
 * A simple enumerator storing the list of all possible notes from C through B, along with their {@link NoteKeyTextures textures} .
 */
public enum Note implements StringIdentifiable {

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

    public static final Note[] KEYBOARD_NOTES = new Note[]{
        Note.F_SHARP, Note.G, Note.G_SHARP, Note.A, Note.A_SHARP, Note.B,
        Note.C, Note.C_SHARP, Note.D, Note.D_SHARP, Note.E, Note.F, Note.F_SHARP, Note.G, Note.G_SHARP, Note.A, Note.A_SHARP, Note.B,
        Note.C, Note.C_SHARP, Note.D, Note.D_SHARP, Note.E, Note.F, Note.F_SHARP,
    };

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
        return this.getHeight() == GlissandoConstants.KEYBOARD_HEIGHT;
    }

    public static Note byPitch(int pitch) {
        return KEYBOARD_NOTES[pitch];
    }

    /**
     * @param pitch The pitch used to determine the color.
     * @return the color associated with {@code pitch} in ARGB format.
     * @see Note#getRgbColor(int)
     */
    public static int getArgbColor(int pitch) {
        return getRgbColor(pitch) | 0xFF000000;
    }

    /**
     * @param pitch The pitch used to determine the color.
     * @return the color associated with {@code pitch} in RGB format.
     */
    public static int getRgbColor(int pitch) {
        float delta = pitch / 24.0F;

        int red = (int) (Math.max(0.0F, MathHelper.sin(delta * MathHelper.TAU) * 0.65F + 0.35F) * 255);
        int green = (int) (Math.max(0.0F, MathHelper.sin((delta + (1.0F / 3.0F)) * MathHelper.TAU) * 0.65F + 0.35F) * 255);
        int blue = (int) (Math.max(0.0F, MathHelper.sin((delta + (2.0F / 3.0F)) * MathHelper.TAU) * 0.65F + 0.35F) * 255);

        return red << 16 | green << 8 | blue;
    }

    @Override
    public String asString() {
        return this.name().toLowerCase(Locale.ROOT);
    }

}