package com.axialeaa.glissando.item;

import it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap;

import java.util.List;

public interface CustomDiscJukebox {

    Short2ObjectArrayMap<List<DataUnit>> getTickTimeOffsetToDataMap();

    short getTicksSinceSongStarted();

    void incrementTicksSinceSongStarted();

}
