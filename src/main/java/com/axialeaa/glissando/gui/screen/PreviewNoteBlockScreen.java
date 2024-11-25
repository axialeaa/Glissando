package com.axialeaa.glissando.gui.screen;

import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.gui.widget.PreviewNoteKeyWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * The "fake" note block screen opened when clicking the "Preview GUI" button in the {@link com.axialeaa.glissando.config.GlissandoConfig config}. Opening this screen pauses the game in singleplayer mode, blurs the background, and returns to the config screen when closed.
 */
public class PreviewNoteBlockScreen extends AbstractNoteBlockScreen<PreviewNoteKeyWidget> {

    private final Screen parentScreen;

    public PreviewNoteBlockScreen(Screen parentScreen) {
        super("note_block_screen_preview", null, SerializableNoteBlockInstrument.UNKNOWN);
        this.parentScreen = parentScreen;
    }

    @Override
    protected PreviewNoteKeyWidget createNewWidget(int x, int y, int pitch, @Nullable BlockPos pos) {
        return new PreviewNoteKeyWidget(x, y, pitch, this);
    }

    @Override
    protected Screen getConfigScreen() {
        return this.parentScreen;
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
