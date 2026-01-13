package com.axialeaa.glissando.item;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.widget.WidgetHelper;
import com.axialeaa.glissando.gui.screen.ConfiguredBackgroundScreen;
import com.axialeaa.glissando.item.component.CustomDiscComponent;
import com.axialeaa.glissando.payload.EntitleCustomMusicDisc;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

public class CustomMusicDiscScreen extends ConfiguredBackgroundScreen {

    private static final int BACKGROUND_DISC_SPRITE_SIZE = 180;
    private static final Identifier BACKGROUND_DISC_SPRITE = Glissando.id("textures/gui/sprites/music_disc.png");

    private final Hand hand;
    private final ItemStack stack;
    private String title;

    public CustomMusicDiscScreen(Hand hand, ItemStack stack) {
        super(Text.translatable("item.glissando.music_disc.entitle"));
        this.hand = hand;
        this.stack = stack;
        this.title = CustomDiscComponent.getTitle(stack);
    }

    @Override
    protected void init() {
        TextFieldWidget textFieldWidget = new TextFieldWidget(
            this.textRenderer,
            this.width / 2 - BACKGROUND_DISC_SPRITE_SIZE / 2,
            this.height / 2,
            BACKGROUND_DISC_SPRITE_SIZE,
            WidgetHelper.COMMON_BUTTON_SIZE,
            ScreenTexts.EMPTY
        );

        textFieldWidget.setText(this.title);
        textFieldWidget.setChangedListener(string -> this.title = string);

        this.addDrawableChild(textFieldWidget);
        this.addDrawableChild(WidgetHelper.createCenteredDoneButton(this));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.drawCenteredTextWithShadow(this.textRenderer, this.getTitle(), this.width / 2, 40, -1);
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            BACKGROUND_DISC_SPRITE,
            this.width / 2 - BACKGROUND_DISC_SPRITE_SIZE / 2,
            this.height / 2 - BACKGROUND_DISC_SPRITE_SIZE / 2,
            0,
            0,
            BACKGROUND_DISC_SPRITE_SIZE,
            BACKGROUND_DISC_SPRITE_SIZE,
            BACKGROUND_DISC_SPRITE_SIZE,
            BACKGROUND_DISC_SPRITE_SIZE
        );

        super.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void close() {
        super.close();

        if (!StringUtils.equals(CustomDiscComponent.getTitle(this.stack), this.title))
            EntitleCustomMusicDisc.send(this.hand, this.stack, this.title);
    }

}
