package za.co.umqasho.renting.owner.api;

import za.co.umqasho.renting.owner.domain.Unit;
import za.co.umqasho.renting.owner.domain.UnitStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UnitResponse(

        UUID id,
        UUID propertyId,
        String unitNumber,
        Integer floor,
        Integer bedrooms,
        Integer bathrooms,
        UnitStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt

) {
    public static UnitResponse from(
            Unit unit
    ) {

        return new UnitResponse(
                unit.getId(),
                unit.getProperty().getId(),
                unit.getUnitNumber(),
                unit.getFloor(),
                unit.getBedrooms(),
                unit.getBathrooms(),
                unit.getStatus(),
                unit.getCreatedAt(),
                unit.getUpdatedAt()
        );
    }
}
