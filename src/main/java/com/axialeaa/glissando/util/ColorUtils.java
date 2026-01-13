package com.axialeaa.glissando.util;

import com.axialeaa.glissando.config.gui.widget.HorizontalColoredQuadGuiElementRenderState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.awt.Color;

public class ColorUtils {

    private static int getMaxHueColor(float hue) {
        return getArgb(hue, 1, 1, 1);
    }

    public static int getArgb(float hue, float saturation, float brightness, float alpha) {
        return ColorHelper.withAlpha(alpha, Color.HSBtoRGB(hue, saturation, brightness));
    }

    public static void drawGradient(DrawContext context, ScreenRect screenRect, float hue) {
        int minX = screenRect.getLeft(),
            minY = screenRect.getTop(),
            maxX = screenRect.getRight(),
            maxY = screenRect.getBottom();

        HorizontalColoredQuadGuiElementRenderState.fill(context, minX, minY, maxX, maxY, -1, getMaxHueColor(hue));
        context.fillGradient(minX, minY, maxX, maxY, 0, Colors.BLACK);
    }

    public static int getArgbPitchColor(int pitch) {
        return getRgbPitchColor(pitch) | Colors.BLACK;
    }

    public static int getRgbPitchColor(int pitch) {
        float delta = pitch / 24.0F;

        int red = (int) (Math.max(0.0F, MathHelper.sin(delta * MathHelper.TAU) * 0.65F + 0.35F) * 255);
        int green = (int) (Math.max(0.0F, MathHelper.sin((delta + (1.0F / 3.0F)) * MathHelper.TAU) * 0.65F + 0.35F) * 255);
        int blue = (int) (Math.max(0.0F, MathHelper.sin((delta + (2.0F / 3.0F)) * MathHelper.TAU) * 0.65F + 0.35F) * 255);

        return red << 16 | green << 8 | blue;
    }

    public enum HSBAChannel implements SimpleEnumStringIdentifiable {

        HUE, SATURATION, BRIGHTNESS, ALPHA;

        private static float[] getFloats(int argb) {
            int r = ColorHelper.getRed(argb),
                g = ColorHelper.getGreen(argb),
                b = ColorHelper.getBlue(argb);

            float[] hsb = Color.RGBtoHSB(r, g, b, null);

            return new float[] { hsb[0], hsb[1], hsb[2], ColorHelper.getAlphaFloat(argb) };
        }

        public float get(int argb) {
            return getFloats(argb)[this.ordinal()];
        }

        public int getColorWithValue(int argb, float value) {
            float[] hsba = getFloats(argb);
            hsba[this.ordinal()] = value;

            return getArgb(
                hsba[HUE.ordinal()],
                hsba[SATURATION.ordinal()],
                hsba[BRIGHTNESS.ordinal()],
                hsba[ALPHA.ordinal()]
            );
        }

    }

}
