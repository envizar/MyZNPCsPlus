package lol.pyr.znpcsplus.util;

import org.bukkit.entity.Player;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public abstract class Viewable {
    private final static List<WeakReference<Viewable>> all = new ArrayList<>();

    public static List<Viewable> all() {
        all.removeIf(reference -> reference.get() == null);
        return all.stream()
                .map(Reference::get)
                .collect(Collectors.toList());
    }

    private boolean queueRunning = false;
    private final Queue<Runnable> visibilityTaskQueue = new ConcurrentLinkedQueue<>();
    private final Set<Player> viewers = new HashSet<>();

    public Viewable() {
        all.add(new WeakReference<>(this));
    }

    private void tryRunQueue() {
        if (visibilityTaskQueue.isEmpty() || queueRunning) return;
        queueRunning = true;
        FutureUtil.exceptionPrintingRunAsync(() -> {
            while (!visibilityTaskQueue.isEmpty()) try {
                visibilityTaskQueue.remove().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            queueRunning = false;
        });
    }

    private void queueVisibilityTask(Runnable runnable) {
        visibilityTaskQueue.add(runnable);
        tryRunQueue();
    }

    public void delete() {
        queueVisibilityTask(() -> {
            UNSAFE_hideAll();
            viewers.clear();
        });
    }

    public CompletableFuture<Void> respawn() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        queueVisibilityTask(() -> {
            UNSAFE_hideAll();
            UNSAFE_showAll().join();
            future.complete(null);
        });
        return future;
    }

    public CompletableFuture<Void> respawn(Player player) {
        hide(player);
        return show(player);
    }

    public CompletableFuture<Void> show(Player player) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        queueVisibilityTask(() -> {
            if (viewers.contains(player)) {
                future.complete(null);
                return;
            }
            viewers.add(player);
            UNSAFE_show(player).join();
            future.complete(null);
        });
        return future;
    }

    public void hide(Player player) {
        queueVisibilityTask(() -> {
            if (!viewers.contains(player)) return;
            viewers.remove(player);
            UNSAFE_hide(player);
        });
    }

    public void UNSAFE_removeViewer(Player player) {
        viewers.remove(player);
    }

    protected void UNSAFE_hideAll() {
        for (Player viewer : viewers) UNSAFE_hide(viewer);
    }

    protected CompletableFuture<Void> UNSAFE_showAll() {
        return FutureUtil.allOf(viewers.stream()
                .map(this::UNSAFE_show)
                .collect(Collectors.toList()));
        // for (Player viewer : viewers) UNSAFE_show(viewer);
    }

    public Set<Player> getViewers() {
        return Collections.unmodifiableSet(viewers);
    }

    public boolean isVisibleTo(Player player) {
        return viewers.contains(player);
    }

    protected abstract CompletableFuture<Void> UNSAFE_show(Player player);

    protected abstract void UNSAFE_hide(Player player);
}
