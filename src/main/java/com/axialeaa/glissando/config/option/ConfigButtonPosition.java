package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.util.SimpleEnumStringIdentifiable;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;
import org.joml.Vector2i;

import java.util.function.BiFunction;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public enum ConfigButtonPosition implements SimpleEnumStringIdentifiable {

    LEFT_OF_DONE((w, h) -> new Vector2i(w / 2 - COMMON_CLOSE_BUTTON_WIDTH / 2 - COMMON_SPACING - COMMON_BUTTON_SIZE, h / 4 + DONE_BUTTON_Y_POS)),
    RIGHT_OF_DONE((w, h) -> new Vector2i(w / 2 + COMMON_CLOSE_BUTTON_WIDTH / 2 + COMMON_SPACING, h / 4 + DONE_BUTTON_Y_POS)),
    FAR_LEFT_OF_DONE((w, h) -> new Vector2i(10, h / 4 + DONE_BUTTON_Y_POS)),
    FAR_RIGHT_OF_DONE((w, h) -> new Vector2i(w - 10 - COMMON_BUTTON_SIZE, h / 4 + DONE_BUTTON_Y_POS)),
    TOP_LEFT_OF_SCREEN((w, h) -> new Vector2i(10, 10)),
    TOP_RIGHT_OF_SCREEN((w, h) -> new Vector2i(w - 10 - COMMON_BUTTON_SIZE, 10)),
    BOTTOM_LEFT_OF_SCREEN((w, h) -> new Vector2i(10, h - 10 - COMMON_BUTTON_SIZE)),
    BOTTOM_RIGHT_OF_SCREEN((w, h) -> new Vector2i(w - 10 - COMMON_BUTTON_SIZE, h - 10 - COMMON_BUTTON_SIZE));

    private final BiFunction<Integer, Integer, Vector2i> positionFunction;

    public static final Codec<ConfigButtonPosition> CODEC = StringIdentifiable.createCodec(ConfigButtonPosition::values);

    ConfigButtonPosition(BiFunction<Integer, Integer, Vector2i> positionFunction) {
        this.positionFunction = positionFunction;
    }

    public Vector2i get(int screenWidth, int screenHeight) {
        return this.positionFunction.apply(screenWidth, screenHeight);
    }

    public int getX(int screenWidth, int screenHeight) {
        return this.get(screenWidth, screenHeight).x;
    }

    public int getY(int screenWidth, int screenHeight) {
        return this.get(screenWidth, screenHeight).y;
    }

}
