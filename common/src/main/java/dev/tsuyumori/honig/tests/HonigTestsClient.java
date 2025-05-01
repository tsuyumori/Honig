package dev.tsuyumori.honig.tests;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;

public class HonigTestsClient {

    public static void init() {
        EntityRendererRegistry.register(HonigTests.EntityRegistry.SPHERE_TEST, SphereEntityRenderer::new);
    }

}
