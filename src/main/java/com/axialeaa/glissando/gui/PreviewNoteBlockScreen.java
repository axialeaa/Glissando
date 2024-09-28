package com.axialeaa.glissando.gui;

import com.axialeaa.glissando.gui.widget.PreviewNoteKeyWidget;
import com.axialeaa.glissando.util.NoteKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.OptionalInt;

@Environment(EnvType.CLIENT)
public class PreviewNoteBlockScreen extends AbstractNoteBlockScreen<PreviewNoteKeyWidget> {

    private static final Text TITLE = Text.translatable("glissando.note_block_screen.title.preview");

    private final Screen returnScreen;
    private final HashSet<PreviewNoteKeyWidget> noteKeyWidgets = new HashSet<>();

    private OptionalInt currentSelectedPitch = OptionalInt.empty();

    public PreviewNoteBlockScreen(Screen returnScreen) {
        super(TITLE);
        this.returnScreen = returnScreen;
    }

    @Override
    public OptionalInt getCurrentSelectedPitch() {
        return this.currentSelectedPitch;
    }

    @Override
    protected void setCurrentSelectedPitch(int pitch) {
        this.currentSelectedPitch = OptionalInt.of(pitch);
    }

    @Override
    protected PreviewNoteKeyWidget createNewWidget(int x, int y, NoteKey noteKey) {
        return new PreviewNoteKeyWidget(x, y, noteKey, this.client);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //? if >=1.20.6 {
        if (this.client == null || this.client.world == null)
            this.renderPanoramaBackground(context, delta);

        this.applyBlur(delta);
        //?} else
        /*this.renderBackgroundTexture(context);*/

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected HashSet<PreviewNoteKeyWidget> getWidgets() {
        return this.noteKeyWidgets;
    }

    @Override
    public void close() {
        super.close();

        if (this.client != null)
            this.client.setScreen(returnScreen);
    }

}
