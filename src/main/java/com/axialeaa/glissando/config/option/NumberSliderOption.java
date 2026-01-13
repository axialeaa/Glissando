package com.axialeaa.glissando.config.option;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.MathHelper;

public abstract class NumberSliderOption<T> extends Option<T> {

    @SafeVarargs
    public NumberSliderOption(String key, T defaultValue, Codec<T> codec, boolean sync, Runnable configSaveRunnable, Callback<T>... onValueChangedCallbacks) {
        super(key, defaultValue, codec, sync, configSaveRunnable, onValueChangedCallbacks);
    }

    public abstract double getSliderProgress();
    public abstract T toValue(double sliderProgress);

    public static class I extends NumberSliderOption<Integer> {

        private final int min;
        private final int max;

        @SafeVarargs
        public I(String key, int defaultValue, int min, int max, boolean sync, Runnable configSaveRunnable, Callback<Integer>... onValueChangedCallbacks) {
            super(key, defaultValue, Codec.intRange(min, max), sync, configSaveRunnable, onValueChangedCallbacks);
            this.min = min;
            this.max = max;
        }

        @Override
        public double getSliderProgress() {
            return MathHelper.getLerpProgress(this.get(), this.min, this.max);
        }

        @Override
        public Integer toValue(double sliderProgress) {
            return Math.round((float) MathHelper.lerp(sliderProgress, this.min, this.max));
        }

    }

    public static class F extends NumberSliderOption<Float> {

        private final float min;
        private final float max;

        @SafeVarargs
        public F(String key, float defaultValue, float min, float max, boolean sync, Runnable configSaveRunnable, Callback<Float>... onValueChangedCallbacks) {
            super(key, defaultValue, Codec.floatRange(min, max), sync, configSaveRunnable, onValueChangedCallbacks);
            this.min = min;
            this.max = max;
        }

        @Override
        public double getSliderProgress() {
            return MathHelper.getLerpProgress(this.get(), this.min, this.max);
        }

        @Override
        public Float toValue(double sliderProgress) {
            float value = (float) MathHelper.lerp(sliderProgress, this.min, this.max);
            return Float.parseFloat("%.2f".formatted(value));
        }

    }

    public static class D extends NumberSliderOption<Double> {

        private final double min;
        private final double max;

        @SafeVarargs
        public D(String key, double defaultValue, double min, double max, boolean sync, Runnable configSaveRunnable, Callback<Double>... onValueChangedCallbacks) {
            super(key, defaultValue, Codec.doubleRange(min, max), sync, configSaveRunnable, onValueChangedCallbacks);
            this.min = min;
            this.max = max;
        }

        @Override
        public double getSliderProgress() {
            return MathHelper.getLerpProgress(this.get(), this.min, this.max);
        }

        @Override
        public Double toValue(double sliderProgress) {
            double value = MathHelper.lerp(sliderProgress, this.min, this.max);
            return Double.parseDouble("%.2f".formatted(value));
        }

    }

}
