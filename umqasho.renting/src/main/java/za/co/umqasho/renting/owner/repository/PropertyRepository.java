package za.co.umqasho.renting.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.umqasho.renting.owner.domain.Property;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID> {

    List<Property> findAllByOwnerIdOrderByCreatedAtDesc(UUID ownerId);

    Optional<Property> findByIdAndOwnerId(
            UUID propertyId,
            UUID ownerId
    );

    boolean existsByIdAndOwnerId(
            UUID propertyId,
            UUID ownerId
    );

}
