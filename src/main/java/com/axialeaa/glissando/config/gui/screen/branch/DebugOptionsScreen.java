package com.axialeaa.glissando.config.gui.screen.branch;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.screen.MainConfigurationScreen;
import com.axialeaa.glissando.config.gui.widget.DirectionalLayoutBuilder;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.text.Text;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public class DebugOptionsScreen extends SimpleOptionBranchScreen {

    public DebugOptionsScreen(MainConfigurationScreen parentScreen) {
        super(Text.translatable("glissando.screen.debug_options.title"), parentScreen);
    }

    @Override
    protected void initBody() {
        this.layout.addBody(new DirectionalLayoutBuilder(
            createCyclingBoolean(BUTTON_WIDTH, COMMON_BUTTON_SIZE, Glissando.clientConfig.debugRendering),
            createCyclingBoolean(BUTTON_WIDTH, COMMON_BUTTON_SIZE, Glissando.clientConfig.debugLogging)
        ).vertical().x(w -> this.width / 2 - BUTTON_WIDTH / 2).build());
    }

}
