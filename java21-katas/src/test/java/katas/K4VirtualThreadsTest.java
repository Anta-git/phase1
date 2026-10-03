package katas;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class K4VirtualThreadsTest {

    @Test
    void stepA_startsTaskOnAVirtualThread() throws InterruptedException {
        var ranOnVirtualThread = new AtomicBoolean(false);

        Thread thread = K4VirtualThreads.startVirtual(() -> ranOnVirtualThread.set(Thread.currentThread().isVirtual()));
        thread.join();

        assertTrue(thread.isVirtual(), "returned thread should be virtual");
        assertTrue(ranOnVirtualThread.get(), "task should have run on that virtual thread");
    }

    @Test
    void stepB_fetchesOneResultThroughAFuture() throws Exception {
        assertEquals("ch1:300", K4VirtualThreads.fetchOne("ch1"));
    }

    @Test
    void stepC_fetchesConcurrentlyAndKeepsOrder() {
        List<String> channels = IntStream.range(0, 5_000).mapToObj(i -> "ch" + i).toList();

        List<String> results = assertTimeoutPreemptively(Duration.ofSeconds(5), () -> K4VirtualThreads.fetchAll(channels));

        assertEquals(5_000, results.size());
        assertEquals("ch0:300", results.getFirst());
        assertEquals("ch4999:600", results.getLast());
    }

    @Test
    void stepD_platformPoolIsLimitedByPoolSize() {
        List<String> channels = IntStream.range(0, 50).mapToObj(i -> "ch" + i).toList();

        long start = System.nanoTime();
        List<String> results = K4VirtualThreads.fetchAllOnPlatformPool(channels, 5);
        Duration took = Duration.ofNanos(System.nanoTime() - start);

        assertEquals(50, results.size());
        assertEquals("ch49:400", results.getLast());
        // 50 tasks / 5 threads = 10 rounds of 200 ms. Sleeps are never shorter than asked, so this can't be flaky.
        assertTrue(took.toMillis() >= 2_000, "expected at least 2s with a pool of 5, took " + took.toMillis() + "ms");
    }
}
