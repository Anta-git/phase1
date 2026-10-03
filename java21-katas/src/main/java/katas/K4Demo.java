package katas;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Run this after Steps C and D pass (right-click → Run 'K4Demo.main()' in IntelliJ).
 * Same 1,000 blocking calls, three ways. Before running, write down how long you expect each to take.
 */
public class K4Demo {

    public static void main(String[] args) {
        List<String> channels = IntStream.range(0, 1_000).mapToObj(i -> "ch" + i).toList();

        time("Platform pool of 10 threads ", () -> K4VirtualThreads.fetchAllOnPlatformPool(channels, 10));
        time("Platform pool of 200 threads", () -> K4VirtualThreads.fetchAllOnPlatformPool(channels, 200));
        time("Virtual threads             ", () -> K4VirtualThreads.fetchAll(channels));
    }

    private static void time(String label, Supplier<List<String>> work) {
        long start = System.nanoTime();
        int n = work.get().size();
        System.out.printf("%s  %,6d ms  (%d results)%n", label, (System.nanoTime() - start) / 1_000_000, n);
    }
}
