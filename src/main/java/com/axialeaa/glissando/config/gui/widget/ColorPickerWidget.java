package com.axialeaa.glissando.config.gui.widget;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.option.Option;
import com.axialeaa.glissando.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.List;
import java.util.Optional;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.COMMON_SPACING;

public class ColorPickerWidget extends DirectionalLayoutWidget {

    private static final int RESET_BUTTON_WIDTH = 40;
    private static final int LOWER_ROW_HEIGHT = 18;
    private static final String HEX_CODE_REGEX = "[a-fA-F\\d]*";

    private final int size;
    private final Option<Integer> colorOption;
    private final TextRenderer textRenderer;

    private final GradientWidget gradientPicker;
    private final TextFieldWidget textField;
    private final ButtonWidget resetButton;
    private final ColorChannelSliderWidget hueSlider;
    private final ColorChannelSliderWidget alphaSlider;

    public ColorPickerWidget(int x, int y, int size, Option<Integer> colorOption, TextRenderer textRenderer) {
        super(x, y, DisplayAxis.VERTICAL);
        this.spacing(COMMON_SPACING);

        this.size = size;
        this.colorOption = colorOption;
        this.textRenderer = textRenderer;

        this.gradientPicker = new GradientWidget();
        this.hueSlider = new ColorChannelSliderWidget(ColorUtils.HSBAChannel.HUE);
        this.alphaSlider = new ColorChannelSliderWidget(ColorUtils.HSBAChannel.ALPHA);

        this.add(new MultilineTextWidget(colorOption.getTranslatedName(), textRenderer), Positioner.create().alignHorizontalCenter());
        this.add(this.gradientPicker);

        this.add(this.hueSlider);
        this.add(this.alphaSlider);

        this.textField = new TextFieldWidget(textRenderer, size - 2 * COMMON_SPACING - RESET_BUTTON_WIDTH - LOWER_ROW_HEIGHT, LOWER_ROW_HEIGHT, ScreenTexts.EMPTY);

        this.textField.setTextPredicate(string -> string.matches(HEX_CODE_REGEX) || string.isEmpty());
        this.textField.setChangedListener(this::onTextChanged);
        this.textField.setMaxLength(8);

        this.resetButton = ButtonWidget
            .builder(
                Text.translatable("controls.reset"),
                widget -> this.updateText(this.colorOption.defaultValue)
            )
            .size(RESET_BUTTON_WIDTH, LOWER_ROW_HEIGHT)
            .build();

        this.resetButton.active = !colorOption.isDefault();

        DirectionalLayoutWidget bottomRowLayout = DirectionalLayoutWidget.horizontal().spacing(COMMON_SPACING);

        bottomRowLayout.add(this.resetButton);
        bottomRowLayout.add(this.textField);
        bottomRowLayout.add(new ColorPreviewWidget());

        this.add(bottomRowLayout);

        this.refreshPositions();
        this.updateText(colorOption.get());
    }

    private void updateText(int argb) {
        this.textField.setText("%08X".formatted(argb).toUpperCase());
    }

    private void onTextChanged(String string) {
        if (string.isEmpty())
            return;

        int newColor = Integer.parseUnsignedInt(string, 16);

        this.updateHue(newColor);
        this.updateAlpha(newColor);

        this.colorOption.set(newColor);
        this.gradientPicker.resetCursorPos(newColor);

        this.resetButton.active = newColor != this.colorOption.defaultValue;
    }

    private void updateHue(int argb) {
        if (ColorUtils.HSBAChannel.SATURATION.get(argb) == 0) // prevents resetting to 0 degrees when selecting some shade of gray
            return;

        float hue = ColorUtils.HSBAChannel.HUE.get(argb);

        this.hueSlider.silentlySetValue(hue);
        this.gradientPicker.setHue(hue);
    }

    private void updateAlpha(int argb) {
        float alpha = ColorUtils.HSBAChannel.ALPHA.get(argb);
        this.alphaSlider.silentlySetValue(alpha);
    }

    @Override
    public void refreshPositions() {
        super.refreshPositions();
        this.gradientPicker.resetCursorPos(this.colorOption.get());
    }

    private class GradientWidget extends ClickableWidget {

        private static final Identifier FRAME = Glissando.id("config/color_picker/color_picker_frame");
        private static final Identifier CURSOR = Glissando.id("config/color_picker/color_picker_cursor");
        public static final int BORDER_WIDTH = 8;
        private static final int CURSOR_SIZE = 9;

        private float hue;
        private Vector2i cursorPos;
        private Optional<Integer> pendingColor = Optional.empty();
        private boolean isMouseDown;

        public GradientWidget() {
            super(0, 0, ColorPickerWidget.this.size, ColorPickerWidget.this.size, ScreenTexts.EMPTY);
            int argb = ColorPickerWidget.this.colorOption.get();
            this.hue = ColorUtils.HSBAChannel.HUE.get(argb);

            this.resetCursorPos(argb);
        }

        private void setHue(float hue) {
            this.hue = hue;
        }

        private void resetCursorPos(int argb) {
            float saturation = ColorUtils.HSBAChannel.SATURATION.get(argb);
            float brightness = ColorUtils.HSBAChannel.BRIGHTNESS.get(argb);

            this.setRelativeCursorPos(saturation, 1 - brightness);
        }

        private void setRelativeCursorPos(double deltaX, double deltaY) {
            int x = MathHelper.floor(deltaX * this.getGradientSize());
            int y = MathHelper.floor(deltaY * this.getGradientSize());

            this.cursorPos = new Vector2i(x + this.getGradientBounds().getLeft(), y + this.getGradientBounds().getTop());
        }

        private int getGradientSize() {
            return ColorPickerWidget.this.size - 2 * BORDER_WIDTH;
        }

        private ScreenRect getGradientBounds() {
            int x = this.getX() + BORDER_WIDTH;
            int y = this.getY() + BORDER_WIDTH;

            return new ScreenRect(x, y, this.getGradientSize(), this.getGradientSize());
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, FRAME, this.getX(), this.getY(), this.width, this.height);
            ColorUtils.drawGradient(context, this.getGradientBounds(), this.hue);

            int cursorRadius = MathHelper.floorDiv(CURSOR_SIZE, 2);
            ScreenRect gradientBounds = this.getGradientBounds();

            int x = Math.min(this.cursorPos.x, gradientBounds.getRight() - 1);
            int y = Math.min(this.cursorPos.y, gradientBounds.getBottom() - 1);

            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, CURSOR, x - cursorRadius, y - cursorRadius, CURSOR_SIZE, CURSOR_SIZE);

            if (this.isMouseDown) {
                context.drawTooltip(ColorPickerWidget.this.textRenderer, this.getTooltipContents(), x, y);
                drawCoordinateLines(context, gradientBounds, x - cursorRadius - 1, x + cursorRadius + 1, y - cursorRadius, y + cursorRadius, x, y, 0x4CFFFFFF);
            }
        }

        private static void drawCoordinateLines(DrawContext context, ScreenRect gradientBounds, int minX, int maxX, int minY, int maxY, int cursorX, int cursorY, int color) {
            if (gradientBounds.getRight() > maxX)
                context.drawHorizontalLine(gradientBounds.getRight() - 1, maxX, cursorY, color);

            if (gradientBounds.getLeft() - 1 < minX)
                context.drawHorizontalLine(gradientBounds.getLeft(), minX, cursorY, color);

            if (gradientBounds.getTop() - 1 < minY)
                context.drawVerticalLine(cursorX, gradientBounds.getTop() - 1, minY, color);

            if (gradientBounds.getBottom() > maxY)
                context.drawVerticalLine(cursorX, gradientBounds.getBottom(), maxY, color);
        }

        private List<Text> getTooltipContents() {
            int argb = ColorPickerWidget.this.colorOption.get();

            return List.of(
                Text.translatable("glissando.option.color.saturation", (int) (ColorUtils.HSBAChannel.SATURATION.get(argb) * 100)),
                Text.translatable("glissando.option.color.brightness", (int) (ColorUtils.HSBAChannel.BRIGHTNESS.get(argb) * 100))
            );
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (!this.active || !this.visible || !this.getGradientBounds().contains(MathHelper.floor(mouseX), MathHelper.floor(mouseY)))
                return false;

            if (!this.isValidClickButton(button) || !this.isMouseOver(mouseX, mouseY))
                return false;

            this.onMouseDown(mouseX, mouseY);

            return true;
        }

        @Override
        protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
            mouseX = MathHelper.clamp(mouseX, this.getGradientBounds().getLeft(), this.getGradientBounds().getRight());
            mouseY = MathHelper.clamp(mouseY, this.getGradientBounds().getTop(), this.getGradientBounds().getBottom());

            this.onMouseDown(mouseX, mouseY);
        }

        private void onMouseDown(double mouseX, double mouseY) {
            double deltaX = (mouseX - this.getX() - BORDER_WIDTH) / this.getGradientSize();
            double deltaY = (mouseY - this.getY() - BORDER_WIDTH) / this.getGradientSize();

            this.isMouseDown = true;
            this.setRelativeCursorPos(deltaX, deltaY);

            int color = ColorPickerWidget.this.colorOption.get();

            float saturation = (float) deltaX;
            float brightness = (float) (1 - deltaY);
            float alpha = ColorUtils.HSBAChannel.ALPHA.get(color);

            color = ColorUtils.getArgb(this.hue, saturation, brightness, alpha);
            this.pendingColor = Optional.of(color);

            ColorPickerWidget.this.colorOption.set(color);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            if (this.pendingColor.isPresent()) {
                ColorPickerWidget.this.updateText(this.pendingColor.get());
                this.pendingColor = Optional.empty();
            }

            this.isMouseDown = false;
            this.playDownSound(MinecraftClient.getInstance().getSoundManager());

            return super.mouseReleased(mouseX, mouseY, button);
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {}

        @Override
        public boolean isNarratable() {
            return false;
        }

        @Nullable
        @Override
        public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
            return null;
        }

    }

    private class ColorChannelSliderWidget extends SliderWidget {

        private final ColorUtils.HSBAChannel channel;

        public ColorChannelSliderWidget(ColorUtils.HSBAChannel channel) {
            super(0, 0, ColorPickerWidget.this.size, LOWER_ROW_HEIGHT, ScreenTexts.EMPTY, channel.get(ColorPickerWidget.this.colorOption.get()));
            this.channel = channel;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            int multiplyFactor = this.channel == ColorUtils.HSBAChannel.HUE ? 360 : 100;
            this.setMessage(Text.translatable("glissando.option.color." + this.channel.asString(), (int) (this.value * multiplyFactor)));
        }

        protected void silentlySetValue(double value) {
            this.value = value;
            this.updateMessage();
        }

        @Override
        protected void applyValue() {
            ColorPickerWidget thisWidget = ColorPickerWidget.this;

            if (this.channel == ColorUtils.HSBAChannel.HUE)
                thisWidget.gradientPicker.setHue((float) this.value);

            Option<Integer> option = thisWidget.colorOption;

            int color = option.get();
            color = this.channel.getColorWithValue(color, (float) this.value);

            option.set(color);
            thisWidget.updateText(color);
        }

    }

    private class ColorPreviewWidget extends UnnavigableWidget {

        private static final Identifier TEXTURE = Glissando.id("config/color_picker/color_preview");
        private static final int BORDER_WIDTH = 1;
        private static final int SIZE = LOWER_ROW_HEIGHT;

        public ColorPreviewWidget() {
            super(SIZE);
        }

        private int getGradientSize() {
            return SIZE - 2 * BORDER_WIDTH;
        }

        private ScreenRect getGradientBounds() {
            int x = this.getX() + BORDER_WIDTH;
            int y = this.getY() + BORDER_WIDTH;

            return new ScreenRect(x, y, this.getGradientSize(), this.getGradientSize());
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, this.getX(), this.getY(), SIZE, SIZE);

            ScreenRect gradientBounds = this.getGradientBounds();

            int endColor = ColorPickerWidget.this.colorOption.get();
            int startColor = ColorHelper.fullAlpha(endColor);

            context.fillGradient(gradientBounds.getLeft(), gradientBounds.getTop(), gradientBounds.getRight(), gradientBounds.getBottom(), startColor, endColor);
        }

    }

}
