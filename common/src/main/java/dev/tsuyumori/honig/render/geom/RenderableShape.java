package dev.tsuyumori.honig.render.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import java.util.function.Consumer;

public interface RenderableShape<SELF extends RenderableShape<SELF>> {
    SELF setRadius(float radius);
    void render(VertexConsumer vBuf, PoseStack.Pose viewMat, Consumer<VertexConsumer> extraVertAttr);
}
