package com.axialeaa.glissando.item.component;

import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.item.DataUnit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record CustomDiscComponent(List<RegistryKey<DataNoteBlockInstrument>> instruments, List<DataUnit> data, String producer, String title) implements TooltipAppender {

    public static final String PATH = "custom_disc";
    public static final int MAX_TITLE_LENGTH = 32;

    public static final CustomDiscComponent DEFAULT = new CustomDiscComponent(List.of(), List.of(), "", "");

    public static final Codec<CustomDiscComponent> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            RegistryKey.createCodec(DataNoteBlockInstrument.REGISTRY_KEY).listOf().fieldOf("instruments").forGetter(CustomDiscComponent::instruments),
            DataUnit.CODEC.listOf().fieldOf("data").forGetter(CustomDiscComponent::data),
            Codec.STRING.fieldOf("producer").forGetter(CustomDiscComponent::producer),
            Codec.sizeLimitedString(MAX_TITLE_LENGTH).fieldOf("title").forGetter(CustomDiscComponent::title)
        )
        .apply(instance, CustomDiscComponent::new)
    );

    public static final PacketCodec<RegistryByteBuf, CustomDiscComponent> PACKET_CODEC = PacketCodec.tuple(
        RegistryKey.createPacketCodec(DataNoteBlockInstrument.REGISTRY_KEY).collect(PacketCodecs.toList()), CustomDiscComponent::instruments,
        DataUnit.PACKET_CODEC.collect(PacketCodecs.toList()), CustomDiscComponent::data,
        PacketCodecs.STRING, CustomDiscComponent::producer,
        PacketCodecs.STRING, CustomDiscComponent::title,
        CustomDiscComponent::new
    );

    public static final ComponentType<CustomDiscComponent> TYPE = ComponentType.<CustomDiscComponent>builder()
        .codec(CODEC)
        .packetCodec(PACKET_CODEC)
        .build();

    public int getLengthInTicks() {
        return this.data.getLast().tickOffset();
    }

    public boolean shouldStopPlaying(long ticksSinceSongStarted) {
        return ticksSinceSongStarted >= this.getLengthInTicks() + 20;
    }

    public static String getTitle(ItemStack stack) {
        return stack.getOrDefault(TYPE, DEFAULT).title();
    }

    public Short2ObjectArrayMap<List<DataUnit>> createTickTimeOffsetToDataMap() {
        Short2ObjectArrayMap<List<DataUnit>> timeDataMap = new Short2ObjectArrayMap<>();

        for (DataUnit unit : this.data) {
            timeDataMap.compute(unit.tickOffset(), (tickOffset, list) -> {
                if (list == null)
                    return new ArrayList<>(List.of());

                list.add(unit);

                return list;
            });
        }

        return timeDataMap;
    }

    public void readData(World world, BlockPos pos, BlockState state, List<DataUnit> data) {
        for (DataUnit unit : data) {
            RegistryKey<DataNoteBlockInstrument> instrumentKey = this.instruments.get(unit.idIndex());
            DataNoteBlockInstrument instrument = DataNoteBlockInstrument.get(world, instrumentKey);

            if (instrument == null)
                continue;

            RegistryEntry<SoundEvent> soundEventEntry = instrument.getSoundEvent(instrumentKey, world, pos, state);

            if (soundEventEntry == null)
                continue;

            world.playSound(null, pos, soundEventEntry.value(), SoundCategory.RECORDS, 4, unit.pitch());
        }
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        if (!StringUtils.isBlank(this.title))
            textConsumer.accept(Text.translatable("item.glissando.music_disc.desc", this.producer, this.title).formatted(Formatting.GRAY));
    }

}