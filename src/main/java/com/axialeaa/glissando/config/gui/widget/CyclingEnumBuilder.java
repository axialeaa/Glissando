package com.axialeaa.glissando.config.gui.widget;

import com.axialeaa.glissando.config.option.Option;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.util.StringIdentifiable;

public class CyclingEnumBuilder<T extends Enum<T> & StringIdentifiable> extends CyclingButtonWidget.Builder<T> {

    private int x = 0;
    private int y = 0;
    private int width;
    private int height;
    private final Option<T> option;

    public CyclingEnumBuilder(Option<T> option) {
        super(value -> option.getTranslation('.' + value.asString()));
        this.option = option;

        this.initially(option.get());
    }

    public CyclingEnumBuilder<T> x(int x) {
        this.x = x;
        return this;
    }

    public CyclingEnumBuilder<T> y(int y) {
        this.y = y;
        return this;
    }

    public CyclingEnumBuilder<T> pos(int x, int y) {
        return this.x(x).y(y);
    }

}
