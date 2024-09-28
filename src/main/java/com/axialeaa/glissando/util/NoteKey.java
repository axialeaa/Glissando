package com.axialeaa.glissando.util;

import com.axialeaa.glissando.config.GlissandoConfig;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.stream.Stream;

import static com.axialeaa.glissando.util.NoteKeyTextureGroup.*;
import static org.lwjgl.glfw.GLFW.*;

public enum NoteKey {

    F_SHARP_2   (0, Note.F_SHARP, GLFW_KEY_1),
    G_2         (1, Note.G, GLFW_KEY_Q),
    G_SHARP_2   (2, Note.G_SHARP, GLFW_KEY_2),
    A_2         (3, Note.A, GLFW_KEY_W),
    A_SHARP_2   (4, Note.A_SHARP, GLFW_KEY_3),
    B_2         (5, Note.B, GLFW_KEY_E),
    C_3         (6, Note.C, GLFW_KEY_R),
    C_SHARP_3   (7, Note.C_SHARP, GLFW_KEY_5),
    D_3         (8, Note.D, GLFW_KEY_T),
    D_SHARP_3   (9, Note.D_SHARP, GLFW_KEY_6),
    E_3         (10, Note.E, GLFW_KEY_Y),
    F_3         (11, Note.F, GLFW_KEY_U),
    F_SHARP_3   (12, Note.F_SHARP, GLFW_KEY_8),
    G_3         (13, Note.G, GLFW_KEY_I),
    G_SHARP_3   (14, Note.G_SHARP, GLFW_KEY_9),
    A_3         (15, Note.A, GLFW_KEY_O),
    A_SHARP_3   (16, Note.A_SHARP, GLFW_KEY_0),
    B_3         (17, Note.B, GLFW_KEY_P),
    C_4         (18, Note.C, GLFW_KEY_Z),
    C_SHARP_4   (19, Note.C_SHARP, GLFW_KEY_S),
    D_4         (20, Note.D, GLFW_KEY_X),
    D_SHARP_4   (21, Note.D_SHARP, GLFW_KEY_D),
    E_4         (22, Note.E, GLFW_KEY_C),
    F_4         (23, Note.F, GLFW_KEY_V),
    F_SHARP_4   (24, Note.F_SHARP, GLFW_KEY_G);

    private final int pitch;
    private final Note note;
    private final int keyCode;

    public static final NoteKey[] ACCIDENTALS = Stream.of(values()).filter(NoteKey::isAccidental).toArray(NoteKey[]::new);
    public static final NoteKey[] NATURALS = Stream.of(values()).filter(noteKey -> !noteKey.isAccidental()).toArray(NoteKey[]::new);

    NoteKey(int pitch, Note note, int keyCode) {
        this.pitch = pitch;
        this.note = note;
        this.keyCode = keyCode;
    }

    public int getPitch() {
        return this.pitch;
    }

    public Note getNote() {
        return this.note;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public boolean isAccidental() {
        return this.note.accidental;
    }

    public NoteKeyTextureGroup getTextureGroup() {
        return this.note.textureGroup;
    }

    public int getWidth() {
        return this.getTextureGroup().width();
    }

    public int getHeight() {
        return this.getTextureGroup().height();
    }

    public boolean isTall() {
        return this.getHeight() == NoteBlockUtils.KEYBOARD_HEIGHT;
    }

    public MutableText getTooltipContents() {
        MutableText text = Text.empty();
        boolean newLine = false;

        if (GlissandoConfig.get().noteTooltips) {
            String key = "glissando.tooltip.note.%s".formatted(this.pitch);
            text.append(Text.translatable(key));

            newLine = true;
        }

        if (!GlissandoConfig.get().keybindTooltips)
            return text;

        if (newLine)
            text.append(Text.literal("\n"));

        InputUtil.Key inputKey = InputUtil.fromKeyCode(this.keyCode, 0);

        String localizedTooltip = Text.translatable("glissando.tooltip.press_key").getString();
        String localizedInputKey = inputKey.getLocalizedText().getString();

        return text.append(localizedTooltip.formatted(localizedInputKey));
    }

    public Tooltip getTooltip() {
        MutableText mutableText = this.getTooltipContents();

        if (GlissandoConfig.get().tooltipColors) {
            int color = NoteBlockUtils.getColorFromPitch(this.pitch);
            mutableText = mutableText.fillStyle(Style.EMPTY.withColor(color));
        }

        return Tooltip.of(mutableText);
    }

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

        private final boolean accidental;
        private final NoteKeyTextureGroup textureGroup;

        Note(boolean accidental) {
            this(accidental, accidental ? ACCIDENTAL : NATURAL);
        }

        Note(boolean accidental, NoteKeyTextureGroup textureGroup) {
            this.accidental = accidental;
            this.textureGroup = textureGroup;
        }

    }

}
