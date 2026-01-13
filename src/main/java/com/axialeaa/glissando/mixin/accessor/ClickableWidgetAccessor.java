package com.axialeaa.glissando.mixin.accessor;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.tooltip.TooltipState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClickableWidget.class)
public interface ClickableWidgetAccessor {

    @Accessor("tooltip")
    TooltipState getTooltipState();

}