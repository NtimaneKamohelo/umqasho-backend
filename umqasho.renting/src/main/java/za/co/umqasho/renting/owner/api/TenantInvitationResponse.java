package za.co.umqasho.renting.owner.api;

import za.co.umqasho.renting.owner.domain.TenantInvitation;
import za.co.umqasho.renting.owner.domain.TenantInvitationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TenantInvitationResponse(
        UUID id,

        UUID propertyId,
        UUID unitId,

        String email,
        String firstName,
        String lastName,

        LocalDate leaseStartDate,
        LocalDate leaseEndDate,

        BigDecimal monthlyRent,
        BigDecimal depositAmount,
        int dueDay,

        TenantInvitationStatus status,

        OffsetDateTime expiresAt,
        OffsetDateTime createdAt
) {

    public static TenantInvitationResponse from(
            TenantInvitation invitation
    ) {

        return new TenantInvitationResponse(
                invitation.getId(),

                invitation.getProperty().getId(),
                invitation.getUnit().getId(),

                invitation.getEmail(),
                invitation.getFirstName(),
                invitation.getLastName(),

                invitation.getLeaseStartDate(),
                invitation.getLeaseEndDate(),

                invitation.getMonthlyRent(),
                invitation.getDepositAmount(),
                invitation.getDueDay(),

                invitation.getStatus(),

                invitation.getExpiresAt(),
                invitation.getCreatedAt()
        );
    }

}
