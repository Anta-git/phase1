package katas;

import java.math.BigDecimal;

/**
 * Kata 1: Records.
 * Records replace most hand-written value/DTO classes (constructor, accessors, equals, hashCode, toString).
 */
public class K1Records {

    /**
     * TODO: Add a compact constructor that validates:
     *  - currency is a non-null 3-letter uppercase code (e.g. "USD")
     *  - amount is non-null and >= 0
     * Throw IllegalArgumentException otherwise.
     */
    public record Money(BigDecimal amount, String currency) {

        /** TODO: Return a new Money with the amounts added. Throw IllegalArgumentException if currencies differ. */
        public Money plus(Money other) {
            throw new UnsupportedOperationException("TODO");
        }
    }
}
