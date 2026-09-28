package katas;

/**
 * Kata 2: Sealed interfaces + pattern-matching switch.
 * The compiler knows every subtype of a sealed interface, so a switch over it needs no default branch.
 */
public class K2SealedAndSwitch {

    //Permits is unnecessary here as all uses are contained to this file. If this changes, permits must be present.
    public sealed interface StreamEvent permits Follow, Subscription, Raid {}

    public record Follow(String user) implements StreamEvent {}

    public record Subscription(String user, int tier, int months) implements StreamEvent {}

    public record Raid(String fromChannel, int viewers) implements StreamEvent {}

    /**
     * TODO: Build the on-screen alert text with a switch expression using record patterns, e.g.
     *   case Follow(var user) -> ...
     * Expected formats:
     *   Follow              -> "alice followed!"
     *   Subscription (m=1)  -> "bob subscribed at tier 2!"
     *   Subscription (m>1)  -> "bob resubscribed at tier 2 for 5 months!"   (hint: a `when` guard)
     *   Raid                -> "carol is raiding with 120 viewers!"
     * Do not add a default branch.
     */
    public static String alertText(StreamEvent event) {
        return switch (event) {
            case Follow f -> "%s followed!".formatted(f.user);
            case Subscription s when s.months == 1 -> "%s subscribed at tier %s!".formatted(s.user, s.tier);
            case Subscription s -> "%s resubscribed at tier %s for %s months!".formatted(s.user, s.tier, s.months);
            case Raid r -> "%s is raiding with %s viewers!".formatted(r.fromChannel, r.viewers);
        };
    }
}
