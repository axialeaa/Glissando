package com.axialeaa.glissando.config.gui.screen.branch;

import com.axialeaa.glissando.config.gui.screen.MainConfigurationScreen;
import com.axialeaa.glissando.gui.screen.ConfiguredBackgroundScreen;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public abstract class SimpleOptionBranchScreen extends ConfiguredBackgroundScreen {

	private static final int SEPARATOR_HEIGHT = 2;
	private static final int SEPARATOR_TEXTURE_WIDTH = 32;
	protected static final int BUTTON_WIDTH = 160;

	private final MainConfigurationScreen parentScreen;
	private final boolean renderSeparators;
	private final boolean darkenContentBackground;
	public final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

	public SimpleOptionBranchScreen(Text title, MainConfigurationScreen parentScreen) {
		this(title, parentScreen, true, true);
	}

	public SimpleOptionBranchScreen(Text title, MainConfigurationScreen parentScreen, boolean renderSeparators, boolean darkenContentBackground) {
		super(title);
		this.parentScreen = parentScreen;
        this.renderSeparators = renderSeparators;
        this.darkenContentBackground = darkenContentBackground;
    }

	@Override
	protected void init() {
		this.initHeader();
		this.initBody();
		this.initFooter();

		this.refreshWidgetPositions();
		this.layout.forEachChild(this::addDrawableChild);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);

		if (!this.renderSeparators)
			return;

		boolean inWorld = this.client != null && this.client.world != null;

		Identifier headerSeparator = inWorld ? INWORLD_HEADER_SEPARATOR_TEXTURE : HEADER_SEPARATOR_TEXTURE;
		Identifier footerSeparator = inWorld ? INWORLD_FOOTER_SEPARATOR_TEXTURE : FOOTER_SEPARATOR_TEXTURE;

		context.drawTexture(RenderPipelines.GUI_TEXTURED, headerSeparator, 0, this.layout.getHeaderHeight() - SEPARATOR_HEIGHT, 0, 0, this.width, SEPARATOR_HEIGHT, SEPARATOR_TEXTURE_WIDTH, SEPARATOR_HEIGHT);
		context.drawTexture(RenderPipelines.GUI_TEXTURED, footerSeparator, 0, this.height - this.layout.getFooterHeight(), 0, 0, this.width, SEPARATOR_HEIGHT, SEPARATOR_TEXTURE_WIDTH, SEPARATOR_HEIGHT);
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.renderBackground(context, mouseX, mouseY, deltaTicks);

		if (this.darkenContentBackground)
			this.renderDarkening(context, 0, this.layout.getHeaderHeight(), this.width, this.layout.getContentHeight());
	}

	protected void initHeader() {
		this.layout.addHeader(this.title, this.textRenderer);
	}

	protected abstract void initBody();

	protected void initFooter() {
		this.layout.addFooter(createCenteredCloseButton(ScreenTexts.BACK, this, 0));
		// this.layout.addFooter(ButtonWidget.builder(ScreenTexts.BACK, button -> this.close()).width(CLOSE_BUTTON_WIDTH).build());
	}

	@Override
	protected void refreshWidgetPositions() {
		this.layout.refreshPositions();
	}

	@Override
	public void close() {
        if (this.client != null)
            this.client.setScreen(this.parentScreen);
    }

}