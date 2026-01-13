package com.axialeaa.glissando;

import com.axialeaa.glissando.block_entity.NoteBlockEntity;
import com.axialeaa.glissando.block_entity.NoteBlockEntityRenderer;
import com.axialeaa.glissando.data.registry.GlissandoParticleTypes;
import com.axialeaa.glissando.item.component.CustomDiscComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.client.particle.WhiteSmokeParticle;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class GlissandoClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(GlissandoParticleTypes.INSTRUMENT_FORMED, WhiteSmokeParticle.Factory::new);
        ComponentTooltipAppenderRegistry.addFirst(CustomDiscComponent.TYPE);
        BlockEntityRendererFactories.register(NoteBlockEntity.TYPE, NoteBlockEntityRenderer::new);
    }

}
