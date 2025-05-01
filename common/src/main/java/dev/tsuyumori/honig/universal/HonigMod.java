package dev.tsuyumori.honig.universal;

import dev.tsuyumori.honig.tests.HonigTests;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;

public class HonigMod {
    public static final String MOD_ID = "honig";

    public static void init() {
        if (Platform.isDevelopmentEnvironment()) {
            HonigTests.init();
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
