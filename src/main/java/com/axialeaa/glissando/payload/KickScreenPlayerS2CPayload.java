package com.axialeaa.glissando.payload;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.gui.screen.NoteBlockScreen;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class KickScreenPlayerS2CPayload implements CustomPayload {

    private static final Id<KickScreenPlayerS2CPayload> PAYLOAD_ID = Glissando.payloadId("kick_screen_player");
    public static final PacketCodec<RegistryByteBuf, KickScreenPlayerS2CPayload> PACKET_CODEC = CustomPayload.codecOf((value, buf) -> new KickScreenPlayerS2CPayload(), buf -> new KickScreenPlayerS2CPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    public static void send(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new KickScreenPlayerS2CPayload());
    }

    public static void register() {
        PayloadRegistryUtils.registerClientbound(PAYLOAD_ID, PACKET_CODEC, (payload, ctx) -> {
            MinecraftClient client = ctx.client();
            client.execute(() -> payload.kickScreenPlayer(client));
        }, payload -> List.of());
    }

    public void kickScreenPlayer(MinecraftClient client) {
        if (client.currentScreen instanceof NoteBlockScreen)
            client.setScreen(null);
    }

}