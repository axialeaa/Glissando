package com.axialeaa.glissando.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record DataUnit(byte idIndex, byte pitch, short tickOffset) {

    public static final Codec<DataUnit> CODEC = Codec.INT.xmap(
        i -> new DataUnit((byte) (i >> 24), (byte) (i >> 16), (short) (i & 65535)),
        dataUnit -> (dataUnit.idIndex << 24) | (dataUnit.pitch << 16) | dataUnit.tickOffset
    );

    public static final PacketCodec<ByteBuf, DataUnit> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.BYTE, DataUnit::idIndex,
        PacketCodecs.BYTE, DataUnit::pitch,
        PacketCodecs.SHORT, DataUnit::tickOffset,
        DataUnit::new
    );

}