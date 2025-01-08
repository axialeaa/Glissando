package com.axialeaa.glissando.gui.screen;

import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.gui.widget.NoteKeyWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

/**
 * The "fake" note block screen opened when clicking the "Preview GUI" button in the {@link com.axialeaa.glissando.config.GlissandoConfig config}. Opening this screen pauses the game in singleplayer mode, blurs the background, and returns to the config screen when closed.
 */
public class PreviewNoteBlockScreen extends AbstractNoteBlockScreen {

    private final Screen parentScreen;

    public PreviewNoteBlockScreen(Screen parentScreen) {
        super("note_block_screen_preview", SerializableNoteBlockInstrument.UNKNOWN);

        this.parentScreen = parentScreen;
    }

    @Override
    protected NoteKeyWidget createNewWidget(int x, int y, int pitch) {
        return new NoteKeyWidget(x, y, pitch, this, true, null);
    }

    @Override
    protected Screen getConfigScreen() {
        return this.parentScreen;
    }

    @Override
    public boolean shouldPause() {
        return this.parentScreen.shouldPause();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //? if >=1.20.6 {
        if (this.client == null || this.client.world == null)
            this.renderPanoramaBackground(context, delta);
        //?} else
        /*this.renderBackgroundTexture(context);*/

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        super.close();

        if (this.client != null)
            this.client.setScreen(this.parentScreen);
    }

}
