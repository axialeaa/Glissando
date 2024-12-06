package com.axialeaa.glissando.util;

import com.axialeaa.glissando.gui.widget.NoteKeyTextures;

import static com.axialeaa.glissando.util.GlissandoConstants.*;

/**
 * Calculates whether a mouse position should register as being "over" a region. This is important because {@link NoteKeyTextures#NATURAL_LEFT} and {@link NoteKeyTextures#NATURAL_RIGHT} are compound rectangles which, with the vanilla behaviour, would overlap with the adjacent accidental unless special padding is applied.
 * @see Rectangle The rectangle helper class
 */
@FunctionalInterface
public interface CursorHoverChecker {

    CursorHoverChecker NATURAL = (x, y, mouseX, mouseY) -> Rectangle.create(x, y, NATURAL_KEY_WIDTH, NATURAL_KEY_HEIGHT).isCoordinateIn(mouseX, mouseY);
    CursorHoverChecker NATURAL_TALL_BASE = (x, y, mouseX, mouseY) -> Rectangle.create(x, y + TALL_KEY_HEIGHT_DIFF, NATURAL_KEY_WIDTH, NATURAL_KEY_HEIGHT).isCoordinateIn(mouseX, mouseY);
    CursorHoverChecker NATURAL_LEFT = NATURAL_TALL_BASE.or((x, y, mouseX, mouseY) -> {
        int maxX = x + NATURAL_KEY_WIDTH - SEMITONE_OFFSET;
        int maxY = y + TALL_KEY_HEIGHT_DIFF;

        return new Rectangle(x, y, maxX, maxY).isCoordinateIn(mouseX, mouseY);
    });
    CursorHoverChecker NATURAL_RIGHT = NATURAL_TALL_BASE.or((x, y, mouseX, mouseY) -> {
        int minX = x + SEMITONE_OFFSET;
        int maxX = x + NATURAL_KEY_WIDTH;
        int maxY = y + TALL_KEY_HEIGHT_DIFF;

        return new Rectangle(minX, y, maxX, maxY).isCoordinateIn(mouseX, mouseY);
    });
    CursorHoverChecker ACCIDENTAL = (x, y, mouseX, mouseY) -> Rectangle.create(x, y, ACCIDENTAL_KEY_WIDTH, ACCIDENTAL_KEY_HEIGHT).isCoordinateIn(mouseX, mouseY);

    boolean check(int x, int y, double mouseX, double mouseY);

    default CursorHoverChecker or(CursorHoverChecker other) {
        return (x, y, mouseX, mouseY) -> check(x, y, mouseX, mouseY) || other.check(x, y, mouseX, mouseY);
    }

}