package com.axialeaa.glissando.payload;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.GlissandoSyncedConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.util.List;

public class SyncConfigPayloads {

    public static void sendS2C(ServerPlayerEntity player) {
        GlissandoSyncedConfig config = Glissando.getServerConfig(player.getServer());
        ServerPlayNetworking.send(player, new S2C(config.file.getAbsolutePath()));
    }

    public static void sendC2S() {
        ClientPlayNetworking.send(new C2S());
    }

    public static void register() {
        PayloadRegistryUtils.registerClientbound(S2C.PAYLOAD_ID, S2C.PACKET_CODEC, S2C::onReceive, payload -> List.of(payload.path));
        PayloadRegistryUtils.registerServerbound(C2S.PAYLOAD_ID, C2S.PACKET_CODEC, (payload, ctx) -> C2S.onReceive(ctx), payload -> List.of());
    }

    public static class C2S implements CustomPayload {

        private static final Id<C2S> PAYLOAD_ID = Glissando.payloadId("serverbound_sync_config");
        private static final PacketCodec<RegistryByteBuf, C2S> PACKET_CODEC = CustomPayload.codecOf((value, buf) -> new C2S(), buf -> new C2S());

        @Override
        public Id<? extends CustomPayload> getId() {
            return PAYLOAD_ID;
        }

        private static void onReceive(ServerPlayNetworking.Context ctx) {
            MinecraftServer server = ctx.server();
            server.execute(() -> Glissando.getServerConfig(server).load());
        }

    }

    public record S2C(String path) implements CustomPayload {

        private static final Id<S2C> PAYLOAD_ID = Glissando.payloadId("clientbound_sync_config");
        private static final PacketCodec<RegistryByteBuf, S2C> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.STRING, S2C::path, S2C::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return PAYLOAD_ID;
        }

        private static void onReceive(S2C payload, ClientPlayNetworking.Context ctx) {
            ctx.client().execute(() -> {
                GlissandoSyncedConfig config = new GlissandoSyncedConfig();
                config.file = new File(payload.path);

                Glissando.syncedConfig = config;
                Glissando.syncedConfig.load();
            });
        }

    }

}