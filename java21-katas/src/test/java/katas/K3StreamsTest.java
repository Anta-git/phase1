package katas;

import katas.K3Streams.Clip;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class K3StreamsTest {

    static final List<Clip> CLIPS = List.of(
            new Clip("shroud", "Insane flick", 900),
            new Clip("pokimane", "Chat reacts", 300),
            new Clip("shroud", "1v5 clutch", 1500),
            new Clip("xqc", "Juice", 50));

    @Test
    void popularTitles() {
        assertEquals(List.of("1v5 clutch", "Insane flick", "Chat reacts"), K3Streams.popularTitles(CLIPS, 100));
    }

    @Test
    void viewsByChannel() {
        assertEquals(Map.of("shroud", 2400, "pokimane", 300, "xqc", 50), K3Streams.viewsByChannel(CLIPS));
    }

    @Test
    void firstAndLast() {
        assertEquals("Insane flick ... Juice", K3Streams.firstAndLast(CLIPS));
    }
}
