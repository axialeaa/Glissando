package com.axialeaa.glissando.config.gui.widget;

import com.axialeaa.glissando.config.option.NumberSliderOption;
import com.axialeaa.glissando.config.option.Option;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.function.Function;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public class NumberSliderOptionWidget<T> extends SliderWidget {

    private final NumberSliderOption<T> option;
    private final Function<Option<T>, Text> invisibleNarrationTextFunction;

    public NumberSliderOptionWidget(int x, int y, int width, int height, NumberSliderOption<T> option, boolean addTooltip, Function<Option<T>, Text> invisibleNarrationTextFunction) {
        super(x, y, width, height, option.getTranslatedName(), option.getSliderProgress());
        this.option = option;
        this.invisibleNarrationTextFunction = invisibleNarrationTextFunction;

        if (addTooltip)
            this.setTooltip(createOptionTooltip(option, ".tooltip", option1 -> Text.of(option1.defaultValue.toString())));

        option.addOnValueChangedCallback((option1, value1) -> {
            this.value = ((NumberSliderOption<T>) option1).getSliderProgress(); // also serves as a way to increment the slider by steps
            this.updateMessage();
        });

        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        T value = this.toOptionValue();
        this.setMessage(ScreenTexts.composeGenericOptionText(option.getTranslatedName(), Text.of(value.toString())));
    }

    @Override
    protected void applyValue() {
        option.set(this.toOptionValue());
    }

    private T toOptionValue() {
        return option.toValue(this.value);
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        super.appendClickableNarrations(builder);
        builder.put(NarrationPart.HINT, this.invisibleNarrationTextFunction.apply(option));
    }

    public static <T> Builder<T> createBuilder(int width, NumberSliderOption<T> option) {
        return new Builder<>(width, option);
    }

    public static class Builder<T> {

        private int x = 0;
        private int y = 0;
        private boolean addTooltip = false;
        private final int width;
        private int height = COMMON_BUTTON_SIZE;
        private final NumberSliderOption<T> option;
        private Function<Option<T>, Text> invisibleNarrationTextFunction = option -> createOptionDescription(option, ".multiline", Option::getSimpleDefaultText);

        public Builder(int width, NumberSliderOption<T> option) {
            this.width = width;
            this.option = option;
        }

        public Builder<T> addTooltip() {
            this.addTooltip = true;
            return this;
        }

        public Builder<T> position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder<T> height(int height) {
            this.height = height;
            return this;
        }

        public Builder<T> invisibleNarrationTextFunction(Function<Option<T>, Text> invisibleNarrationTextFunction) {
            this.invisibleNarrationTextFunction = invisibleNarrationTextFunction;
            return this;
        }

        public NumberSliderOptionWidget<T> build() {
            return new NumberSliderOptionWidget<>(this.x, this.y, this.width, this.height, this.option, this.addTooltip, this.invisibleNarrationTextFunction);
        }

    }

}
