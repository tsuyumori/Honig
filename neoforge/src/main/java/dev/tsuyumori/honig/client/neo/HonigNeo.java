package dev.tsuyumori.honig.client.neo;

import dev.tsuyumori.honig.universal.HonigMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;

@Mod(HonigMod.MOD_ID)
public class HonigNeo {
    public HonigNeo(FMLModContainer container, IEventBus modBus, Dist dist) {
        HonigMod.init();
    }
}
