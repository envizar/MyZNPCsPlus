package lol.pyr.znpcsplus.util;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class FutureUtil {
    public static CompletableFuture<Void> allOf(Collection<CompletableFuture<?>> futures) {
        return CompletableFuture.runAsync(() -> {
            for (CompletableFuture<?> future : futures) future.join();
        });
    }
}
