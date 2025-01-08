package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.gui.widget.NoteKeyTextures;
import net.minecraft.client.gui.DrawContext;

@SuppressWarnings("unused")
public enum KeyboardColorMode implements GlissandoNameableEnum {

    ALWAYS (NoteKeyTextures::drawWithColor),
    NEVER ((textures, context, x, y, pressed, hovered, color) -> textures.draw(context, x, y, pressed, hovered)),
    PRESSED ((textures, context, x, y, pressed, hovered, color) -> {
        KeyboardColorMode mode = pressed ? ALWAYS : NEVER;
        mode.draw(textures, context, x, y, pressed, hovered, color);
    });

    private final DrawConsumer consumer;

    KeyboardColorMode(DrawConsumer consumer) {
        this.consumer = consumer;
    }

    public void draw(NoteKeyTextures textures, DrawContext context, int x, int y, boolean pressed, boolean hovered, int color) {
        this.consumer.draw(textures, context, x, y, pressed, hovered, color);
    }

    @Override
    public String getOptionName() {
        return GlissandoConfig.KEYBOARD_COLOR_MODE;
    }

    @FunctionalInterface
    public interface DrawConsumer {

        void draw(NoteKeyTextures textures, DrawContext context, int x, int y, boolean pressed, boolean hovered, int color);

    }

}
