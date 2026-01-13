package com.axialeaa.glissando.config.gui.screen;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.leonard.Leonard;
import com.axialeaa.glissando.config.gui.leonard.LeonardButtonWidget;
import com.axialeaa.glissando.config.gui.screen.branch.*;
import com.axialeaa.glissando.config.gui.widget.DirectionalLayoutBuilder;
import com.axialeaa.glissando.gui.screen.KeyboardScreen;
import com.axialeaa.glissando.gui.screen.NoteBlockScreen;
import com.axialeaa.glissando.gui.widget.note_key.NoteKeyWidget;
import com.axialeaa.glissando.util.Note;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public class MainConfigurationScreen extends KeyboardScreen {

    private static final Identifier BACKGROUND_OPTIONS_TEXTURE = Glissando.id("config/options/background_options");
    private static final Identifier INTERACTION_OPTIONS_TEXTURE = Glissando.id("config/options/interaction_options");
    private static final Identifier PULSE_ANIMATION_OPTIONS_TEXTURE = Glissando.id("config/options/pulse_animation_options");
    private static final Identifier TOOLTIP_OPTIONS_TEXTURE = Glissando.id("config/options/tooltip_options");
    private static final Identifier KEYBOARD_SCREEN_OPTIONS_TEXTURE = Glissando.id("config/options/keyboard_screen_options");
    private static final Identifier CONFIG_BUTTON_POSITION_OPTION_TEXTURE = Glissando.id("config/config_button/config_button_position_option");

    private final Screen parentScreen;

    public MainConfigurationScreen(Screen parentScreen) {
        super(Text.translatable("glissando.screen.config.title"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected NoteKeyWidget createNewWidget(int x, int y, int pitch, Note note) {
        return new NoteKeyWidget(x, y, pitch, note, this, true, null);
    }

    @Override
    public int getDefaultSelectedWidgetPitch() {
        if (this.parentScreen instanceof NoteBlockScreen noteBlockScreen)
            return noteBlockScreen.getSelectedWidgetPitch();

        return 0;
    }

    @Override
    public void tick() {
        super.tick();

        if (Leonard.active)
            Leonard.onTick(this);
    }

    @Override
    protected void init() {
        super.init();

        DirectionalLayoutWidget layoutWidget = new DirectionalLayoutBuilder(
            createScreenChangeButton(
                Text.translatable("glissando.screen.background_options.title"),
                BACKGROUND_OPTIONS_TEXTURE,
                new BackgroundOptionsScreen(this),
                true
            ),
            createScreenChangeButton(
                Text.translatable("glissando.screen.interaction_options.title"),
                INTERACTION_OPTIONS_TEXTURE,
                new InteractionOptionsScreen(this),
                true
            ),
            createScreenChangeButton(
                Text.translatable("glissando.screen.tooltip_options.title"),
                TOOLTIP_OPTIONS_TEXTURE,
                new TooltipOptionsScreen(this),
                true
            ),
            createScreenChangeButton(
                Text.translatable("glissando.screen.keyboard_screen_options.title"),
                KEYBOARD_SCREEN_OPTIONS_TEXTURE,
                new KeyboardScreenOptionsScreen(this),
                true
            ),
            createScreenChangeButton(
                Text.translatable("glissando.screen.pulse_animation_options.title"),
                PULSE_ANIMATION_OPTIONS_TEXTURE,
                new PulseAnimationOptionsScreen(this),
                true
            ),
            new LeonardButtonWidget(this)
        )
            .vertical()
            .position(w -> SCREEN_MARGIN_SIZE, h -> this.height / 2 - h / 2 - COMMON_BUTTON_SIZE)
            .build();

        layoutWidget.forEachChild(this::addDrawableChild);
        Leonard.onScreenInit(SCREEN_MARGIN_SIZE + HALF_COMMON_BUTTON_SIZE, layoutWidget.getY() + layoutWidget.getHeight() - HALF_COMMON_BUTTON_SIZE);

        this.addDrawableChild(createPositionedScreenChangeButton(
            Text.translatable("glissando.screen.config_button_position.title"),
            CONFIG_BUTTON_POSITION_OPTION_TEXTURE,
            new ConfigButtonPositionOptionScreen(this),
            this.width,
            this.height
        ));
        this.addDrawableChild(createCenteredDoneButton(this));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float tickProgress) {
        super.render(context, mouseX, mouseY, tickProgress);
        Leonard.render(context, mouseX, mouseY, tickProgress);
    }

    @Override
    public void removed() {
        Leonard.close();
    }

    @Override
    public void close() {
        if (this.client != null)
            this.client.setScreen(this.parentScreen);
    }

}
