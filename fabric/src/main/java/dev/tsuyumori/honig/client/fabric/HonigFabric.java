package dev.tsuyumori.honig.client.fabric;

import dev.tsuyumori.honig.universal.HonigMod;
import net.fabricmc.api.ModInitializer;

public class HonigFabric implements ModInitializer {
    @Override
    public void onInitialize() { HonigMod.init(); }
}
