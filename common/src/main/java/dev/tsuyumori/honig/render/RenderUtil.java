package dev.tsuyumori.honig.render;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface RenderUtil {
    float PHI = (1 + Mth.sqrt(5)) / 2;
    float PHI_INVERSE = 1 / PHI;

    /// Contains Geometry helpers
    interface Geo {
        static Vector3f unitSphere(float u, float v, float r) {
            return new Vector3f(Mth.cos(u) * Mth.sin(v) * r, Mth.cos(v) * r, Mth.sin(u) * Mth.sin(v) * r);
        }
    }

    /// Contains Matrix helper
    interface Mat {
       static Vector3f worldPosition(Matrix4f modelMat) {
           return new Vector3f(modelMat.m30(), modelMat.m31(), modelMat.m32());
       }
    }

    interface Screen {
        static Camera mainCamera() {
            return Minecraft.getInstance().gameRenderer.getMainCamera();
        }
    }
}
