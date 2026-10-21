package za.co.umqasho.renting.owner.api;

import za.co.umqasho.renting.owner.domain.Property;
import za.co.umqasho.renting.owner.domain.PropertyStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PropertyResponse(

        UUID id,
        UUID ownerId,
        String name,
        String streetAddress,
        String suburb,
        String city,
        String province,
        String postalCode,
        String country,
        PropertyStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt


) {

    public static PropertyResponse from(
            Property property
    ){
        return new PropertyResponse(
                property.getId(),
                property.getOwner().getId(),
                property.getPropertyName(),
                property.getStreetAddress(),
                property.getSuburb(),
                property.getCity(),
                property.getProvince(),
                property.getPostalCode(),
                property.getCountry(),
                property.getStatus(),
                property.getCreatedAt(),
                property.getUpdatedAt()
        );
    }

}
