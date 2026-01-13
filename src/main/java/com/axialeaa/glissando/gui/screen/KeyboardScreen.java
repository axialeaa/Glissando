package com.axialeaa.glissando.gui.screen;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.option.KeyboardLayout;
import com.axialeaa.glissando.gui.widget.note_key.NoteKeyTextures;
import com.axialeaa.glissando.gui.widget.note_key.NoteKeyWidget;
import com.axialeaa.glissando.mixin.accessor.ScreenAccessor;
import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.util.ColorUtils;
import com.axialeaa.glissando.util.Note;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public abstract class KeyboardScreen extends ConfiguredBackgroundScreen {

    public static final int NATURAL_KEY_Y_POS = 120;
    public static final int KEYBOARD_WIDTH = 196;

    public static final int SEMITONE_X_OFFSET = (NoteKeyTextures.ACCIDENTAL_WIDTH - COMMON_SPACING) / 2;
    public static final int TALL_KEY_HEIGHT_DIFF = NoteKeyTextures.TALL_KEY_HEIGHT - NoteKeyTextures.NATURAL_HEIGHT;

    private static final int TALL_KEY_Y_POS = NATURAL_KEY_Y_POS - TALL_KEY_HEIGHT_DIFF;

    @Nullable public NoteKeyWidget selectedWidget;

    public final ObjectArrayList<NoteKeyWidget> noteKeyWidgets = new ObjectArrayList<>();

    @NotNull
    private DataNoteBlockInstrument instrument = DataNoteBlockInstrument.UNKNOWN;

    public KeyboardScreen(Text title) {
        super(title);
    }

    protected abstract NoteKeyWidget createNewWidget(int x, int y, int pitch, Note note);

    public abstract int getDefaultSelectedWidgetPitch();

    @NotNull
    public DataNoteBlockInstrument getInstrument() {
        return this.instrument;
    }

    public void setInstrument(@NotNull DataNoteBlockInstrument instrument) {
        this.instrument = instrument;
    }

    @Override
    protected void init() {
        this.noteKeyWidgets.clear();
        this.addKeys();

        this.setSelectedWidget(this.getWidgetFromPitch(this.getDefaultSelectedWidgetPitch()));
    }

    private void addKeys() {
        int keyboardStartX = this.width / 2 - KEYBOARD_WIDTH / 2;

        int naturalX = keyboardStartX;
        int accidentalX = keyboardStartX - SEMITONE_X_OFFSET;

        for (short pitch = 0; pitch < Note.KEYBOARD_NOTES.length; pitch++) {
            Note note = Note.byPitch(pitch);

            if (note.isAccidental()) {
                this.addKey(accidentalX, TALL_KEY_Y_POS, pitch);
                accidentalX += getOffsetForAccidental(note);

                continue;
            }

            this.addKey(naturalX, note.isTall() ? TALL_KEY_Y_POS : NATURAL_KEY_Y_POS, pitch); // moves tall keys up
            naturalX += NoteKeyTextures.NATURAL_WIDTH + COMMON_SPACING;
        }
    }

    private static int getOffsetForAccidental(Note note) {
        int newX = NoteKeyTextures.ACCIDENTAL_WIDTH + COMMON_SPACING;

        if (note == Note.F_SHARP || note == Note.G_SHARP)
            newX += COMMON_SPACING;
        else if (note == Note.A_SHARP || note == Note.D_SHARP)
            newX += NoteKeyTextures.NATURAL_WIDTH + SEMITONE_X_OFFSET;

        return newX;
    }

    private void addKey(int x, int y, short pitch) {
        NoteKeyWidget widget = this.createNewWidget(x, y, pitch, Note.byPitch(pitch));
        this.noteKeyWidgets.add(pitch, this.addDrawableChild(widget));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int pitch = this.getSelectedWidgetPitch();

        for (Drawable drawable : ((ScreenAccessor) this).getDrawables()) {
            if (drawable instanceof NoteKeyWidget widget) {
                widget.render(context, mouseX, mouseY, this.isSelectedWidget(widget));
                continue;
            }

            drawable.render(context, mouseX, mouseY, delta);
        }

        int color = Colors.WHITE;

        if (Glissando.clientConfig.titleColor.get() && this.selectedWidget != null)
            color = ColorUtils.getArgbPitchColor(pitch);

        this.renderTitleText(context, color);
    }

    private void renderTitleText(DrawContext context, int color) {
        context.drawCenteredTextWithShadow(this.textRenderer, this.getTitle(), this.width / 2, 40, color);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (NoteKeyWidget widget : this.noteKeyWidgets) {
            if (!widget.mouseClicked(mouseX, mouseY, button))
                continue;

            this.setSelectedWidget(widget);

            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (NoteKeyWidget widget : this.noteKeyWidgets) {
            if (!widget.isSelected() || !widget.keyPressed(keyCode, scanCode, modifiers))
                continue;

            this.setSelectedWidget(widget);

            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        int pitch = Glissando.clientConfig.keyboardLayout.get().getPitch(chr);

        if (pitch == KeyboardLayout.INVALID_CHAR_FLAG)
            return false;

        NoteKeyWidget widget = this.getWidgetFromPitch(pitch);

        if (!widget.charTyped(chr, modifiers))
            return false;

        this.setSelectedWidget(widget);

        return true;
    }

    public void setSelectedWidget(NoteKeyWidget widget) {
        this.selectedWidget = widget;
        this.setFocused(widget);
    }

    protected boolean isSelectedWidget(NoteKeyWidget widget) {
        return this.getSelectedWidgetPitch() == this.getPitchFromWidget(widget);
    }

    public int getSelectedWidgetPitch() {
        return this.getPitchFromWidget(this.selectedWidget);
    }

    private int getPitchFromWidget(NoteKeyWidget widget) {
        return this.noteKeyWidgets.indexOf(widget);
    }

    protected NoteKeyWidget getWidgetFromPitch(int pitch) {
        return this.noteKeyWidgets.get(pitch);
    }

}