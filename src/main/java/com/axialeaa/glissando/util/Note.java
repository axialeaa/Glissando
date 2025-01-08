package com.axialeaa.glissando.util;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.gui.widget.NoteKeyTextures;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import static com.axialeaa.glissando.gui.widget.NoteKeyTextures.*;

/**
 * A simple enumerator storing the list of all possible notes from C through B, along with their {@link NoteKeyTextures textures} .
 */
public enum Note {

    C       ("c", NATURAL_LEFT),
    C_SHARP ("c_sharp", ACCIDENTAL),
    D       ("d", NATURAL),
    D_SHARP ("d_sharp", ACCIDENTAL),
    E       ("e", NATURAL_RIGHT),
    F       ("f", NATURAL_LEFT),
    F_SHARP ("f_sharp", ACCIDENTAL),
    G       ("g", NATURAL),
    G_SHARP ("g_sharp", ACCIDENTAL),
    A       ("a", NATURAL),
    A_SHARP ("a_sharp", ACCIDENTAL),
    B       ("b", NATURAL_RIGHT);

    private final String translationPath;
    public final NoteKeyTextures textures;

    public static final Note[] KEYBOARD_NOTES = new Note[] {
        Note.F_SHARP, Note.G, Note.G_SHARP, Note.A, Note.A_SHARP, Note.B,
        Note.C, Note.C_SHARP, Note.D, Note.D_SHARP, Note.E, Note.F, Note.F_SHARP, Note.G, Note.G_SHARP, Note.A, Note.A_SHARP, Note.B,
        Note.C, Note.C_SHARP, Note.D, Note.D_SHARP, Note.E, Note.F, Note.F_SHARP,
    };

    Note(String translationPath, NoteKeyTextures textures) {
        this.translationPath = translationPath;
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

    public boolean isAccidental() {
        return this.textures == ACCIDENTAL;
    }

    public Text getTranslatedName(int pitch, SerializableNoteBlockInstrument instrument) {
        String translationPath = this.translationPath;

        if (GlissandoConfig.get().solmization)
            translationPath += "." + GlissandoConfig.SOLMIZATION;

        return Glissando.translate("note." + translationPath, instrument.getSubscriptOctaveOf(pitch));
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

}