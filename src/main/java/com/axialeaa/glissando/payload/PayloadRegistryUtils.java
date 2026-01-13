package com.axialeaa.glissando.payload;

import com.axialeaa.glissando.Glissando;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class PayloadRegistryUtils {

    public static <T extends CustomPayload> void registerServerbound(CustomPayload.Id<T> payloadId, PacketCodec<RegistryByteBuf, T> packetCodec, ServerPlayNetworking.PlayPayloadHandler<T> handler, Function<T, List<Object>> parameters) {
        PayloadTypeRegistry.playC2S().register(payloadId, packetCodec);
        ServerPlayNetworking.registerGlobalReceiver(payloadId, (payload, ctx) -> {
            Glissando.printDebugLog(logger -> logger.info("Received serverbound payload [{}] with parameters: {}.", payloadId.id(), Arrays.toString(parameters.apply(payload).toArray())));
            handler.receive(payload, ctx);
        });
    }

    public static <T extends CustomPayload> void registerClientbound(CustomPayload.Id<T> payloadId, PacketCodec<RegistryByteBuf, T> packetCodec, ClientPlayNetworking.PlayPayloadHandler<T> handler, Function<T, List<Object>> parameters) {
        PayloadTypeRegistry.playS2C().register(payloadId, packetCodec);
        ClientPlayNetworking.registerGlobalReceiver(payloadId, (payload, ctx) -> {
            Glissando.printDebugLog(logger -> logger.info("Received clientbound payload [{}] with parameters: {}.", payloadId.id(), Arrays.toString(parameters.apply(payload).toArray())));
            handler.receive(payload, ctx);
        });
    }

}
