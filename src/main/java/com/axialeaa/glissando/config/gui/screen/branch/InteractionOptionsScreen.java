package com.axialeaa.glissando.config.gui.screen.branch;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.screen.MainConfigurationScreen;
import com.axialeaa.glissando.config.gui.widget.DirectionalLayoutBuilder;
import com.axialeaa.glissando.config.option.InteractionMode;
import net.minecraft.text.Text;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public class InteractionOptionsScreen extends SimpleOptionBranchScreen {

    public InteractionOptionsScreen(MainConfigurationScreen parentScreen) {
        super(Text.translatable("glissando.screen.interaction_options.title"), parentScreen);
    }

    @Override
    protected void initBody() {
        this.layout.addBody(new DirectionalLayoutBuilder(
            createCyclingBoolean(BUTTON_WIDTH, COMMON_BUTTON_SIZE, Glissando.clientConfig.openScreenWhenPlaced),
            createCyclingEnum(BUTTON_WIDTH, COMMON_BUTTON_SIZE, Glissando.clientConfig.interactionMode, InteractionMode.values(), true)
        ).vertical().x(w -> this.width / 2 - BUTTON_WIDTH / 2).build());
    }

}
