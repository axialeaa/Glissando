package com.axialeaa.glissando.config;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.option.*;
import com.axialeaa.glissando.gui.screen.PreviewNoteBlockScreen;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.List;

import static com.axialeaa.glissando.config.GlissandoConfig.*;

public class GlissandoConfigScreen {

    private static final String BEHAVIORS_CATEGORY = "behaviors";
    private static final String SCREEN_GROUP = "screen";
    private static final String INPUTS_GROUP = "inputs";

    private static final String VISUALS_CATEGORY = "visuals";
    private static final String BACKGROUND_GROUP = "background";
    private static final String TITLE_GROUP = "title";
    private static final String CONFIG_BUTTON_GROUP = "config_button";
    private static final String KEYBOARD_GROUP = "keyboard";
    private static final String TOOLTIPS_GROUP = "tooltips";

    private static final Text CONFIG_TITLE = Glissando.translate("config.title");

    private static final Text GENERIC_ENABLED = Glissando.translate("config.generic_enabled");
    private static final Text GENERIC_COLORS = Glissando.translate("config.generic_colors");

    public static Text getOptionTranslation(String name, boolean desc) {
        return Glissando.translate("config.option.%s.%s".formatted(name, desc ? "desc" : "name"));
    }

    private static Text getOptionName(String name) {
        return getOptionTranslation(name, false);
    }

    private static OptionDescription getOptionDesc(String name) {
        return OptionDescription.of(getOptionTranslation(name, true));
    }

    private static Text getGroupTranslation(String name, boolean desc) {
        return Glissando.translate("config.group.%s.%s".formatted(name, desc ? "desc" : "name"));
    }

    private static Text getGroupName(String name) {
        return getGroupTranslation(name, false);
    }

    private static OptionDescription getGroupDesc(String name) {
        return OptionDescription.of(getGroupTranslation(name, true));
    }

    private static Text getCategoryName(String name) {
        return Glissando.translate("config.category.%s".formatted(name));
    }

    private static OptionEntry createOption(Option<?> option) {
        return createOption(true, option);
    }

    private static OptionEntry createOption(boolean apply, Option<?> option) {
        return new OptionEntry(apply, option);
    }

    private static OptionGroup createGroup(String name, OptionEntry... options) {
        OptionGroup.Builder builder = OptionGroup.createBuilder()
            .name(getGroupName(name))
            .description(getGroupDesc(name));

        for (OptionEntry optionEntry : options)
            builder.optionIf(optionEntry.apply(), optionEntry.option());

        return builder.build();
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

            var keyboardLayout = Option.<KeyboardLayout>createBuilder()
                .name(getOptionName(KEYBOARD_LAYOUT))
                .description(getOptionDesc(KEYBOARD_LAYOUT))
                .binding(defaults.keyboardLayout, () -> config.keyboardLayout, value -> config.keyboardLayout = value)
                .controller(EnumDropdownControllerBuilder::create)
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
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).onOffFormatter())
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
                .addListener((option, event) -> configButtonPosition.setAvailable(option.pendingValue() || !option.available()))
                .build();

            var keyboardColorPredicate = Option.<KeyboardColorPredicate>createBuilder()
                .name(getOptionName(KEYBOARD_COLOR_PREDICATE))
                .description(GlissandoNameableEnum::getOptionDesc)
                .binding(defaults.keyboardColorPredicate, () -> config.keyboardColorPredicate, value -> config.keyboardColorPredicate = value)
                .controller(option -> EnumControllerBuilder.create(option).enumClass(KeyboardColorPredicate.class))
                .build();

            var tooltipTitleColor = Option.<Boolean>createBuilder()
                .name(getOptionName(TOOLTIP_TITLE_COLORS))
                .description(getOptionDesc(TOOLTIP_TITLE_COLORS))
                .binding(defaults.tooltipTitleColors, () -> config.tooltipTitleColors, value -> config.tooltipTitleColors = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).onOffFormatter())
                .build();

            var tooltipLineArrangement = ListOption.<TooltipType>createBuilder()
                .name(getGroupName(TOOLTIP_LINE_ARRANGEMENT))
                .description(getGroupDesc(TOOLTIP_LINE_ARRANGEMENT))
                .binding(defaults.tooltipLineArrangement, () -> config.tooltipLineArrangement, value -> config.tooltipLineArrangement = value)
                .controller(option -> EnumDropdownControllerBuilder.create(option).formatValue(GlissandoNameableEnum::getDisplayName))
                .initial(TooltipType.EMPTY)
                .minimumNumberOfEntries(0)
                .maximumNumberOfEntries(TooltipType.values().length - 1)
                .insertEntriesAtEnd(true)
                .build();

            var tooltips = Option.<Boolean>createBuilder()
                .name(GENERIC_ENABLED)
                .description(getOptionDesc(TOOLTIPS))
                .binding(defaults.tooltips, () -> config.tooltips, value -> config.tooltips = value)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true).yesNoFormatter())
                .addListener((option, event) -> {
                    boolean value = option.pendingValue() || !option.available();

                    tooltipTitleColor.setAvailable(value);
                    tooltipLineArrangement.setAvailable(value);
                })
                .build();

            return builder
                .title(CONFIG_TITLE)
                .categories(List.of(
                    ConfigCategory.createBuilder()
                        .name(getCategoryName(BEHAVIORS_CATEGORY))
                        .option(interactionMode)
                        .group(createGroup(SCREEN_GROUP,
                            createOption(openScreenWhenPlaced))
                        )
                        .group(createGroup(INPUTS_GROUP,
                            createOption(keyboardLayout)
                        ))
                        .build(),

                    ConfigCategory.createBuilder()
                        .name(getCategoryName(VISUALS_CATEGORY))
                        .option(previewGui)
                        .group(createGroup(BACKGROUND_GROUP,
                            //? if >=1.20.6
                            createOption(backgroundBlur),
                            createOption(backgroundStartColor),
                            createOption(backgroundEndColor)
                        ))
                        .group(createGroup(TITLE_GROUP,
                            createOption(titleColors),
                            createOption(showInstrument)
                        ))
                        .group(createGroup(CONFIG_BUTTON_GROUP,
                            createOption(Glissando.LOADER.isDevelopmentEnvironment() || Glissando.MOD_MENU_LOADED, configButton),
                            createOption(configButtonPosition)
                        ))
                        .group(createGroup(KEYBOARD_GROUP,
                            createOption(keyboardColorPredicate)
                        ))
                        .group(createGroup(TOOLTIPS_GROUP,
                            createOption(tooltips),
                            createOption(tooltipTitleColor)
                        ))
                        .groupIf(tooltips.pendingValue(), tooltipLineArrangement)
                        .build()
                    ))

                .save(CONFIG::save);
            }
        );
    }

}
