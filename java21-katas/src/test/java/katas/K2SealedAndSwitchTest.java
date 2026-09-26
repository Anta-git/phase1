package katas;

import katas.K2SealedAndSwitch.*;
import org.junit.jupiter.api.Test;

import static katas.K2SealedAndSwitch.alertText;
import static org.junit.jupiter.api.Assertions.assertEquals;

class K2SealedAndSwitchTest {

    @Test
    void follow() {
        assertEquals("alice followed!", alertText(new Follow("alice")));
    }

    @Test
    void newSub() {
        assertEquals("bob subscribed at tier 2!", alertText(new Subscription("bob", 2, 1)));
    }

    @Test
    void resub() {
        assertEquals("bob resubscribed at tier 2 for 5 months!", alertText(new Subscription("bob", 2, 5)));
    }

    @Test
    void raid() {
        assertEquals("carol is raiding with 120 viewers!", alertText(new Raid("carol", 120)));
    }
}
