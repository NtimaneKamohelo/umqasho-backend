package za.co.umqasho.renting.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.umqasho.renting.owner.domain.TenantInvitation;
import za.co.umqasho.renting.owner.domain.TenantInvitationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantInvitationRespository extends JpaRepository<TenantInvitation, UUID> {

    Optional<TenantInvitation> findByTokenHash(
            String tokenHash
    );

    List<TenantInvitation> findAllByPropertyIdOrderByCreatedAtDesc(
            UUID propertyId
    );

    List<TenantInvitation> findallbyUnitIdAndStatus(
            UUID unitId,
            TenantInvitationStatus status
    );

}
