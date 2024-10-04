package com.axialeaa.glissando.gui.screen;

import com.axialeaa.glissando.gui.widget.PreviewNoteKeyWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.BlockPos;

/**
 * The "fake" note block screen opened when clicking the "Preview GUI" button in the {@link com.axialeaa.glissando.config.GlissandoConfig config}. Opening this screen pauses the game in singleplayer mode, blurs the background, and returns to the config screen when closed.
 */
public class PreviewNoteBlockScreen extends AbstractNoteBlockScreen<PreviewNoteKeyWidget> {

    private final Screen screen;

    public PreviewNoteBlockScreen(Screen screen) {
        super("note_block_screen_preview");
        this.screen = screen;
    }

    @Override
    protected PreviewNoteKeyWidget createNewWidget(int x, int y, int pitch, BlockPos pos) {
        return new PreviewNoteKeyWidget(x, y, pitch, this);
    }

    @Override
    protected Screen getConfigScreen() {
        return this.screen;
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
            this.client.setScreen(this.screen);
    }

}
