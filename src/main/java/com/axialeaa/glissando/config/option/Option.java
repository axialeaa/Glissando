package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.payload.SyncConfigPayloads;
import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Option<T> {

    private final String key;
    public final T defaultValue;
    private final Codec<T> codec;
    public final boolean sync;
    private final Runnable configSaveRunnable;
    private final Collection<Callback<T>> onValueChangedCallbacks;

    private T value;

    @SafeVarargs
    public Option(String key, T defaultValue, Codec<T> codec, boolean sync, Runnable configSaveRunnable, Callback<T>... onValueChangedCallbacks) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.codec = codec;
        this.sync = sync;
        this.configSaveRunnable = configSaveRunnable;
        this.onValueChangedCallbacks = new ArrayList<>(List.of(onValueChangedCallbacks));

        this.value = defaultValue;
    }

    public void transform(ValueTransformer<T> transformer) {
        this.set(transformer.transform(this.get()));
    }

    public void set(T value) {
        this.value = value;
        this.configSaveRunnable.run();

        if (sync)
            SyncConfigPayloads.sendC2S();

        for (Callback<T> callback : this.onValueChangedCallbacks)
            callback.apply(this, value);
    }

    public T get() {
        return this.value;
    }

    public void reset() {
        this.set(this.defaultValue);
    }

    public boolean isDefault() {
        return this.value == this.defaultValue;
    }

    public void addOnValueChangedCallback(Callback<T> callback) {
        this.onValueChangedCallbacks.add(callback);
    }

    public Text getTranslation(String suffix) {
        return Text.translatable("glissando.option." + this.key + suffix);
    }

    public Text getTranslatedName() {
        return this.getTranslation("");
    }

    public Text getTranslatedTooltip() {
        return this.getTranslation(".tooltip");
    }

    public Text getSimpleDefaultText() {
        return Text.of(this.defaultValue.toString());
    }

    public Codec<T> getCodec() {
        return this.codec;
    }

    public Optional<JsonElement> serialize() {
        DataResult<JsonElement> dataResult = this.codec.encodeStart(JsonOps.INSTANCE, this.get());
        return dataResult.result();
    }

    public void deserializeAndApply(JsonElement element) {
        DataResult<T> result = this.codec.parse(JsonOps.INSTANCE, element);
        Optional<T> optional = result.resultOrPartial(Glissando.LOGGER::error);

        if (optional.isPresent()) {
            this.value = optional.get();
            return;
        }

        Glissando.LOGGER.warn("Invalid value for option \"{}\". Falling back to default.", this.key);

        this.reset();
    }

    @FunctionalInterface
    public interface ValueTransformer<T> {

        ValueTransformer<Boolean> INVERT_BOOLEAN = b -> !b;

        T transform(T value);

    }

    @FunctionalInterface
    public interface Callback<T> {

        void apply(Option<T> option, T value);

        static <T> Callback<T> empty() {
            return (option, value) -> {};
        }

    }

}
