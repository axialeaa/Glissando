package com.axialeaa.glissando.gui.widget;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.gui.screen.KeyboardScreen;
import com.axialeaa.glissando.gui.widget.note_key.NoteKeyWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.util.Identifier;

public class ResetSelectionWidget extends ButtonWidget {

    private static final Identifier UP_HIGHLIGHTED_SPRITE = Glissando.id("note_block/reset_selection_up_highlighted");
    private static final Identifier UP_SPRITE = Glissando.id("note_block/reset_selection_up");
    private static final Identifier DOWN_HIGHLIGHTED_SPRITE = Glissando.id("note_block/reset_selection_down_highlighted");
    private static final Identifier DOWN_SPRITE = Glissando.id("note_block/reset_selection_down");

    public static final int WIDTH = 11;
    public static final int HEIGHT = 7;

    private final KeyboardScreen screen;
    private final NoteKeyWidget selectedNoteKeyWidget;
    private final boolean accidental;

    public ResetSelectionWidget(int x, int y, KeyboardScreen screen, NoteKeyWidget selectedNoteKeyWidget, boolean accidental) {
        super(x, y, WIDTH, HEIGHT, ScreenTexts.EMPTY, widget -> getPressAction((ResetSelectionWidget) widget), DEFAULT_NARRATION_SUPPLIER);
        
        this.screen = screen;
        this.selectedNoteKeyWidget = selectedNoteKeyWidget;
        this.accidental = accidental;
    }

    private static void getPressAction(ResetSelectionWidget widget) {
        widget.screen.setSelectedWidget(widget.selectedNoteKeyWidget);

        widget.selectedNoteKeyWidget.playDownSound(MinecraftClient.getInstance().getSoundManager());
        widget.selectedNoteKeyWidget.onPress();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, this.getSprite(), this.getX(), this.getY(), this.width, this.height);
    }

    private Identifier getSprite() {
        if (this.accidental)
            return this.isHovered() ? DOWN_HIGHLIGHTED_SPRITE : DOWN_SPRITE;

        return this.isHovered() ? UP_HIGHLIGHTED_SPRITE : UP_SPRITE;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {}

}
