package katas;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Kata 4: Virtual threads (Java 21).
 *
 * <h2>The problem they solve</h2>
 * A typical backend request spends most of its time <em>waiting</em>: on the database, on another
 * service's HTTP API, on a cache. The classic model is "one thread per request", and the thread just
 * sits blocked while it waits.
 * <p>
 * A normal ("platform") thread is a real OS thread: ~1 MB of stack and expensive to create. So we
 * put them in a fixed pool, e.g. 200 threads. If each request blocks for 200 ms, that pool can serve
 * at most 200 / 0.2 s = 1,000 requests per second, even though the CPU is nearly idle. The
 * threads are the bottleneck, not the hardware.
 * <p>
 * The old fix was reactive/async code (CompletableFuture chains, WebFlux, callbacks). It scales,
 * but it's harder to read, debug, and profile.
 *
 * <h2>What a virtual thread is</h2>
 * A virtual thread is a {@link Thread} managed by the JVM rather than the OS. It costs a few hundred
 * bytes and is cheap to create, so you can have millions. When a virtual thread blocks
 * (sleep, socket read, JDBC call), the JVM <em>unmounts</em> it from the OS thread it was running on
 * ("carrier thread") and runs another virtual thread there instead. When the I/O completes, it gets
 * mounted again and continues.
 * <p>
 * Result: you write plain, blocking, top-to-bottom code, and it scales like async code.
 *
 * <h2>The rules of thumb</h2>
 * <ul>
 *   <li>Don't pool virtual threads. They're cheap, so create one per task and throw it away.</li>
 *   <li>They help with <b>waiting</b> (I/O), not <b>computing</b>. CPU-bound work still only has as
 *       many cores as the machine does.</li>
 *   <li>To limit access to a scarce resource (e.g. max 10 DB connections), use a Semaphore or
 *       connection pool, not a small thread pool.</li>
 * </ul>
 *
 * <h2>The tools you'll use</h2>
 * <ul>
 *   <li>{@code Thread.ofVirtual().start(runnable)} starts one virtual thread directly.</li>
 *   <li>{@code Executors.newVirtualThreadPerTaskExecutor()} is an {@code ExecutorService} that starts a new
 *       virtual thread for each task you {@code submit}.</li>
 *   <li>{@code executor.submit(callable)} returns a {@code Future<T>}, a handle to a result that
 *       may not exist yet. {@code future.get()} blocks until it does.</li>
 *   <li>{@code ExecutorService} is {@code AutoCloseable} (Java 19+). Closing it <b>waits for every
 *       submitted task to finish</b>, so try-with-resources gives you a natural "wait for all" point.</li>
 * </ul>
 *
 * Work through the steps in order; each has its own test in K4VirtualThreadsTest.
 * When all pass, run {@link K4Demo} and then answer the questions in K4-QUESTIONS.md.
 */
public class K4VirtualThreads {

    /** Simulates a slow blocking I/O call, e.g. an HTTP request to another service. */
    public static String fetchViewerCount(String channel) {
        try {
            Thread.sleep(Duration.ofMillis(200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return channel + ":" + channel.length() * 100;
    }

    /**
     * Step A: Start {@code task} on a new virtual thread and return the thread.
     * One line. Look at {@code Thread.ofVirtual()}.
     * (The test calls {@code join()} on the returned thread to wait for it.)
     */
    public static Thread startVirtual(Runnable task) {
        return Thread.ofVirtual().start(task);
    }

    /**
     * Step B: Fetch one channel's count on a virtual thread via an executor, and return the result.
     * <ol>
     *   <li>Open {@code Executors.newVirtualThreadPerTaskExecutor()} in try-with-resources.</li>
     *   <li>{@code submit} a lambda that calls {@link #fetchViewerCount}. You get a {@code Future<String>}.</li>
     *   <li>Return {@code future.get()}.</li>
     * </ol>
     * {@code get()} throws two checked exceptions, which is why this method declares them.
     * On its own this is slower than calling fetchViewerCount directly; it's practice for Step C.
     */
    public static String fetchOne(String channel) throws InterruptedException, ExecutionException {
        try (ExecutorService myExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> future = myExecutor.submit(() -> fetchViewerCount(channel));
            return future.get();
        }
    }

    /**
     * Step C: Call fetchViewerCount for every channel concurrently, one virtual thread each.
     * Return results in the same order as the input.
     * The test uses 5,000 channels and must finish in under 5 seconds (done one at a time it'd take 1,000 s).
     * <p>
     * Shape: the same as Step B, but submit all the tasks <b>before</b> waiting on any of them.
     * If you call get() right after each submit, you're back to doing them one at a time.
     * <p>
     * Note this method does <b>not</b> declare checked exceptions, so {@code get()} is awkward to call
     * inside a stream lambda. Look at {@code Future.resultNow()} (Java 19+): it returns the
     * result without checked exceptions, but only works once the task has already finished.
     * When is every task guaranteed to be finished?
     */
    public static List<String> fetchAll(List<String> channels) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            return fetchAllWith(executor, channels);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * Step D: The "before virtual threads" version, for contrast.
     * Same as Step C, but use {@code Executors.newFixedThreadPool(poolSize)}: a pool of
     * {@code poolSize} reusable platform threads. Only {@code poolSize} tasks can be sleeping at once;
     * the rest wait in a queue.
     * <p>
     * Before running the test, predict: 50 channels, pool of 5, 200 ms each. How long will it take?
     */
    public static List<String> fetchAllOnPlatformPool(List<String> channels, int poolSize) {
        try (ExecutorService executor = Executors.newFixedThreadPool(poolSize)) {
            return fetchAllWith(executor, channels);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }


    private static List<String> fetchAllWith(ExecutorService executor, List<String> channels) throws ExecutionException, InterruptedException {
        List<Future<String>> futures = new java.util.ArrayList<>(List.of());
        List<String> results = new ArrayList<>();

        for (String channel : channels) {
            futures.add(executor.submit(() -> fetchViewerCount(channel)));
        }

        for (Future<String> future : futures) {
            results.add(future.get());
        }

        return results;
    }
}
