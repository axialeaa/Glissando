package com.axialeaa.glissando.util;

import com.axialeaa.glissando.Glissando;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class AnimationMath {

    public static float getAnimationProgress(long ticksSinceStarted, float tickProgress, int animationTime) {
        return (ticksSinceStarted + tickProgress) / animationTime;
    }

    public record Pulse(long ticksSinceStarted) {

        public static float animationProgress(long ticksSinceStarted, float tickProgress) {
            return getAnimationProgress(ticksSinceStarted, tickProgress, Glissando.clientConfig.pulseAnimationTime.get());
        }

        public float animationProgress(float tickProgress) {
            return animationProgress(this.ticksSinceStarted, tickProgress);
        }

        public boolean active(float tickProgress) {
            float progress = this.animationProgress(tickProgress);
            return progress >= 0 && progress <= 1;
        }

        private int getPeakCount() {
            return Glissando.clientConfig.pulseBounces.get() + 1;
        }

        private float getDampenCoefficient(float animationProgress) {
            return 1 - animationProgress;
        }

        public float wavePoint(float animationProgress) {
            float dampenCoefficient = this.getDampenCoefficient(animationProgress);
            return MathHelper.abs(MathHelper.sin(dampenCoefficient * this.getPeakCount() * MathHelper.PI)) * Glissando.clientConfig.pulseMagnitude.get() * dampenCoefficient;
        }

        public Text getText(float tickProgress) {
            float animationProgress = this.animationProgress(tickProgress);
            float dampenCoefficient = this.getDampenCoefficient(animationProgress);

            MutableText text = Text.translatable(
                "glissando.screen.pulse_animation_expression",
                "%.2f".formatted(this.getPeakCount() * dampenCoefficient),
                "%.2f".formatted(Glissando.clientConfig.pulseMagnitude.get() * dampenCoefficient),
                "%.2f".formatted(this.wavePoint(animationProgress))
            );

            return text;
        }

        public float scalar(float tickProgress) {
            if (!this.active(tickProgress))
                return 1;

            return 1 + this.wavePoint(this.animationProgress(tickProgress));
        }

        public void transformNoteBlockMatrices(MatrixStack matrices, float tickProgress) {
            if (!this.active(tickProgress))
                return;

            float scalar = this.scalar(tickProgress);
            float translation = this.wavePoint(this.animationProgress(tickProgress)) * -0.5F;

            matrices.translate(translation, 0, translation);
            matrices.scale(scalar, scalar, scalar);
        }

    }

}
