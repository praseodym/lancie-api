package ch.wisv.areafiftylan.exception;

/**
 * Created by sille on 24-12-15.
 */
public class ImmutableOrderException extends RuntimeException {

    public ImmutableOrderException(Long orderId) {
        super("No more tickets can be added to Order " + orderId);
    }
}
