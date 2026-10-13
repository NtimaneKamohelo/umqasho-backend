package za.co.umqasho.renting.owner.api;

import jakarta.validation.constraints.Size;

public record UpdateOwnerProfileRequest(
        @Size(
                max = 225,
                message = "Business name must not excedd 225 charackers"
        )
        String businessName,

        @Size(
                max = 32,
                message = "Phone number must not exceed 32 characters."
        )
        String phoneNumber
) {
}
