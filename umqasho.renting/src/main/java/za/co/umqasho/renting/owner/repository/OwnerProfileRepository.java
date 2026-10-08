package za.co.umqasho.renting.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.umqasho.renting.owner.domain.OwnerProfile;

import java.util.Optional;
import java.util.UUID;

public interface OwnerProfileRepository extends JpaRepository<OwnerProfile, UUID> {

    Optional<OwnerProfile> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}
