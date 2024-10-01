package com.axialeaa.glissando.gui.screen;

import com.axialeaa.glissando.gui.widget.PreviewNoteKeyWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * The "fake" note block screen opened when clicking the "Preview GUI" button in the {@link com.axialeaa.glissando.config.GlissandoConfig config}. Opening this screen pauses the game in singleplayer mode, blurs the background, and returns to the config screen when closed.
 */
public class PreviewNoteBlockScreen extends AbstractNoteBlockScreen<PreviewNoteKeyWidget> {

    private final Screen parent;

    public PreviewNoteBlockScreen(Screen parent) {
        super(Text.translatable("glissando.note_block_screen.title.preview"));
        this.parent = parent;
    }

    @Override
    protected PreviewNoteKeyWidget createNewWidget(int x, int y, int pitch) {
        return new PreviewNoteKeyWidget(x, y, pitch);
    }

    @Override
    protected Screen getConfigScreen() {
        return this.parent;
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
    public void close() {
        super.close();

        if (this.client != null)
            this.client.setScreen(parent);
    }

}
