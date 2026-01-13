package com.axialeaa.glissando.config.gui.leonard;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

public record FloatTexturedQuadGuiElementRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose, float x1, float y1, float x2, float y2, float u1, float u2, float v1, float v2, int color, float rotationRad, @Nullable ScreenRect scissorArea, @Nullable ScreenRect bounds) implements SimpleGuiElementRenderState {

	public FloatTexturedQuadGuiElementRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose, float x1, float y1, float x2, float y2, float u1, float u2, float v1, float v2, int color, float rotationRad, @Nullable ScreenRect scissorArea) {
		this(pipeline, textureSetup, pose, x1, y1, x2, y2, u1, u2, v1, v2, color, rotationRad, scissorArea, createBounds(x1, y1, x2, y2, pose, scissorArea));
	}

	@Override
	public void setupVertices(VertexConsumer vertices, float depth) {
		this.pose.rotateAbout(this.rotationRad, this.getCenter().x, this.getCenter().y);
		vertices.vertex(this.pose, this.x1, this.y1, depth).texture(this.u1, this.v1).color(this.color);
		vertices.vertex(this.pose, this.x1, this.y2, depth).texture(this.u1, this.v2).color(this.color);
		vertices.vertex(this.pose, this.x2, this.y2, depth).texture(this.u2, this.v2).color(this.color);
		vertices.vertex(this.pose, this.x2, this.y1, depth).texture(this.u2, this.v1).color(this.color);
	}

	@Nullable
	private static ScreenRect createBounds(float x1, float y1, float x2, float y2, Matrix3x2f pose, @Nullable ScreenRect scissorArea) {
		ScreenRect screenRect = new ScreenRect((int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1)).transformEachVertex(pose);
		return scissorArea != null ? scissorArea.intersection(screenRect) : screenRect;
	}

	private Vector2f getCenter() {
		return new Vector2f(MathHelper.lerp(0.5F, this.x1, this.x2), MathHelper.lerp(0.5F, this.y1, this.y2));
	}

}
