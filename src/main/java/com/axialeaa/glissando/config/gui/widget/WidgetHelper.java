package com.axialeaa.glissando.config.gui.widget;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.option.Option;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import org.joml.Vector2i;

import java.util.function.Function;

public class WidgetHelper {

    public static final int COMMON_CLOSE_BUTTON_WIDTH = 200;
    public static final int DONE_BUTTON_Y_POS = 144;
    public static final int COMMON_BUTTON_SIZE = 20;
    public static final int HALF_COMMON_BUTTON_SIZE = 10;
    public static final int COMMON_TEXTURE_SIZE = 16;
    public static final int HALF_COMMON_TEXTURE_SIZE = 8;
    public static final int BUTTON_TEXTURE_MARGIN = 2; // HALF_COMMON_BUTTON_SIZE - HALF_COMMON_TEXTURE_SIZE
    public static final int SCREEN_MARGIN_SIZE = 10;
    public static final int COMMON_SPACING = 1;

    public static CheckboxWidget createCheckboxBoolean(int x, int y, Option<Boolean> option, TextRenderer textRenderer) {
        return CheckboxWidget.builder(option.getTranslatedName(), textRenderer)
            .callback((widget, checked) -> option.set(checked))
            .tooltip(Tooltip.of(option.getTranslatedTooltip()))
            .checked(option.get())
            .pos(x, y)
            .build();
    }

    public static <T> MultilineTextWidget createMultilineTooltip(int maxWidth, int maxRows, boolean centered, Option<T> option, Function<Option<T>, Text> defaultTextFunction, TextRenderer textRenderer) {
        return createMultilineTooltip(0, 0, maxWidth, maxRows, centered, option, defaultTextFunction, textRenderer);
    }

    public static <T> MultilineTextWidget createMultilineTooltip(int x, int y, int maxWidth, int maxRows, boolean centered, Option<T> option, Function<Option<T>, Text> defaultTextFunction, TextRenderer textRenderer) {
        Text description = createOptionDescription(option, ".multiline", defaultTextFunction);
        MultilineTextWidget textWidget = new MultilineTextWidget(description, textRenderer);

        textWidget.setPosition(x, y);
        textWidget.setMaxRows(maxRows);
        textWidget.setMaxWidth(maxWidth);
        textWidget.setCentered(centered);

        return textWidget;
    }

    public static CyclingButtonWidget<Boolean> createCyclingBoolean(int width, int height, Option<Boolean> option) {
        return createCyclingBoolean(0, 0, width, height, option);
    }

    public static CyclingButtonWidget<Boolean> createCyclingBoolean(int x, int y, int width, int height, Option<Boolean> option) {
        Function<Option<Boolean>, Text> defaultTextFunction = option1 -> option1.defaultValue ? ScreenTexts.ON : ScreenTexts.OFF;
        return CyclingButtonWidget.onOffBuilder(option.get())
            .tooltip(value -> createOptionTooltip(option, '.' + value.toString() + ".tooltip", defaultTextFunction))
            .build(x, y, width, height, option.getTranslatedName(), (widget, value) -> option.set(value));
    }

    public static <T extends Enum<T> & StringIdentifiable> CyclingButtonWidget<T> createCyclingEnum(int width, int height, Option<T> option, T[] values, boolean pertainTooltipToValue) {
        return createCyclingEnum(0, 0, width, height, option, values, pertainTooltipToValue);
    }

    public static <T extends Enum<T> & StringIdentifiable> CyclingButtonWidget<T> createCyclingEnum(int x, int y, int width, int height, Option<T> option, T[] values, boolean pertainTooltipToValue) {
        Function<T, Text> valueToText = value -> option.getTranslation('.' + value.asString());
        return CyclingButtonWidget.builder(valueToText)
            .values(values)
            .initially(option.get())
            .tooltip(value -> {
                String suffix = ".tooltip";

                if (pertainTooltipToValue)
                    suffix = '.' + value.asString() + suffix;

                return createOptionTooltip(option, suffix, option1 -> valueToText.apply(option1.defaultValue));
            })
            .build(x, y, width, height, option.getTranslatedName(), (widget, value) -> option.set(value));
    }

    public static <T> Text createOptionDescription(Option<T> option, String translationSuffix, Function<Option<T>, Text> defaultTextFunction) {
        MutableText tooltipText = option.getTranslation(translationSuffix).copy();
        tooltipText.append(ScreenTexts.LINE_BREAK);

        Text defaultValueText = Text.translatable("editGamerule.default", defaultTextFunction.apply(option)).formatted(Formatting.GRAY);
        tooltipText.append(defaultValueText);

        return tooltipText;
    }

    public static <T> Tooltip createOptionTooltip(Option<T> option, String translationSuffix, Function<Option<T>, Text> defaultTextFunction) {
        return Tooltip.of(createOptionDescription(option, translationSuffix, defaultTextFunction));
    }

    public static ButtonWidget createCenteredDoneButton(Screen screen) {
        return createCenteredCloseButton(ScreenTexts.DONE, screen);
    }

    public static ButtonWidget createCenteredCloseButton(Text text, Screen screen) {
        return createCenteredCloseButton(text, screen, screen.height / 4 + DONE_BUTTON_Y_POS);
    }

    public static ButtonWidget createCenteredCloseButton(Text text, Screen screen, int y) {
        int x = screen.width / 2 - COMMON_CLOSE_BUTTON_WIDTH / 2;
        return createCenteredCloseButton(text, screen, x, y, COMMON_CLOSE_BUTTON_WIDTH, COMMON_BUTTON_SIZE);
    }

    public static ButtonWidget createCenteredCloseButton(Text text, Screen screen, int x, int y, int width, int height) {
        return ButtonWidget.builder(text, button -> screen.close())
            .dimensions(x, y, width, height)
            .build();
    }

    public static TextIconButtonWidget createPositionedScreenChangeButton(Text text, Identifier texture, Screen screen, int screenWidth, int screenHeight) {
        Vector2i vec = Glissando.clientConfig.configButtonPosition.get().get(screenWidth, screenHeight);
        return createScreenChangeButton(vec.x, vec.y, text, texture, screen, true);
    }

    public static TextIconButtonWidget createScreenChangeButton(Text text, Identifier texture, Screen screen, boolean addTooltip) {
        return createScreenChangeButton(0, 0, text, texture, screen, addTooltip);
    }

    public static TextIconButtonWidget createScreenChangeButton(int x, int y, Text text, Identifier texture, Screen screen, boolean addTooltip) {
        return createSimpleButton(x, y, text, texture, widget -> MinecraftClient.getInstance().setScreen(screen), addTooltip);
    }

    public static TextIconButtonWidget createSimpleButton(Text text, Identifier texture, ButtonWidget.PressAction pressAction, boolean addTooltip) {
        return createSimpleButton(0, 0, text, texture, pressAction, addTooltip);
    }

    public static TextIconButtonWidget createSimpleButton(int x, int y, Text text, Identifier texture, ButtonWidget.PressAction pressAction, boolean addTooltip) {
        TextIconButtonWidget buttonWidget = TextIconButtonWidget
            .builder(text, pressAction, false)
            .dimension(COMMON_BUTTON_SIZE, COMMON_BUTTON_SIZE)
            .texture(texture, COMMON_TEXTURE_SIZE, COMMON_TEXTURE_SIZE)
            .build();

        buttonWidget.setPosition(x, y);

        if (addTooltip)
            buttonWidget.setTooltip(Tooltip.of(buttonWidget.getMessage(), null));

        return buttonWidget;
    }

}
