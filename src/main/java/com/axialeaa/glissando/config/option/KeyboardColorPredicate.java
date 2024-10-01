package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.config.GlissandoNameableEnum;
import com.axialeaa.glissando.util.NoteKeyTextures;
import net.minecraft.client.gui.DrawContext;

@SuppressWarnings("unused")
public enum KeyboardColorPredicate implements GlissandoNameableEnum {

    ALWAYS (NoteKeyTextures::drawWithColor),
    NEVER ((textures, context, x, y, pressed, hovered, color) -> textures.draw(context, x, y, pressed, hovered)),
    PRESSED ((textures, context, x, y, pressed, hovered, color) -> {
        KeyboardColorPredicate predicate = pressed ? ALWAYS : NEVER;
        predicate.draw(textures, context, x, y, pressed, hovered, color);
    });

    private final DrawNoteKeyTexturesConsumer consumer;

    KeyboardColorPredicate(DrawNoteKeyTexturesConsumer consumer) {
        this.consumer = consumer;
    }

    public void draw(NoteKeyTextures textures, DrawContext context, int x, int y, boolean pressed, boolean hovered, int color) {
        this.consumer.draw(textures, context, x, y, pressed, hovered, color);
    }

    @Override
    public String getOptionName() {
        return GlissandoConfig.KEYBOARD_COLOR_PREDICATE;
    }

    @FunctionalInterface
    public interface DrawNoteKeyTexturesConsumer {

        void draw(NoteKeyTextures textures, DrawContext context, int x, int y, boolean pressed, boolean hovered, int color);

    }

}
