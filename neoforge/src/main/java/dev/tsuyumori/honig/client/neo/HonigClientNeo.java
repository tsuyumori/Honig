package dev.tsuyumori.honig.client.neo;

import dev.tsuyumori.honig.universal.HonigClient;
import dev.tsuyumori.honig.universal.HonigMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;

@Mod(value = HonigMod.MOD_ID, dist = Dist.CLIENT)
public class HonigClientNeo {

    public HonigClientNeo(FMLModContainer container, IEventBus modBus, Dist dist) {
        HonigClient.init();
    }

}
