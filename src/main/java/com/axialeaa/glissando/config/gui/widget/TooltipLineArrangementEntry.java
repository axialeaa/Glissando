package com.axialeaa.glissando.config.gui.widget;

import com.axialeaa.glissando.config.option.Option;
import com.axialeaa.glissando.config.option.TooltipType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextWidget;

import java.util.ArrayList;
import java.util.List;

public class TooltipLineArrangementEntry extends ElementListWidget.Entry<TooltipLineArrangementEntry> {

    private TooltipType tooltipType;
    private Option<List<TooltipType>> option;
    private ButtonWidget moveDownButton;
    private ButtonWidget moveUpButton;
    private ButtonWidget deleteButton;
    private TextWidget textWidget;

    public TooltipLineArrangementEntry(TooltipType tooltipType, Option<List<TooltipType>> option) {
        this.tooltipType = tooltipType;
        this.option = option;
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(this.moveDownButton, this.moveUpButton, this.deleteButton);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(this.moveDownButton, this.moveUpButton, this.deleteButton, this.textWidget);
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickProgress) {

    }

}
