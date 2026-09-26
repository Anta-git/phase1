package katas;

import java.util.List;
import java.util.Map;

/**
 * Kata 3: Modern streams and sequenced collections (Java 16-21).
 */
public class K3Streams {

    public record Clip(String channel, String title, int views) {}

    /** TODO: Titles of clips with more than minViews views, highest views first. Use Stream.toList(). */
    public static List<String> popularTitles(List<Clip> clips, int minViews) {
        throw new UnsupportedOperationException("TODO");
    }

    /** TODO: Total views per channel (Collectors.groupingBy + summingInt). */
    public static Map<String, Integer> viewsByChannel(List<Clip> clips) {
        throw new UnsupportedOperationException("TODO");
    }

    /** TODO: Return "<first title> ... <last title>" using List.getFirst()/getLast() (Java 21 SequencedCollection). */
    public static String firstAndLast(List<Clip> clips) {
        throw new UnsupportedOperationException("TODO");
    }
}
