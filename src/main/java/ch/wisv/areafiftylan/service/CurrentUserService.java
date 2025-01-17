package ch.wisv.areafiftylan.service;

/**
 * The CurrentUserService is for permissions that require some logic to be determined.
 */
public interface CurrentUserService {
    boolean canAccessUser(Object principal, Long userId);

    boolean canAccessTeam(Object principal, Long teamId);

    boolean canEditTeam(Object principal, Long teamId);

    boolean canAccessOrder(Object principal, Long orderId);
}