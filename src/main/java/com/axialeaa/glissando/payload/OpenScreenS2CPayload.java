package com.axialeaa.glissando.payload;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.block_entity.NoteBlockEntity;
import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.gui.screen.NoteBlockScreen;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public record OpenScreenS2CPayload(BlockPos pos, RegistryKey<DataNoteBlockInstrument> instrument, boolean placed) implements CustomPayload {

    private static final Id<OpenScreenS2CPayload> PAYLOAD_ID = Glissando.payloadId("open_screen");

    public static final PacketCodec<RegistryByteBuf, OpenScreenS2CPayload> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, OpenScreenS2CPayload::pos,
        RegistryKey.createPacketCodec(DataNoteBlockInstrument.REGISTRY_KEY), OpenScreenS2CPayload::instrument,
        PacketCodecs.BOOLEAN, OpenScreenS2CPayload::placed,
        OpenScreenS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    public static void send(ServerPlayerEntity player, BlockPos pos, RegistryKey<DataNoteBlockInstrument> instrument, boolean placed) {
        ServerPlayNetworking.send(player, new OpenScreenS2CPayload(pos, instrument, placed));
    }

    public static void register() {
        PayloadRegistryUtils.registerClientbound(PAYLOAD_ID, PACKET_CODEC, (payload, ctx) -> {
            MinecraftClient client = ctx.client();
            ClientWorld world = client.world;

            client.execute(() -> payload.openScreen(client, Objects.requireNonNull(world)));
        }, payload -> List.of(payload.pos, payload.instrument, payload.placed));
    }

    private void openScreen(MinecraftClient client, @NotNull ClientWorld world) {
        if (!(world.getBlockEntity(this.pos) instanceof NoteBlockEntity blockEntity))
            return;

        if (this.placed && !Glissando.clientConfig.openScreenWhenPlaced.get())
            return;

        Registry<DataNoteBlockInstrument> registry = world.getRegistryManager().getOrThrow(DataNoteBlockInstrument.REGISTRY_KEY);
        DataNoteBlockInstrument instrument = registry.getValueOrThrow(this.instrument);

        client.setScreen(new NoteBlockScreen(blockEntity, instrument));
    }

}