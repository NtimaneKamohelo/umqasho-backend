package za.co.umqasho.renting.owner.api;

import za.co.umqasho.renting.owner.domain.OwnerProfile;

import java.time.OffsetDateTime;
import java.util.*;

public record OwnerProfileResponse(
        UUID id,
        UUID userId,
        String email,
        String businessName,
        String phoneNumber,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static OwnerProfileResponse from(
            OwnerProfile profile
    ){
        return new OwnerProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getUser().getEmail(),
                profile.getBusinessName(),
                profile.getPhoneNumber(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
