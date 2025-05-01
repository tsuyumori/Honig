package dev.tsuyumori.honig.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

import static net.minecraft.client.renderer.RenderStateShard.*;

public class HonigRenderTypes {

    public static final Function<ResourceLocation, RenderType> ENTITY_SOLID_TRIS = Util.memoize(
            resourceLocation -> {
                RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)
                        .setTextureState(new TextureStateShard(resourceLocation, false, false))
                        .setTransparencyState(NO_TRANSPARENCY)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(true);
                return RenderType.create("entity_solid", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 1536, true, false, compositeState);
            }
    );
}
