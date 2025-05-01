package dev.tsuyumori.honig.tests;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class HonigTests {

    public static void init() {
        EntityRegistry.init();
    }

    public static class EntityRegistry {
        public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create("honig", Registries.ENTITY_TYPE);

        public static final RegistrySupplier<EntityType<SphereTestEntity>> SPHERE_TEST = registerEntityType(
                "sphere_test", () -> EntityType.Builder.of(SphereTestEntity::new, MobCategory.MONSTER).sized(1F, 1F).clientTrackingRange(10).build("sphere_test")
        );

        private static <T extends EntityType<?>> RegistrySupplier<T> registerEntityType(final String name, final Supplier<T> type) {
            return ENTITY_TYPES.register(ResourceLocation.fromNamespaceAndPath("honig", name), type);
        }

        public static void init() {
            ENTITY_TYPES.register();
        }
    }
}
