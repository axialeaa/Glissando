package com.axialeaa.glissando.config;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.option.InteractionMode;
import com.axialeaa.glissando.config.option.KeyboardColorMode;
import com.axialeaa.glissando.config.option.OpenScreenMode;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.awt.*;

public class GlissandoConfig {

    static final String FILE_NAME = "%s.json".formatted(Glissando.MOD_ID);

    public static final String RESTORE_VANILLA = "restore_vanilla";
    public static final String OPEN_SCREEN_ON_PLACED = "open_screen_on_placed";
    public static final String OPEN_SCREEN_PREDICATE = "open_screen_predicate";
    public static final String KEYBIND_INPUTS = "keybind_inputs";
    public static final String INTERACTION_MODE = "interaction_mode";
    public static final String MOUSE_INPUTS = "mouse_inputs";

    public static final String PREVIEW_GUI = "preview_gui";
    public static final String NOTE_TOOLTIPS = "note_tooltips";
    public static final String KEYBIND_TOOLTIPS = "keybind_tooltips";
    public static final String BACKGROUND_START_COLOR = "background_start_color";
    public static final String BACKGROUND_END_COLOR = "background_end_color";
    public static final String TITLE_COLORS = "title_colors";
    public static final String KEYBOARD_COLOR_PREDICATE = "keyboard_color_predicate";
    public static final String TOOLTIP_COLORS = "tooltip_colors";

    @SerialEntry(value = RESTORE_VANILLA) public boolean restoreVanilla = false;
    @SerialEntry(value = OPEN_SCREEN_ON_PLACED) public boolean openScreenOnPlaced = true;
    @SerialEntry(value = OPEN_SCREEN_PREDICATE) public OpenScreenMode openScreenPredicate = OpenScreenMode.TUNABLE;
    @SerialEntry(value = KEYBIND_INPUTS) public boolean keybindInputs = true;
    @SerialEntry(value = INTERACTION_MODE) public InteractionMode interactionMode = InteractionMode.RECLUSIVE;
    @SerialEntry(value = MOUSE_INPUTS) public boolean mouseInputs = true;

    @SerialEntry(value = NOTE_TOOLTIPS) public boolean noteTooltips = true;
    @SerialEntry(value = KEYBIND_TOOLTIPS) public boolean keybindTooltips = true;
    @SerialEntry(value = BACKGROUND_START_COLOR) public Color backgroundStartColor = new Color(0xC00F0F0F, true);
    @SerialEntry(value = BACKGROUND_END_COLOR) public Color backgroundEndColor = new Color(0xD00F0F0F, true);
    @SerialEntry(value = TITLE_COLORS) public boolean titleColors = false;
    @SerialEntry(value = KEYBOARD_COLOR_PREDICATE) public KeyboardColorMode keyboardColorPredicate = KeyboardColorMode.PRESSED;
    @SerialEntry(value = TOOLTIP_COLORS) public boolean tooltipColors = true;

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
