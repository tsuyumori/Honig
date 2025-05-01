package dev.tsuyumori.honig.render.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.tsuyumori.honig.render.RenderUtil;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class UVSphereGeometry implements RenderableShape<UVSphereGeometry> {

    int longs, lats;
    float radius;

    UVSphereGeometry() { this(32, 16); }

    UVSphereGeometry(int longs, int lats) {
        this.longs = longs; this.lats = lats;
    }

    @Override
    public UVSphereGeometry setRadius(float radius) {
        this.radius = radius; return this;
    }

    public UVSphereGeometry setDetail(int longs, int lats) {
        this.longs = longs; this.lats = lats; return this;
    }

    @Override
    public void render(VertexConsumer vBuf, PoseStack.Pose modelMat, Consumer<VertexConsumer> extraVertAttr) {
        float startU = 0, startV = 0;
        float endU = Mth.PI * 2, endV = Mth.PI;
        float stepU = (endU - startU) / longs;
        float stepV = (endV - startV) / lats;

        for (int i = 0; i < longs; ++i) {
            for (int j = 0; j < lats; ++j) {
                float u = i * stepU + startU;
                float v = j * stepV + startV;
                float un = (i + 1 == longs) ? endU : (i + 1) * stepU + startU;
                float vn = (j + 1 == lats) ? endV : (j + 1) * stepV + startV;

                Vector3f p0 = RenderUtil.Geo.unitSphere(u, v, radius);
                Vector3f p1 = RenderUtil.Geo.unitSphere(u, vn, radius);
                Vector3f p2 = RenderUtil.Geo.unitSphere(un, v, radius);
                Vector3f p3 = RenderUtil.Geo.unitSphere(un, vn, radius);

                Vector3f n0 = p0.normalize();
                Vector3f n1 = p1.normalize();
                Vector3f n2 = p2.normalize();
                Vector3f n3 = p3.normalize();

                float textureU = u / endU;
                float textureV = v / endV;
                float textureUN = un / endU;
                float textureVN = vn / endV;

                extraVertAttr.accept(vBuf.addVertex(modelMat, p0).setUv(textureU, textureV).setNormal(modelMat, n0.x(), n0.y(), n0.z()));
                extraVertAttr.accept(vBuf.addVertex(modelMat, p2).setUv(textureUN, textureV).setNormal(modelMat, n2.x(), n2.y(), n2.z()));
                extraVertAttr.accept(vBuf.addVertex(modelMat, p1).setUv(textureU, textureVN).setNormal(modelMat, n1.x(), n1.y(), n1.z()));

                extraVertAttr.accept(vBuf.addVertex(modelMat, p3).setUv(textureUN, textureVN).setNormal(modelMat, n3.x(), n3.y(), n3.z()));
                extraVertAttr.accept(vBuf.addVertex(modelMat, p1).setUv(textureU, textureVN).setNormal(modelMat, n1.x(), n1.y(), n1.z()));
                extraVertAttr.accept(vBuf.addVertex(modelMat, p2).setUv(textureUN, textureV).setNormal(modelMat, n2.x(), n2.y(), n2.z()));
            }
        }
    }
}
