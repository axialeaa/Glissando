package com.axialeaa.glissando.payload;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.item.component.CustomDiscComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

import java.util.List;

public record EntitleCustomMusicDisc(Hand hand, ItemStack stack, String title) implements CustomPayload {

    private static final Id<EntitleCustomMusicDisc> PAYLOAD_ID = Glissando.payloadId("entitle_custom_music_disc");
    private static final PacketCodec<RegistryByteBuf, Hand> HAND_PACKET_CODEC = PacketCodec.of((hand, buf) -> buf.writeBoolean(hand == Hand.MAIN_HAND), buf -> buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND);

    public static final PacketCodec<RegistryByteBuf, EntitleCustomMusicDisc> PACKET_CODEC = PacketCodec.tuple(
        HAND_PACKET_CODEC, EntitleCustomMusicDisc::hand,
        ItemStack.PACKET_CODEC, EntitleCustomMusicDisc::stack,
        PacketCodecs.STRING, EntitleCustomMusicDisc::title,
        EntitleCustomMusicDisc::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    public static void send(Hand hand, ItemStack stack, String title) {
        ClientPlayNetworking.send(new EntitleCustomMusicDisc(hand, stack, title));
    }

    public static void register() {
        PayloadRegistryUtils.registerServerbound(PAYLOAD_ID, PACKET_CODEC, (payload, ctx) ->
            ctx.server().execute(() -> payload.entitle(ctx.player())),
            payload -> List.of(payload.stack, payload.title)
        );
    }

    private void entitle(ServerPlayerEntity playerEntity) {
        this.stack.apply(CustomDiscComponent.TYPE, CustomDiscComponent.DEFAULT, component -> new CustomDiscComponent(component.instruments(), component.data(), playerEntity.getName().getString(), this.title));
        playerEntity.setStackInHand(this.hand, this.stack);
    }

}