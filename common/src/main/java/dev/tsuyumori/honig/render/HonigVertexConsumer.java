package dev.tsuyumori.honig.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface HonigVertexConsumer {
    default VertexConsumer honig$setColorIf(boolean condition, float r, float g, float b, float a) {
        if (condition) return setColor(r, g, b, a);
        return (VertexConsumer)this;
    }

    default VertexConsumer honig$setColorIf(boolean condition, int rgba) {
        if (condition) return setColor(rgba);
        return (VertexConsumer)this;
    }

    default VertexConsumer honig$setLightIf(boolean condition, int light) {
        if (condition) return setLight(light);
        return (VertexConsumer)this;
    }

    default VertexConsumer honig$setNormalIf(boolean condition, float x, float y, float z) {
        if (condition) return setNormal(x, y, z);
        return (VertexConsumer) this;
    }

    default VertexConsumer honig$setNormalIf(boolean condition, PoseStack.Pose pose, float x, float y, float z) {
        if (condition) return setNormal(pose, x, y, z);
        return (VertexConsumer)this;
    }

    default VertexConsumer honig$setOverlayIf(boolean condition, int i) {
        if (condition) return setOverlay(i);
        return (VertexConsumer)this;
    }

    default VertexConsumer honig$setUvIf(boolean condition, float u, float v) {
        if (condition) return setUv(u, v);
        return (VertexConsumer)this;
    }

    default VertexConsumer setColor(float r, float g, float b, float a) { throw new RuntimeException(); }
    default VertexConsumer setColor(int rgba) { throw new RuntimeException(); }
    default VertexConsumer setLight(int light) { throw new RuntimeException(); }
    default VertexConsumer setNormal(float x, float y, float z) { throw new RuntimeException(); }
    default VertexConsumer setNormal(PoseStack.Pose pose, float x, float y, float z) { throw new RuntimeException(); }
    default VertexConsumer setOverlay(int i) { throw new RuntimeException(); }
    default VertexConsumer setUv(float u, float v) { throw new RuntimeException(); }
}
