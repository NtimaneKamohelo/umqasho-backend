package za.co.umqasho.renting.common.security;

import java.util.UUID;

public record UmqashoAuthenticationDetails(
        UUID userId,
        String email,
        String firebaseUid
) {
}
