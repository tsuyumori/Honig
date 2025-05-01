package dev.tsuyumori.honig.universal;

import java.util.function.Supplier;

public interface GeneralUtil {

    /// Makes declaring and simultaneously using a supplier a bit more concise - you shouldn't
    /// really need to use this unless you conditionally load mod classes on NeoForge
    static <T> T defer(Supplier<T> supplier) { return supplier.get(); }
    static void defer(Runnable runnable) { runnable.run(); }
}
