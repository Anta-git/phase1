package katas;

import katas.K1Records.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class K1RecordsTest {

    @Test
    void recordsGiveValueEquality() {
        assertEquals(new Money(new BigDecimal("5"), "USD"), new Money(new BigDecimal("5"), "USD"));
    }

    @Test
    void rejectsInvalidValues() {
        assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("-1"), "USD"));
        assertThrows(IllegalArgumentException.class, () -> new Money(BigDecimal.ONE, "usd"));
        assertThrows(IllegalArgumentException.class, () -> new Money(BigDecimal.ONE, "DOLLARS"));
        assertThrows(IllegalArgumentException.class, () -> new Money(null, "USD"));
    }

    @Test
    void addsSameCurrency() {
        var sum = new Money(new BigDecimal("2.50"), "USD").plus(new Money(new BigDecimal("1.25"), "USD"));
        assertEquals(new Money(new BigDecimal("3.75"), "USD"), sum);
    }

    @Test
    void refusesToAddDifferentCurrencies() {
        assertThrows(IllegalArgumentException.class,
                () -> new Money(BigDecimal.ONE, "USD").plus(new Money(BigDecimal.ONE, "EUR")));
    }
}
