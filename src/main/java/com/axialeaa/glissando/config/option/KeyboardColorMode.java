package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.util.NoteKeyTextureGroup;
import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.Locale;

public enum KeyboardColorMode implements NameableEnum {

    NEVER ((textures, context, x, y, pressed, selected, color) -> textures.draw(context, x, y, pressed, selected)),
    ALWAYS (NoteKeyTextureGroup::drawWithColor),
    PRESSED ((textures, context, x, y, pressed, selected, color) -> {
        if (pressed)
            ALWAYS.consumer.draw(textures, context, x, y, true, selected, color);
        else NEVER.consumer.draw(textures, context, x, y, false, selected, color);
    });

    private final DrawNoteKeyTexturesConsumer consumer;

    KeyboardColorMode(DrawNoteKeyTexturesConsumer consumer) {
        this.consumer = consumer;
    }

    public void draw(NoteKeyTextureGroup textures, DrawContext context, int x, int y, boolean pressed, boolean selected, int color) {
        this.consumer.draw(textures, context, x, y, pressed, selected, color);
    }

    @Override
    public Text getDisplayName() {
        return this.getTranslation(false);
    }

    public String getString() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public Text getTranslation(boolean desc) {
        return Glissando.getOptionTranslation(GlissandoConfig.KEYBOARD_COLOR_PREDICATE + "." + this.getString(), desc);
    }

    @FunctionalInterface
    public interface DrawNoteKeyTexturesConsumer {

        void draw(NoteKeyTextureGroup textures, DrawContext context, int x, int y, boolean pressed, boolean selected, int color);

    }

}
