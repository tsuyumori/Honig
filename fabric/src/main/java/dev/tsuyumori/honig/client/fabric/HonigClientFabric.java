package dev.tsuyumori.honig.client.fabric;

import dev.tsuyumori.honig.universal.HonigClient;
import net.fabricmc.api.ClientModInitializer;

public final class HonigClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HonigClient.init();
    }
}
