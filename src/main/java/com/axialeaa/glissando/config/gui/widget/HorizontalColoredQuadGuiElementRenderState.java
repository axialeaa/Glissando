package com.axialeaa.glissando.config.gui.widget;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public record HorizontalColoredQuadGuiElementRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose, int minX, int minY, int maxX, int maxY, int leftColor, int rightColor, @Nullable ScreenRect scissorArea, @Nullable ScreenRect bounds) implements SimpleGuiElementRenderState {

	public HorizontalColoredQuadGuiElementRenderState(Matrix3x2f pose, int minX, int minY, int maxX, int maxY, int leftColor, int rightColor, @Nullable ScreenRect scissorArea) {
		this(RenderPipelines.GUI, TextureSetup.empty(), pose, minX, minY, maxX, maxY, leftColor, rightColor, scissorArea, createBounds(minX, minY, maxX, maxY, pose, scissorArea));
	}

	public static void fill(DrawContext context, int minX, int minY, int maxX, int maxY, int leftColor, int rightColor) {
		Matrix3x2f pose = new Matrix3x2f(context.getMatrices());
		ScreenRect scissorArea = context.scissorStack.peekLast();

		context.state.addSimpleElement(new HorizontalColoredQuadGuiElementRenderState(pose, minX, minY, maxX, maxY, leftColor, rightColor, scissorArea));
	}

	@Override
	public void setupVertices(VertexConsumer vertexConsumer, float depth) {
		vertexConsumer.vertex(this.pose, this.minX, this.minY, depth).color(this.leftColor);
		vertexConsumer.vertex(this.pose, this.minX, this.maxY, depth).color(this.leftColor);
		vertexConsumer.vertex(this.pose, this.maxX, this.maxY, depth).color(this.rightColor);
		vertexConsumer.vertex(this.pose, this.maxX, this.minY, depth).color(this.rightColor);
	}

	@Nullable
	private static ScreenRect createBounds(int minX, int minY, int maxX, int maxY, Matrix3x2f pose, @Nullable ScreenRect scissorArea) {
		ScreenRect screenRect = new ScreenRect(minX, minY, maxX - minX, maxY - minY).transformEachVertex(pose);
		return scissorArea != null ? scissorArea.intersection(screenRect) : screenRect;
	}

}