package dev.tsuyumori.honig.tests;

import dev.tsuyumori.honig.render.HonigRenderTypes;
import dev.tsuyumori.honig.render.IrisCompatHelper;
import dev.tsuyumori.honig.render.RenderUtil;
import dev.tsuyumori.honig.render.geom.PolyhedralGeometry;
import dev.tsuyumori.honig.render.geom.PlatonicSolids;
import com.mojang.blaze3d.vertex.*;
import dev.tsuyumori.honig.universal.GeneralUtil;
import dev.tsuyumori.honig.universal.HonigMod;
import net.irisshaders.iris.layer.BlockEntityRenderStateShard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.util.concurrent.atomic.AtomicReference;

public class SphereEntityRenderer extends EntityRenderer<SphereTestEntity> {
    protected SphereEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(SphereTestEntity entity) {
        return null;
    }

    private static final float RED = 0.075f;
    private static final float GREEN = 0.15f;
    private static final float BLUE = 0.2f;

    @Override
    public void render(SphereTestEntity entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        super.render(entity, f, g, poseStack, multiBufferSource, light);
        boolean shaders = IrisCompatHelper.maybeGet().isPresent();

        AtomicReference<VertexConsumer> vBuf = new AtomicReference<>();

        IrisCompatHelper.maybeGet().ifPresentOrElse(compat -> {
            //float uProgress = compat.timeUniforms().getFirst().getFrameTimeCounter() * 0.01F % 1;
            //int id = GeneralUtil.defer(() -> WorldRenderingSettings.INSTANCE.getBlockStateIds().getOrDefault(Blocks.END_PORTAL.defaultBlockState(), -1));
            //GeneralUtil.defer(() -> CapturedRenderingState.INSTANCE.setCurrentBlockEntity(id));

            MultiBufferSource entityBuffers = compat.forcePipelinePhase(multiBufferSource, "iris:is_block_entity", GeneralUtil.defer(() -> BlockEntityRenderStateShard.INSTANCE));
            vBuf.set(entityBuffers.getBuffer(HonigRenderTypes.ENTITY_SOLID_TRIS.apply(HonigMod.id("textures/entity/sphere_test.png"))));

        }, () -> vBuf.set(multiBufferSource.getBuffer(HonigRenderTypes.ENTITY_SOLID_TRIS.apply(HonigMod.id("textures/entity/sphere_test.png")))));

        poseStack.pushPose();
        poseStack.translate(0, .5F, 0);
        PoseStack.Pose modelMat = poseStack.last();
        poseStack.popPose();

        Vector3f cameraPosition = RenderUtil.Screen.mainCamera().getPosition().toVector3f();
        float distance = cameraPosition.distance(entity.position().toVector3f());

        PolyhedralGeometry sphere = PlatonicSolids.ICOSAHEDRON.get().setRadius(.5F).autoDetail(distance);

        sphere.render(vBuf.get(), modelMat, vertex -> vertex.setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).honig$setColorIf(shaders, .075F, .15F, .2F, 1));
    }
}
