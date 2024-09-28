package com.axialeaa.glissando.config;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.option.InteractionMode;
import com.axialeaa.glissando.config.option.KeyboardColorMode;
import com.axialeaa.glissando.config.option.OpenScreenMode;
import com.axialeaa.glissando.gui.PreviewNoteBlockScreen;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.axialeaa.glissando.config.GlissandoConfig.*;

public class GlissandoConfigScreen {

    public static final Text TITLE = Text.translatable("%s.config.title".formatted(Glissando.MOD_ID));

    public static Text getOptionTranslation(String name, boolean desc) {
        return Text.translatable("%s.config.option.%s.%s".formatted(Glissando.MOD_ID, name, desc ? "desc" : "name"));
    }

    public static OptionDescription getOptionDesc(String name) {
        return OptionDescription.of(getOptionTranslation(name, true));
    }

    private static Text getGroupName(String name, boolean desc) {
        return Text.translatable("%s.config.group.%s.%s".formatted(Glissando.MOD_ID, name, desc ? "desc" : "name"));
    }

    private static Text getCategoryName(String name) {
        return Text.translatable("%s.config.category.%s".formatted(Glissando.MOD_ID, name));
    }

    private static ConfigCategory createCategory(String name, Option<?> option, OptionGroup... groups) {
        return ConfigCategory.createBuilder().name(getCategoryName(name)).option(option).groups(List.of(groups)).build();
    }

    private static OptionGroup createGroup(String name, Option<?>... options) {
        OptionDescription desc = OptionDescription.of(getGroupName(name, true));
        return OptionGroup.createBuilder().name(getGroupName(name, false)).description(desc).options(List.of(options)).build();
    }

    public static YetAnotherConfigLib configure() {
        return YetAnotherConfigLib.create(CONFIG, (defaults, config, builder) -> {
            var openScreenOnPlaced = Option.<Boolean>createBuilder()
                .name(getOptionTranslation(OPEN_SCREEN_ON_PLACED, false))
                .description(getOptionDesc(OPEN_SCREEN_ON_PLACED))
                .binding(defaults.openScreenOnPlaced, () -> config.openScreenOnPlaced, value -> config.openScreenOnPlaced = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .build();

            var openScreenPredicate = Option.<OpenScreenMode>createBuilder()
                .name(getOptionTranslation(OPEN_SCREEN_PREDICATE, false))
                .description(value -> OptionDescription.of(value.getTranslation(true)))
                .binding(defaults.openScreenPredicate, () -> config.openScreenPredicate, value -> config.openScreenPredicate = value)
                .controller(option -> EnumControllerBuilder.create(option).enumClass(OpenScreenMode.class))
                .listener((option, value) -> openScreenOnPlaced.setAvailable(value != OpenScreenMode.NEVER))
                .build();

            var interactionMode = Option.<InteractionMode>createBuilder()
                .name(getOptionTranslation(INTERACTION_MODE, false))
                .description(value -> OptionDescription.of(value.getTranslation(true)))
                .binding(defaults.interactionMode, () -> config.interactionMode, value -> config.interactionMode = value)
                .controller(option -> EnumControllerBuilder.create(option).enumClass(InteractionMode.class))
                .build();

            var mouseInputs = Option.<Boolean>createBuilder()
                .name(getOptionTranslation(MOUSE_INPUTS, false))
                .description(getOptionDesc(MOUSE_INPUTS))
                .binding(defaults.mouseInputs, () -> config.mouseInputs, value -> config.mouseInputs = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .build();

            var previewGui = ButtonOption.createBuilder()
                .name(getOptionTranslation(PREVIEW_GUI, false))
                .description(getOptionDesc(PREVIEW_GUI))
                .action((screen, option) -> {
                    screen.finishOrSave();
                    MinecraftClient.getInstance().setScreen(new PreviewNoteBlockScreen(screen));
                })
                .build();

            var backgroundStartColor = Option.<Color>createBuilder()
                .name(getOptionTranslation(BACKGROUND_START_COLOR, false))
                .description(getOptionDesc(BACKGROUND_START_COLOR))
                .binding(defaults.backgroundStartColor, () -> config.backgroundStartColor, value -> config.backgroundStartColor = value)
                .controller(option -> ColorControllerBuilder.create(option).allowAlpha(true))
                .build();

            var backgroundEndColor = Option.<Color>createBuilder()
                .name(getOptionTranslation(BACKGROUND_END_COLOR, false))
                .description(getOptionDesc(BACKGROUND_END_COLOR))
                .binding(defaults.backgroundEndColor, () -> config.backgroundEndColor, value -> config.backgroundEndColor = value)
                .controller(option -> ColorControllerBuilder.create(option).allowAlpha(true))
                .build();

            var titleColors = Option.<Boolean>createBuilder()
                .name(getOptionTranslation(TITLE_COLORS, false))
                .description(getOptionDesc(TITLE_COLORS))
                .binding(defaults.titleColors, () -> config.titleColors, value -> config.titleColors = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .build();

            var keyboardColorPredicate = Option.<KeyboardColorMode>createBuilder()
                .name(getOptionTranslation(KEYBOARD_COLOR_PREDICATE, false))
                .description(value -> OptionDescription.of(value.getTranslation(true)))
                .binding(defaults.keyboardColorPredicate, () -> config.keyboardColorPredicate, value -> config.keyboardColorPredicate = value)
                .controller(option -> EnumControllerBuilder.create(option).enumClass(KeyboardColorMode.class))
                .build();

            var tooltipColors = Option.<Boolean>createBuilder()
                .name(getOptionTranslation(TOOLTIP_COLORS, false))
                .description(getOptionDesc(TOOLTIP_COLORS))
                .binding(defaults.tooltipColors, () -> config.tooltipColors, value -> config.tooltipColors = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .build();

            AtomicBoolean tooltipColorsAvailable = new AtomicBoolean(config.keybindTooltips || config.noteTooltips);
            AtomicBoolean keybindTooltipsEnabled = new AtomicBoolean(config.keybindTooltips);

            var noteTooltips = Option.<Boolean>createBuilder()
                .name(getOptionTranslation(NOTE_TOOLTIPS, false))
                .description(getOptionDesc(NOTE_TOOLTIPS))
                .binding(defaults.noteTooltips, () -> config.noteTooltips, value -> config.noteTooltips = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .listener((option, value) -> {
                    tooltipColorsAvailable.set(keybindTooltipsEnabled.get() || value);
                    tooltipColors.setAvailable(tooltipColorsAvailable.get());
                })
                .build();

            var keybindTooltips = Option.<Boolean>createBuilder()
                .name(getOptionTranslation(KEYBIND_TOOLTIPS, false))
                .description(getOptionDesc(KEYBIND_TOOLTIPS))
                .binding(defaults.keybindTooltips, () -> config.keybindTooltips, value -> config.keybindTooltips = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .listener((option, value) -> {
                    keybindTooltipsEnabled.set(value);
                    tooltipColorsAvailable.set(noteTooltips.pendingValue() || value);

                    tooltipColors.setAvailable(tooltipColorsAvailable.get());
                })
                .build();

            var keybindInputs = Option.<Boolean>createBuilder()
                .name(getOptionTranslation(KEYBIND_INPUTS, false))
                .description(getOptionDesc(KEYBIND_INPUTS))
                .binding(defaults.keybindInputs, () -> config.keybindInputs, value -> config.keybindInputs = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .listener((option, value) -> {
                    interactionMode.setAvailable(value);
                    keybindTooltips.setAvailable(value);
                })
                .build();

            var restoreVanilla = Option.<Boolean>createBuilder()
                .name(getOptionTranslation(RESTORE_VANILLA, false))
                .description(getOptionDesc(RESTORE_VANILLA))
                .binding(defaults.restoreVanilla, () -> config.restoreVanilla, value -> config.restoreVanilla = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .listener((option, value) -> {
                    openScreenOnPlaced.setAvailable(!value);
                    openScreenPredicate.setAvailable(!value);
                    keybindInputs.setAvailable(!value);
                    interactionMode.setAvailable(!value);
                    mouseInputs.setAvailable(!value);
                    noteTooltips.setAvailable(!value);
                    keybindTooltips.setAvailable(!value);
                    keyboardColorPredicate.setAvailable(!value);
                    tooltipColors.setAvailable(!value);
                    titleColors.setAvailable(!value);
                    backgroundStartColor.setAvailable(!value);
                    backgroundEndColor.setAvailable(!value);
                })
                .build();

            return builder.title(TITLE)
                .categories(List.of(
                    createCategory("behaviors",
                        restoreVanilla,
                        createGroup("screen",
                            openScreenOnPlaced,
                            openScreenPredicate
                        ),
                        createGroup("keybinds",
                            keybindInputs,
                            interactionMode
                        ),
                        createGroup("mouse", mouseInputs)
                    ),
                    createCategory("visuals",
                        previewGui,
                        createGroup("tooltips",
                            noteTooltips,
                            keybindTooltips
                        ),
                        createGroup("colors",
                            backgroundStartColor,
                            backgroundEndColor,
                            titleColors,
                            keyboardColorPredicate,
                            tooltipColors
                        )
                    )
                ))
                .save(CONFIG::save);
            }
        );
    }

}
