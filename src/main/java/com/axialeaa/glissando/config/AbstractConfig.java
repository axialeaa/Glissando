package com.axialeaa.glissando.config;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.option.NumberSliderOption;
import com.axialeaa.glissando.config.option.Option;
import com.google.common.collect.Maps;
import com.google.gson.FormattingStyle;
import com.google.gson.JsonElement;
import com.google.gson.Strictness;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.dynamic.Codecs;

import java.io.*;
import java.util.*;

public abstract class AbstractConfig {
    
    public File file;

    private final Map<String, Option<?>> optionMap = Maps.newHashMap();
    private boolean triedFileReaderCreation = false;

    public void load() {
        File file = this.file;
        
        try (JsonReader jsonReader = new JsonReader(this.tryCreateFileReader(file))) {
            jsonReader.setStrictness(Strictness.LENIENT);

            jsonReader.beginObject();
            this.tryParseOptionData(jsonReader);
            jsonReader.endObject();
        }
        catch (IOException e) {
            Glissando.LOGGER.warn("Failed to parse config file at path {}; falling back to default values.", file, e);
            this.write();
        }
    }

    private FileReader tryCreateFileReader(File file) {
        try {
            return new FileReader(file);
        }
        catch (FileNotFoundException e) {
            if (this.triedFileReaderCreation)
                throw new RuntimeException("Failed to create FileReader. Is the file at %s readable?".formatted(file), e);

            this.write();
            this.triedFileReaderCreation = true;

            return this.tryCreateFileReader(file);
        }
    }

    private void tryParseOptionData(JsonReader reader) throws IOException {
        while (reader.hasNext()) {
            if (reader.peek() != JsonToken.NAME) {
                reader.skipValue();
                continue;
            }

            String name = reader.nextName();
            Option<?> option = this.optionMap.get(name);

            if (option == null) { // failsafe
                reader.skipValue();
                continue;
            }

            JsonElement element = TypeAdapters.JSON_ELEMENT.read(reader);
            option.deserializeAndApply(element);
        }
    }

    public void write() {
        File file = this.file;
        file.getParentFile().mkdirs();
        
        try (JsonWriter jsonWriter = new JsonWriter(new FileWriter(file))) {
            jsonWriter.setFormattingStyle(FormattingStyle.PRETTY);
            jsonWriter.beginObject();

            this.writeOptions(jsonWriter);

            jsonWriter.endObject();
            jsonWriter.flush();
        }
        catch (IOException e) {
            Glissando.LOGGER.warn("Failed to save config file to path {}.", file, e);
        }
    }

    private void writeOptions(JsonWriter writer) throws IOException {
        for (Map.Entry<String, Option<?>> entry : this.optionMap.entrySet()) {
            writer.name(entry.getKey());
            Optional<JsonElement> optional = entry.getValue().serialize();

            if (optional.isPresent())
                TypeAdapters.JSON_ELEMENT.write(writer, optional.get());
        }
    }

    @SafeVarargs
    protected final <T> Option<T> of(String path, T defaultValue, Codec<T> codec, boolean sync, Option.Callback<T>... onValueChangedCallbacks) {
        Option<T> option = new Option<>(path, defaultValue, codec, sync, this::write, onValueChangedCallbacks);
        optionMap.put(path, option);
        
        return option;
    }

    @SafeVarargs
    protected final Option<Boolean> ofBool(String path, boolean defaultValue, boolean sync, Option.Callback<Boolean>... onValueChangedCallbacks) {
        return this.of(path, defaultValue, Codec.BOOL, sync, onValueChangedCallbacks);
    }

    @SafeVarargs
    protected final NumberSliderOption.I ofInt(String path, int defaultValue, int min, int max, boolean sync, Option.Callback<Integer>... onValueChangedCallbacks) {
        NumberSliderOption.I option = new NumberSliderOption.I(path, defaultValue, min, max, sync, this::write, onValueChangedCallbacks);
        optionMap.put(path, option);

        return option;
    }

    @SafeVarargs
    protected final NumberSliderOption.F ofFloat(String path, float defaultValue, float min, float max, boolean sync, Option.Callback<Float>... onValueChangedCallbacks) {
        NumberSliderOption.F option = new NumberSliderOption.F(path, defaultValue, min, max, sync, this::write, onValueChangedCallbacks);
        optionMap.put(path, option);

        return option;
    }

    @SafeVarargs
    protected final NumberSliderOption.D ofDouble(String path, double defaultValue, double min, double max, boolean sync, Option.Callback<Double>... onValueChangedCallbacks) {
        NumberSliderOption.D option = new NumberSliderOption.D(path, defaultValue, min, max, sync, this::write, onValueChangedCallbacks);
        optionMap.put(path, option);

        return option;
    }

    @SafeVarargs
    protected final Option<Integer> ofColor(String path, int defaultValue, boolean sync, Option.Callback<Integer>... onValueChangedCallbacks) {
        return this.of(path, defaultValue, Codecs.ARGB, sync, onValueChangedCallbacks);
    }

    @SafeVarargs
    protected final <T extends Enum<T> & StringIdentifiable> Option<T> ofEnum(String path, T defaultValue, Codec<T> codec, boolean sync, Option.Callback<T>... onValueChangedCallbacks) {
        return this.of(path, defaultValue, codec, sync, onValueChangedCallbacks);
    }

    @SafeVarargs
    protected final <T> Option<List<T>> ofList(String path, List<T> defaultValue, Codec<T> elementCodec, int minSize, int maxSize, boolean sync, Option.Callback<List<T>>... onValueChangedCallbacks) {
        return this.of(path, defaultValue, elementCodec.listOf(minSize, maxSize), sync, onValueChangedCallbacks);
    }

}
