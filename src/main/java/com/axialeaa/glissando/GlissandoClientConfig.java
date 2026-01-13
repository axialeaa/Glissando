package com.axialeaa.glissando;

import com.axialeaa.glissando.config.AbstractConfig;
import com.axialeaa.glissando.config.option.*;

import java.util.ArrayList;
import java.util.List;

public class GlissandoClientConfig extends AbstractConfig {

    public Option<InteractionMode> interactionMode = this.ofEnum("interaction_mode", InteractionMode.SOCIAL, InteractionMode.CODEC, false);
    public Option<Boolean> openScreenWhenPlaced = this.ofBool("open_screen_when_placed", true, false);

    public Option<ConfigButtonPosition> configButtonPosition = this.ofEnum("config_button_position", ConfigButtonPosition.LEFT_OF_DONE, ConfigButtonPosition.CODEC, false);

    public Option<KeyboardLayout> keyboardLayout = this.ofEnum("keyboard_layout", KeyboardLayout.QWERTY, KeyboardLayout.CODEC, false);
    public Option<KeyboardColorCondition> keyboardColorCondition = this.ofEnum("keyboard_color_condition", KeyboardColorCondition.PRESSED, KeyboardColorCondition.CODEC, false);

    public Option<Boolean> backgroundBlur = this.ofBool("background_blur", false, false);
    public Option<Integer> backgroundTopColor = this.ofColor("background_top_color", 0xC00F0F0F, false);
    public Option<Integer> backgroundBottomColor = this.ofColor("background_bottom_color", 0xD00F0F0F, false);

    public Option<Boolean> titleColor = this.ofBool("title_color", false, false);

    // todo
    public Option<Boolean> enableTooltips = this.ofBool("enable_tooltips", true, false);
    // todo
    public Option<Boolean> tooltipTitleColors = this.ofBool("tooltip_title_colors", true, false);
    // todo
    public Option<List<TooltipType>> tooltipLineArrangement = this.ofList("tooltip_line_arrangement", new ArrayList<>(List.of(TooltipType.NOTE, TooltipType.PITCH)), TooltipType.CODEC, 0, 2, false);

    public NumberSliderOption.F pulseMagnitude = this.ofFloat("pulse_magnitude", 0.15F, 0, 1, false);
    public NumberSliderOption.I pulseBounces = this.ofInt("pulse_bounce_count", 1, 0, 6, false);
    public NumberSliderOption.I pulseAnimationTime = this.ofInt("pulse_animation_time", 8, 1, 200, false);

    public Option<Boolean> debugLogging = this.ofBool("debug_logging", false, false);
    public Option<Boolean> debugRendering = this.ofBool("debug_rendering", false, false);

}
