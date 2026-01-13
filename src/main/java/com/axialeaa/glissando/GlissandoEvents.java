package com.axialeaa.glissando;

import com.axialeaa.glissando.config.ServerConfigHolder;
import com.axialeaa.glissando.item.MusicDiscItem;
import com.axialeaa.glissando.payload.SyncConfigPayloads;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Items;
import net.minecraft.util.WorldSavePath;

import java.io.File;

public class GlissandoEvents {

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            GlissandoSyncedConfig config = new GlissandoSyncedConfig();
            config.file = new File(server.getSavePath(WorldSavePath.ROOT).toFile(), Glissando.MOD_ID + "_server.json");

            ((ServerConfigHolder) server).setConfig(config);

            config.load();
        });

        ServerPlayerEvents.JOIN.register(SyncConfigPayloads::sendS2C);

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((itemGroup, fabricItemGroupEntries) -> {
            if (itemGroup.contains(Items.MUSIC_DISC_13.getDefaultStack()))
                fabricItemGroupEntries.addBefore(Items.MUSIC_DISC_13, MusicDiscItem.REGISTRY_INSTANCE);
        });
    }

}
