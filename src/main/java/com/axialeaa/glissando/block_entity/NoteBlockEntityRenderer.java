package com.axialeaa.glissando.block_entity;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.data.instrument_formation.MultiBlockInstrumentFormation;
import com.axialeaa.glissando.util.AnimationMath;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class NoteBlockEntityRenderer implements BlockEntityRenderer<NoteBlockEntity> {

    private final BlockRenderManager manager;

    public NoteBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.manager = ctx.getRenderManager();
    }

    @Override
    public void render(NoteBlockEntity blockEntity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        World world = blockEntity.getWorld();

        if (world == null) {
            world = MinecraftClient.getInstance().world;
            assert world != null;
        }

        this.pulse(world, blockEntity, tickProgress, matrices, vertexConsumers, overlay);

        if (Glissando.clientConfig.debugRendering.get())
            Debug.render(world, blockEntity, tickProgress, matrices, vertexConsumers);
    }

    private void pulse(World world, NoteBlockEntity blockEntity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int overlay) {
        matrices.push();

        long ticksSinceStarted = world.getTime() - blockEntity.lastPoweredTicks;
        AnimationMath.Pulse pulseAnimation = new AnimationMath.Pulse(ticksSinceStarted);

        pulseAnimation.transformNoteBlockMatrices(matrices, tickProgress);
        this.renderModel(blockEntity.getPos(), blockEntity.getCachedState(), matrices, vertexConsumers, world, !pulseAnimation.active(tickProgress), overlay);

        matrices.pop();
    }

    private void renderModel(BlockPos pos, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, boolean cull, int overlay) {
        RenderLayer renderLayer = RenderLayers.getMovingBlockLayer(state);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);

        List<BlockModelPart> list = this.manager.getModel(state).getParts(Random.create(state.getRenderingSeed(pos)));

        this.manager.getModelRenderer().render(world, list, state, pos, matrices, vertexConsumer, cull, overlay);
    }

    @Override
    public int getRenderDistance() {
        return MinecraftClient.getInstance().options.getClampedViewDistance() * 16;
    }

    public static class Debug {

        private static final int COLOR = Colors.CYAN;
        private static final Box BOX = Box.from(Vec3d.ZERO).expand(MultiBlockInstrumentFormation.MAX_RANGE);
        private static final int ANIMATION_TIME = 20; // in ticks
        private static final float SIDE_STARTING_ALPHA = 0.3F;
        private static final float TEXT_SIZE = 0.05F;
        private static final int TEXT_COLOR = Colors.WHITE;
        private static final Vec3d TEXT_POS = new Vec3d(0.5, 2.5, 0.5);

        private static void render(World world, NoteBlockEntity blockEntity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
            float progress = AnimationMath.getAnimationProgress(getTicksSinceLastCheck(world, blockEntity), tickProgress, ANIMATION_TIME);

            if (progress > 1)
                progress = 1;

            float r = ColorHelper.getRedFloat(COLOR),
                g = ColorHelper.getGreenFloat(COLOR),
                b = ColorHelper.getBlueFloat(COLOR);

            drawBox(matrices, vertexConsumers, progress, r, g, b);
            drawText(world, blockEntity, matrices, vertexConsumers);
        }

        private static long getTicksSinceLastCheck(World world, NoteBlockEntity blockEntity) {
            return world.getTime() - blockEntity.lastInstrumentCheckedTicks;
        }

        private static void drawBox(MatrixStack matrices, VertexConsumerProvider vertexConsumers, float progress, float r, float g, float b) {
            if (progress >= 0)
                DebugRenderer.drawBox(matrices, vertexConsumers, BOX, r, g, b, SIDE_STARTING_ALPHA * (1 - progress));

            r = MathHelper.lerp(progress, r, 1);
            g = MathHelper.lerp(progress, g, 1);
            b = MathHelper.lerp(progress, b, 1);

            VertexRendering.drawBox(matrices, vertexConsumers.getBuffer(RenderLayer.getLines()), BOX, r, g, b, 1);
        }

        private static void drawText(World world, NoteBlockEntity blockEntity, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
            if (Glissando.syncedConfig == null)
                return;

            Vec3d vec3d = blockEntity.getPos().toCenterPos().add(TEXT_POS);
            String string = "Since last check: " + getTicksSinceLastCheck(world, blockEntity);

            DebugRenderer.drawString(matrices, vertexConsumers, string, vec3d.x, vec3d.y, vec3d.z, TEXT_COLOR, TEXT_SIZE, true, 0, true);
        }

    }

}
