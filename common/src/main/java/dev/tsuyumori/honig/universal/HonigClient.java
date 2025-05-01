package dev.tsuyumori.honig.universal;

import dev.tsuyumori.honig.tests.HonigTestsClient;
import dev.architectury.platform.Platform;

public class HonigClient {


    public static void init() {
        if (Platform.isDevelopmentEnvironment()) {
            HonigTestsClient.init();
        }
    }
}
