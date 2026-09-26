package katas;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class K4VirtualThreadsTest {

    @Test
    void fetchesConcurrentlyAndKeepsOrder() {
        List<String> channels = IntStream.range(0, 5_000).mapToObj(i -> "ch" + i).toList();

        List<String> results = assertTimeoutPreemptively(Duration.ofSeconds(5), () -> K4VirtualThreads.fetchAll(channels));

        assertEquals(5_000, results.size());
        assertEquals("ch0:300", results.getFirst());
        assertEquals("ch4999:600", results.getLast());
    }
}
