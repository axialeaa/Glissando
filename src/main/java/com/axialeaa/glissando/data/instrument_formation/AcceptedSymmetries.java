package com.axialeaa.glissando.data.instrument_formation;

import com.axialeaa.glissando.util.SimpleEnumStringIdentifiable;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum AcceptedSymmetries implements SimpleEnumStringIdentifiable {

    STRICT, ROTATIONS, ROTATIONS_AND_FLIPS;

    public static final Codec<AcceptedSymmetries> CODEC = StringIdentifiable.createCodec(AcceptedSymmetries::values);

}