package com.axialeaa.glissando;

import com.axialeaa.glissando.cca.RecordingComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class GlissandoCCA implements EntityComponentInitializer {

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(RecordingComponent.KEY, RecordingComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }

}
