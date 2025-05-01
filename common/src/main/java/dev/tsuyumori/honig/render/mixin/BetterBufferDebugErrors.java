package dev.tsuyumori.honig.render.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(BufferBuilder.class)
public class BetterBufferDebugErrors {

    @WrapOperation(method = "endLastVertex", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;"))
    public Stream<String> endLastVertex(Stream<VertexFormatElement> instance, Function<VertexFormatElement, String> function, Operation<Stream<String>> original) {
        Stream<String> name = original.call(instance, function);
        return name.map(string -> switch (string) {
            case "UV0" -> string + " (main texture)";
            case "UV1" -> string + " (overlay texture)";
            case "UV2" -> string + " (light map)";
            default -> string;
        });
    }

}
