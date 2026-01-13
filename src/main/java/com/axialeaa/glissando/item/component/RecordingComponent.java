package com.axialeaa.glissando.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record RecordingComponent(boolean recording, long lastStartedRecordingTicks) {

    public static final String PATH = "recording";
    public static final RecordingComponent DEFAULT = new RecordingComponent(false, 0);

    public static final Codec<RecordingComponent> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.BOOL.fieldOf("recording").forGetter(RecordingComponent::recording),
            Codec.LONG.fieldOf("last_started_recording_ticks").forGetter(RecordingComponent::lastStartedRecordingTicks)
        )
        .apply(instance, RecordingComponent::new)
    );

    public static final PacketCodec<RegistryByteBuf, RecordingComponent> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.BOOLEAN, RecordingComponent::recording,
        PacketCodecs.LONG, RecordingComponent::lastStartedRecordingTicks,
        RecordingComponent::new
    );

    public static final ComponentType<RecordingComponent> TYPE = ComponentType.<RecordingComponent>builder().codec(CODEC).packetCodec(PACKET_CODEC).build();

}