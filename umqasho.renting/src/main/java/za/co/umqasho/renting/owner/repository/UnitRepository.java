package za.co.umqasho.renting.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.umqasho.renting.owner.domain.Unit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {

    List<Unit> findAllByPropertyIdOrderByUnitNumberAsc(UUID propertyId);

    Optional<Unit> findByIdandPropertyId(
            UUID unitId,
            UUID propertyId
    );

    boolean existsByPropertyIdAndUnitNumberIgnoreCase(
            UUID propertyId,
            String unitNumber
    );

}
