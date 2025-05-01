package dev.tsuyumori.honig.render;

import com.mojang.datafixers.util.Pair;
import dev.tsuyumori.honig.universal.GeneralUtil;
import dev.architectury.platform.Platform;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.layer.*;
import net.irisshaders.iris.uniforms.SystemTimeUniforms;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;

import java.util.Optional;

/// Direct invocations of non-existing classes are not allowed on NeoForge,
/// so we create a wrapper for frequently used ones<br><br>
/// Other classes should be accessed through creating a supplier then using it, see {@link GeneralUtil#defer}
public interface IrisCompatHelper {

    MultiBufferSource forcePipelinePhase(MultiBufferSource buffers, String name, RenderStateShard renderStateShard);

    default Pair<SystemTimeUniforms.Timer, SystemTimeUniforms.FrameCounter> timeUniforms() {
        return Pair.of(SystemTimeUniforms.TIMER, SystemTimeUniforms.COUNTER);
    }

    /// Creates an instance of {@link IrisCompatHelper} if
    /// Iris is installed and shaders are enabled
    static Optional<IrisCompatHelper> maybeGet() {
        if (!Platform.isModLoaded("iris") || !GeneralUtil.defer(
                () -> IrisApi.getInstance().isShaderPackInUse()
        )) return Optional.empty();

        //noinspection Convert2Lambda
        return Optional.of(new IrisCompatHelper() {

            @Override
            public MultiBufferSource forcePipelinePhase(MultiBufferSource buffers, String name, RenderStateShard renderStateShard) {
                if (buffers instanceof BufferSourceWrapper wrapper) {
                    buffers = wrapper.getOriginal();
                }
                return new BufferSourceWrapper(buffers,  type -> OuterWrappedRenderType.wrapExactlyOnce(name, type, renderStateShard));
            }

        });
    }
}
