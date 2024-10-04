package com.axialeaa.glissando.config;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.option.*;
import com.axialeaa.glissando.gui.screen.PreviewNoteBlockScreen;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.axialeaa.glissando.config.GlissandoConfig.*;

public class GlissandoConfigScreen {

    private static final Text CONFIG_TITLE = Glissando.translate("config.title");

    private static final Text GENERIC_ENABLED = Glissando.translate("config.generic_enabled");
    private static final Text GENERIC_COLORS = Glissando.translate("config.generic_colors");

    static Text getOptionTranslation(String name, boolean desc) {
        return Glissando.translate("config.option.%s.%s".formatted(name, desc ? "desc" : "name"));
    }

    private static Text getOptionName(String name) {
        return getOptionTranslation(name, false);
    }

    private static OptionDescription getOptionDesc(String name) {
        return OptionDescription.of(getOptionTranslation(name, true));
    }

    private static Text getGroupName(String name, boolean desc) {
        return Glissando.translate("config.group.%s.%s".formatted(name, desc ? "desc" : "name"));
    }

    private static Text getCategoryName(String name) {
        return Glissando.translate("config.category.%s".formatted(name));
    }

    private static ConfigCategory createCategory(String name, Option<?> option, OptionGroup... groups) {
        return ConfigCategory.createBuilder().name(getCategoryName(name)).option(option).groups(List.of(groups)).build();
    }

    @SafeVarargs
    private static OptionGroup createGroup(String name, Pair<Boolean, Option<?>>... options) {
        OptionGroup.Builder builder = OptionGroup.createBuilder()
            .name(getGroupName(name, false))
            .description(OptionDescription.of(getGroupName(name, true)));

        for (Pair<Boolean, Option<?>> pair : options)
            builder.optionIf(pair.getLeft(), pair.getRight());

        return builder.build();
    }

    private static OptionGroup createGroup(String name, Option<?>... options) {
        return OptionGroup.createBuilder()
            .name(getGroupName(name, false))
            .description(OptionDescription.of(getGroupName(name, true)))
            .options(List.of(options)).build();
    }

    public static Screen create(Screen parent) {
        return configure().generateScreen(parent);
    }

    public static YetAnotherConfigLib configure() {
        return YetAnotherConfigLib.create(CONFIG, (defaults, config, builder) -> {
            var interactionMode = Option.<InteractionMode>createBuilder()
                .name(getOptionName(INTERACTION_MODE))
                .description(GlissandoNameableEnum::getOptionDesc)
                .binding(defaults.interactionMode, () -> config.interactionMode, value -> config.interactionMode = value)
                .controller(option -> EnumControllerBuilder.create(option).enumClass(InteractionMode.class))
                .build();

            var openScreenWhenPlaced = Option.<Boolean>createBuilder()
                .name(getOptionName(OPEN_SCREEN_WHEN_PLACED))
                .description(getOptionDesc(OPEN_SCREEN_WHEN_PLACED))
                .binding(defaults.openScreenWhenPlaced, () -> config.openScreenWhenPlaced, value -> config.openScreenWhenPlaced = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .build();

            var mouseInputs = Option.<Boolean>createBuilder()
                .name(getOptionName(MOUSE_INPUTS))
                .description(getOptionDesc(MOUSE_INPUTS))
                .binding(defaults.mouseInputs, () -> config.mouseInputs, value -> config.mouseInputs = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .build();

            var previewGui = ButtonOption.createBuilder()
                .name(getOptionName(PREVIEW_GUI))
                .description(getOptionDesc(PREVIEW_GUI))
                .action((screen, option) -> {
                    screen.finishOrSave();
                    MinecraftClient.getInstance().setScreen(new PreviewNoteBlockScreen(screen));
                })
                .build();

            //? if >=1.20.6 {
            var backgroundBlur = Option.<Boolean>createBuilder()
                .name(getOptionName(BACKGROUND_BLUR))
                .description(getOptionDesc(BACKGROUND_BLUR))
                .binding(defaults.backgroundBlur, () -> config.backgroundBlur, value -> config.backgroundBlur = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .build();
            //?}

            var backgroundStartColor = Option.<Color>createBuilder()
                .name(getOptionName(BACKGROUND_START_COLOR))
                .description(getOptionDesc(BACKGROUND_START_COLOR))
                .binding(defaults.backgroundStartColor, () -> config.backgroundStartColor, value -> config.backgroundStartColor = value)
                .controller(option -> ColorControllerBuilder.create(option).allowAlpha(true))
                .build();

            var backgroundEndColor = Option.<Color>createBuilder()
                .name(getOptionName(BACKGROUND_END_COLOR))
                .description(getOptionDesc(BACKGROUND_END_COLOR))
                .binding(defaults.backgroundEndColor, () -> config.backgroundEndColor, value -> config.backgroundEndColor = value)
                .controller(option -> ColorControllerBuilder.create(option).allowAlpha(true))
                .build();

            var titleColors = Option.<Boolean>createBuilder()
                .name(GENERIC_COLORS)
                .description(getOptionDesc(TITLE_COLORS))
                .binding(defaults.titleColors, () -> config.titleColors, value -> config.titleColors = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .build();

            var showInstrument = Option.<Boolean>createBuilder()
                .name(getOptionName(SHOW_INSTRUMENT))
                .description(getOptionDesc(SHOW_INSTRUMENT))
                .binding(defaults.showInstrument, () -> config.showInstrument, value -> config.showInstrument = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .build();

            var configButtonPosition = Option.<ConfigButtonPosition>createBuilder()
                .name(getOptionName(CONFIG_BUTTON_POSITION))
                .description(getOptionDesc(CONFIG_BUTTON_POSITION))
                .binding(defaults.configButtonPosition, () -> config.configButtonPosition, value -> config.configButtonPosition = value)
                .controller(option -> EnumControllerBuilder.create(option).enumClass(ConfigButtonPosition.class))
                .build();

            var configButton = Option.<Boolean>createBuilder()
                .name(GENERIC_ENABLED)
                .description(getOptionDesc(CONFIG_BUTTON))
                .binding(defaults.configButton, () -> config.configButton, value -> config.configButton = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .available(Glissando.LOADER.isDevelopmentEnvironment() || Glissando.MOD_MENU_LOADED)
                .listener((option, value) -> configButtonPosition.setAvailable(value || !option.available()))
                .build();

            var keyboardColorPredicate = Option.<KeyboardColorPredicate>createBuilder()
                .name(getOptionName(KEYBOARD_COLOR_PREDICATE))
                .description(GlissandoNameableEnum::getOptionDesc)
                .binding(defaults.keyboardColorPredicate, () -> config.keyboardColorPredicate, value -> config.keyboardColorPredicate = value)
                .controller(option -> EnumControllerBuilder.create(option).enumClass(KeyboardColorPredicate.class))
                .build();

            var tooltipColors = Option.<Boolean>createBuilder()
                .name(GENERIC_COLORS)
                .description(getOptionDesc(TOOLTIP_COLORS))
                .binding(defaults.tooltipColors, () -> config.tooltipColors, value -> config.tooltipColors = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .build();

            AtomicBoolean keybindTooltipsEnabled = new AtomicBoolean(config.keybindTooltips);
            AtomicBoolean pitchTooltipsEnabled = new AtomicBoolean(config.pitchTooltips);

            AtomicBoolean keybindInputsEnabled = new AtomicBoolean(config.keybindInputs);

            var noteTooltips = Option.<Boolean>createBuilder()
                .name(getOptionName(NOTE_TOOLTIPS))
                .description(getOptionDesc(NOTE_TOOLTIPS))
                .binding(defaults.noteTooltips, () -> config.noteTooltips, value -> config.noteTooltips = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .listener((option, value) -> tooltipColors.setAvailable(value || keybindTooltipsEnabled.get() || pitchTooltipsEnabled.get()))
                .build();

            var pitchTooltips = Option.<Boolean>createBuilder()
                .name(getOptionName(PITCH_TOOLTIPS))
                .description(getOptionDesc(PITCH_TOOLTIPS))
                .binding(defaults.pitchTooltips, () -> config.pitchTooltips, value -> config.pitchTooltips = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .listener((option, value) -> {
                    pitchTooltipsEnabled.set(value);
                    tooltipColors.setAvailable(noteTooltips.pendingValue() || keybindTooltipsEnabled.get() || value);
                })
                .build();

            var keybindTooltips = Option.<Boolean>createBuilder()
                .name(getOptionName(KEYBIND_TOOLTIPS))
                .description(getOptionDesc(KEYBIND_TOOLTIPS))
                .binding(defaults.keybindTooltips, () -> config.keybindTooltips, value -> config.keybindTooltips = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .listener((option, value) -> {
                    keybindTooltipsEnabled.set(keybindInputsEnabled.get() && value);
                    tooltipColors.setAvailable(noteTooltips.pendingValue() || pitchTooltips.pendingValue() || value);
                })
                .build();

            var keybindInputs = Option.<Boolean>createBuilder()
                .name(getOptionName(KEYBIND_INPUTS))
                .description(getOptionDesc(KEYBIND_INPUTS))
                .binding(defaults.keybindInputs, () -> config.keybindInputs, value -> config.keybindInputs = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .listener((option, value) -> {
                    keybindInputsEnabled.set(value);
                    keybindTooltipsEnabled.set(keybindTooltips.pendingValue() && value);
                    keybindTooltips.setAvailable(value);
                })
                .build();

            return builder.title(CONFIG_TITLE)
                .categories(List.of(
                    createCategory("behaviors",
                        interactionMode,
                        createGroup("screen", openScreenWhenPlaced),
                        createGroup("inputs",
                            keybindInputs,
                            mouseInputs
                        )
                    ),
                    createCategory("visuals",
                        previewGui,
                        createGroup("background",
                            //? if >=1.20.6
                            backgroundBlur,
                            backgroundStartColor,
                            backgroundEndColor
                        ),
                        createGroup("title",
                            titleColors,
                            showInstrument
                        ),
                        createGroup("config_button",
                            new Pair<>(Glissando.LOADER.isDevelopmentEnvironment() || Glissando.MOD_MENU_LOADED, configButton),
                            new Pair<>(true, configButtonPosition)
                        ),
                        createGroup("keyboard", keyboardColorPredicate),
                        createGroup("tooltips",
                            tooltipColors,
                            noteTooltips,
                            pitchTooltips,
                            keybindTooltips
                        )
                    )
                ))
                .save(CONFIG::save);
            }
        );
    }

}
