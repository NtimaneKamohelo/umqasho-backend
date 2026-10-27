package za.co.umqasho.renting.owner.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import za.co.umqasho.renting.common.security.CurrentUser;
import za.co.umqasho.renting.owner.domain.Property;
import za.co.umqasho.renting.owner.domain.Unit;
import za.co.umqasho.renting.owner.repository.PropertyRepository;
import za.co.umqasho.renting.owner.repository.UnitRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UnitService {

    private final CurrentUser currentUser;
    private final PropertyRepository propertyRepository;
    private final UnitRepository unitRepository;

    public UnitService(
            CurrentUser currentUser,
            PropertyRepository propertyRepository,
            UnitRepository unitRepository,
    ){
        this.currentUser = currentUser;
        this.propertyRepository = propertyRepository;
        this.unitRepository = unitRepository;
    }

    public Unit create(
            UUID propertyId,
            String unitNumber,
            Integer floor,
            Integer bedrooms,
            Integer bathrooms
    ){
        Property property =
                getOwnedProperty(propertyId);

        String normalizedUnitNumber = unitNumber.trim();

        if (
                unitRepository
                        .existsByPropertyIdAndUnitNumberIgnoreCase(
                                propertyId,
                                normalizedUnitNumber
                        )
        ){
            throw new IllegalArgumentException(
                    "A unit with this number already exists for the property."
            );
        }

        return unitRepository.save(
                new Unit(
                        property,
                        normalizedUnitNumber,
                        floor,
                        bedrooms == null ? 0 : bedrooms,
                        bathrooms == null ? 0 : bathrooms
                )
        );
    }

    @Transactional
    public List<Unit> findAll(
            UUID propertyId
    ) {
        getOwnedProperty(propertyId);

        return unitRepository
                .findAllByPropertyIdOrderByUnitNumberAsc(
                        propertyId
                );
    }

    @Transactional
    public Unit find(
            UUID propertyId,
            UUID unitId
    ) {
        getOwnedProperty(propertyId);

        return unitRepository
                .findByIdandPropertyId(
                        unitId,
                        propertyId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unit not found."
                        )
                );
    }

    public Unit update(
            UUID propertyId,
            UUID unitId,
            String unitNumber,
            Integer floor,
            Integer bedrooms,
            Integer bathrooms
    ){
        Unit unit = find(propertyId, unitId);

        String normalizedUnitNumber = unitNumber.trim();

        boolean changedUnitNumber = !unit.getUnitNumber()
                .equalsIgnoreCase(normalizedUnitNumber);

        if (
                changedUnitNumber && unitRepository
                        .existsByPropertyIdAndUnitNumberIgnoreCase(
                                propertyId,
                                normalizedUnitNumber
                        )
        ) {
            throw new IllegalArgumentException(
                    "A unit with thus number already exists for the property."
            );
        }

        unit.update(
                normalizedUnitNumber,
                floor,
                bedrooms == null ? 0 : bedrooms,
                bathrooms == null ? 0 : bathrooms
        );

        return unit;
    }

    private Property getOwnedProperty(
            UUID propertyId
    ){
        return propertyRepository
                .findByIdAndOwnerId(
                        propertyId,
                        currentUser.userId()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Property not found."
                        )
                );
    }

}
