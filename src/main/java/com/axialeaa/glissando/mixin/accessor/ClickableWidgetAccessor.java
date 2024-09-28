package com.axialeaa.glissando.mixin.accessor;

import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;

//? if >=1.20.6 {
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.gui.tooltip.TooltipState;
//?} elif =1.20.1
/*import org.spongepowered.asm.mixin.gen.Invoker;*/

@Mixin(ClickableWidget.class)
public interface ClickableWidgetAccessor {

    //? if >=1.20.6 {
    @Accessor("tooltip")
    TooltipState getTooltipState();
    //?} elif =1.20.1 {
    /*@Invoker("applyTooltip")
    void invokeApplyTooltip();
    *///?}

}