package com.axialeaa.glissando.config.gui.widget;

import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.Widget;

import java.util.function.IntUnaryOperator;

public class DirectionalLayoutBuilder {

    private boolean vertical = false;
    private int spacing = 1;
    private IntUnaryOperator x = w -> 0;
    private IntUnaryOperator y = h -> 0;
    private final Widget[] widgets;

    public DirectionalLayoutBuilder(Widget... widgets) {
        this.widgets = widgets;
    }

    public DirectionalLayoutBuilder x(IntUnaryOperator x) {
        this.x = x;
        return this;
    }

    public DirectionalLayoutBuilder y(IntUnaryOperator y) {
        this.y = y;
        return this;
    }

    public DirectionalLayoutBuilder position(IntUnaryOperator x, IntUnaryOperator y) {
        return this.x(x).y(y);
    }

    public DirectionalLayoutBuilder vertical() {
        this.vertical = true;
        return this;
    }

    public DirectionalLayoutBuilder spacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    public DirectionalLayoutWidget build() {
        DirectionalLayoutWidget layoutWidget = (this.vertical ? DirectionalLayoutWidget.vertical() : DirectionalLayoutWidget.horizontal());
        layoutWidget.spacing(this.spacing);

        for (Widget widget : this.widgets)
            layoutWidget.add(widget);

        layoutWidget.refreshPositions();
        layoutWidget.setPosition(this.x.applyAsInt(layoutWidget.getWidth()), this.y.applyAsInt(layoutWidget.getHeight()));

        return layoutWidget;
    }

}
