package za.co.umqasho.renting.owner.api;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTenantInvitationRequest(

        @NotBlank
        @Email
        @Size(max = 320)
        String email,

        @Size(max = 32)
        String phoneNumber,

        @NotBlank
        @Size(max = 100)
        String firstName,

        @NotBlank
        @Size(max = 100)
        String lastName,

        @NotNull
        LocalDate leaseStartDate,

        @NotNull
        LocalDate leaseEndDate,

        @NotNull
        @DecimalMin(
                value = "0.01",
                inclusive = true
        )
        BigDecimal monthlyRent,

        @NotNull
        @DecimalMin(
                value = "0.00",
                inclusive = true
        )
        BigDecimal depositAmount,

        @NotNull
        @Min(1)
        @Max(28)
        Integer dueDay

) {
}
