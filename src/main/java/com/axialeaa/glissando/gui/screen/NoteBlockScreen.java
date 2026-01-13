package com.axialeaa.glissando.gui.screen;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.block_entity.NoteBlockEntity;
import com.axialeaa.glissando.config.gui.screen.MainConfigurationScreen;
import com.axialeaa.glissando.config.option.InteractionMode;
import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.gui.widget.ResetSelectionWidget;
import com.axialeaa.glissando.gui.widget.note_key.NoteKeyWidget;
import com.axialeaa.glissando.util.Note;
import net.minecraft.block.NoteBlock;

import com.axialeaa.glissando.payload.TuneNoteBlockC2SPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

/**
 * The "normal" note block screen opened when interacting with a note block.
 */
public class NoteBlockScreen extends KeyboardScreen {

    private static final Identifier CONFIG_BUTTON_TEXTURE = Glissando.id("config/config_button/config_button");

    private final NoteBlockEntity blockEntity;
    private final DataNoteBlockInstrument instrument;

    public NoteBlockScreen(NoteBlockEntity blockEntity, DataNoteBlockInstrument instrument) {
        super(Text.translatable("glissando.screen.note_block.title", instrument.description().getString()));

        this.blockEntity = blockEntity;
        this.instrument = instrument;
    }

    @Override
    protected NoteKeyWidget createNewWidget(int x, int y, int pitch, Note note) {
        return new NoteKeyWidget(x, y, pitch, note, this, false, this.blockEntity.getPos());
    }

    @Override
    public int getDefaultSelectedWidgetPitch() {
        return this.blockEntity.getCachedState().get(NoteBlock.NOTE);
    }

    @Override
    public void removed() {
        InteractionMode mode = Glissando.clientConfig.interactionMode.get();

        if (mode == InteractionMode.SOCIAL || this.selectedWidget == null)
            return;

        short pitch = (short) this.getSelectedWidgetPitch();

        if (this.blockEntity.getCachedState().get(NoteBlock.NOTE) != pitch)
            TuneNoteBlockC2SPayload.send(this.blockEntity.getPos(), pitch, mode == InteractionMode.RECLUSIVE);
    }

    @Override
    protected void init() {
        super.init();
        this.setInstrument(this.instrument);

        this.addResetSelectionButton();
        this.addDrawableChild(createCenteredDoneButton(this));
        this.addDrawableChild(createPositionedScreenChangeButton(
            Text.translatable("glissando.screen.config.title"),
            CONFIG_BUTTON_TEXTURE,
            new MainConfigurationScreen(this),
            this.width,
            this.height
        ));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void tick() {
        for (NoteKeyWidget widget : this.noteKeyWidgets)
            widget.updateTooltip();
    }

    private void addResetSelectionButton() {
        if (this.selectedWidget == null)
            return;

        int x = this.selectedWidget.getX(), y = this.selectedWidget.getY();
        Note note = this.selectedWidget.note;

        boolean accidental = note.isAccidental();

        if (!accidental)
            x += COMMON_SPACING;

        y += accidental ? -(ResetSelectionWidget.HEIGHT + COMMON_SPACING) : this.selectedWidget.getHeight() + COMMON_SPACING;

        this.addDrawableChild(new ResetSelectionWidget(x, y, this, this.selectedWidget, accidental));
    }

}
