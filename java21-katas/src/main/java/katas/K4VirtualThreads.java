package katas;

import java.time.Duration;
import java.util.List;

/**
 * Kata 4: Virtual threads (Java 21).
 * Cheap threads that let plain blocking code scale. Spring Boot uses them when
 * spring.threads.virtual.enabled=true.
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
     * TODO: Call fetchViewerCount for every channel concurrently, one virtual thread each,
     * using Executors.newVirtualThreadPerTaskExecutor() in try-with-resources.
     * Return results in the same order as the input.
     * The test uses 5,000 channels and must finish in under 5 seconds.
     */
    public static List<String> fetchAll(List<String> channels) {
        throw new UnsupportedOperationException("TODO");
    }
}
