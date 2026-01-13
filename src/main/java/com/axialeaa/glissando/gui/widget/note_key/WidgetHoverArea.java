package com.axialeaa.glissando.gui.widget.note_key;

import com.axialeaa.glissando.gui.screen.KeyboardScreen;
import net.minecraft.client.gui.ScreenRect;

import java.util.*;

/**
 * Calculates whether a mouse position should register as being "over" a region. This is important because {@link NoteKeyTextures#NATURAL_LEFT} and {@link NoteKeyTextures#NATURAL_RIGHT} are compound rectangles which, with the vanilla behaviour, would overlap with the adjacent accidental unless special padding is applied.
 */
@FunctionalInterface
public interface WidgetHoverArea {

    WidgetHoverArea NATURAL = createBasic(NoteKeyTextures.NATURAL_WIDTH, NoteKeyTextures.NATURAL_HEIGHT);

    WidgetHoverArea NATURAL_TALL_BASE = (widgetX, widgetY) -> getAreaBounds(widgetX, widgetY + KeyboardScreen.TALL_KEY_HEIGHT_DIFF, NoteKeyTextures.NATURAL_WIDTH, NoteKeyTextures.NATURAL_HEIGHT);

    WidgetHoverArea NATURAL_LEFT = NATURAL_TALL_BASE.or((widgetX, widgetY) -> {
        int width = NoteKeyTextures.NATURAL_WIDTH - KeyboardScreen.SEMITONE_X_OFFSET;
        int height = KeyboardScreen.TALL_KEY_HEIGHT_DIFF;

        return getAreaBounds(widgetX, widgetY, width, height);
    });

    WidgetHoverArea NATURAL_RIGHT = NATURAL_TALL_BASE.or((widgetX, widgetY) -> {
        int minX = widgetX + KeyboardScreen.SEMITONE_X_OFFSET;
        int width = NoteKeyTextures.NATURAL_WIDTH - KeyboardScreen.SEMITONE_X_OFFSET;
        int height = KeyboardScreen.TALL_KEY_HEIGHT_DIFF;

        return getAreaBounds(minX, widgetY, width, height);
    });

    WidgetHoverArea ACCIDENTAL = createBasic(NoteKeyTextures.ACCIDENTAL_WIDTH, NoteKeyTextures.ACCIDENTAL_HEIGHT);

    Collection<ScreenRect> getAreaBounds(int widgetX, int widgetY);

    static WidgetHoverArea createBasic(int width, int height) {
        return (widgetX, widgetY) -> getAreaBounds(widgetX, widgetY, width, height);
    }

    static Collection<ScreenRect> getAreaBounds(int widgetX, int widgetY, int width, int height) {
        return List.of(new ScreenRect(widgetX, widgetY, width, height));
    }

    default boolean contains(int widgetX, int widgetY, int x, int y) {
        for (ScreenRect rect : this.getAreaBounds(widgetX, widgetY)) {
            if (rect.contains(x, y))
                return true;
        }

        return false;
    }

    default boolean intersects(int widgetX, int widgetY, ScreenRect other) {
        for (ScreenRect rect : this.getAreaBounds(widgetX, widgetY)) {
            if (rect.intersects(other))
                return true;
        }

        return false;
    }

    default WidgetHoverArea or(WidgetHoverArea other) {
        return (widgetX, widgetY) -> {
            Collection<ScreenRect> list = new ArrayList<>(this.getAreaBounds(widgetX, widgetY));
            list.addAll(other.getAreaBounds(widgetX, widgetY));

            return list;
        };
    }

}