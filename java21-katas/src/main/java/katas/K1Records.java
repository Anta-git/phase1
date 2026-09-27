package katas;

import java.math.BigDecimal;

/**
 * Kata 1: Records.
 * Records replace most hand-written value/DTO classes (constructor, accessors, equals, hashCode, toString).
 */
public class K1Records {

    public record Money(BigDecimal amount, String currency) {
        public Money(BigDecimal amount, String currency) {
            this.amount = amount;
            this.currency = currency;
            if (currency == null || !currency.matches("[A-Z]{3}")) {
                throw new IllegalArgumentException();
            }
            if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException();
            }
        }

        public Money plus(Money other) {
            if (this.currency.matches(other.currency)) {
                return new Money(this.amount.add(other.amount), currency);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
