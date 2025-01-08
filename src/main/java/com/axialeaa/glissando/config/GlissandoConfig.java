package com.axialeaa.glissando.config;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.option.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.awt.*;
import java.util.List;

public class GlissandoConfig {

    private static final String FILE_NAME = Glissando.MOD_ID + ".json";

    public static final String
        INTERACTION_MODE = "interaction_mode",
        OPEN_SCREEN_WHEN_PLACED = "open_screen_when_placed",
        KEYBOARD_LAYOUT = "keyboard_layout",

        PREVIEW_GUI = "preview_gui",
        SHOW_INSTRUMENT = "show_instrument",
        CONFIG_BUTTON = "config_button",
        CONFIG_BUTTON_POSITION = "config_button_position",
        TOOLTIP_LINE_ARRANGEMENT = "tooltip_line_arrangement",

        //? if >=1.20.6
        BACKGROUND_BLUR = "background_blur",
        BACKGROUND_START_COLOR = "background_start_color",
        BACKGROUND_END_COLOR = "background_end_color",
        TITLE_COLORS = "title_colors",
        KEYBOARD_COLOR_MODE = "keyboard_color_mode",
        TOOLTIP_TITLE_COLORS = "tooltip_title_colors",
        TOOLTIPS = "tooltips",
        TOOLTIP_TYPE = "tooltip_type",
        SOLMIZATION = "solmization";

    @SerialEntry(value = INTERACTION_MODE) public InteractionMode interactionMode = InteractionMode.RECLUSIVE;
    @SerialEntry(value = OPEN_SCREEN_WHEN_PLACED) public boolean openScreenWhenPlaced = true;
    @SerialEntry(value = KEYBOARD_LAYOUT) public KeyboardLayout keyboardLayout = KeyboardLayout.QWERTY;

    //? if >=1.20.6
    @SerialEntry(value = BACKGROUND_BLUR) public boolean backgroundBlur = false;
    @SerialEntry(value = BACKGROUND_START_COLOR) public Color backgroundStartColor = new Color(0xC00F0F0F, true);
    @SerialEntry(value = BACKGROUND_END_COLOR) public Color backgroundEndColor = new Color(0xD00F0F0F, true);
    @SerialEntry(value = TITLE_COLORS) public boolean titleColors = false;
    @SerialEntry(value = SHOW_INSTRUMENT) public boolean showInstrument = true;
    @SerialEntry(value = CONFIG_BUTTON) public boolean configButton = true;
    @SerialEntry(value = CONFIG_BUTTON_POSITION) public ConfigButtonPosition configButtonPosition = ConfigButtonPosition.LEFT;
    @SerialEntry(value = KEYBOARD_COLOR_MODE) public KeyboardColorMode keyboardColorMode = KeyboardColorMode.PRESSED;
    @SerialEntry(value = TOOLTIP_TITLE_COLORS) public boolean tooltipTitleColors = true;
    @SerialEntry(value = TOOLTIPS) public boolean tooltips = true;
    @SerialEntry(value = TOOLTIP_LINE_ARRANGEMENT) public List<TooltipType> tooltipLineArrangement = List.of(TooltipType.NOTE, TooltipType.PITCH);
    @SerialEntry(value = SOLMIZATION) public boolean solmization = false;

    public static final ConfigClassHandler<GlissandoConfig> CONFIG = ConfigClassHandler.createBuilder(GlissandoConfig.class)
        .serializer(config -> GsonConfigSerializerBuilder.create(config)
            .setPath(Glissando.LOADER.getConfigDir().resolve(FILE_NAME))
            .build()
        )
        .build();

    public static void load() {
        CONFIG.load();
    }

    public static GlissandoConfig get() {
        return CONFIG.instance();
    }

}
