package za.co.umqasho.renting.owner.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Size;
import za.co.umqasho.renting.common.security.CurrentUser;
import za.co.umqasho.renting.identity.AppUser;
import za.co.umqasho.renting.identity.AppUserRepository;
import za.co.umqasho.renting.owner.domain.Property;
import za.co.umqasho.renting.owner.repository.PropertyRepository;

import java.util.List;
import java.util.UUID;

//import static sun.security.krb5.internal.ktab.KeyTab.normalize;

public class PropertyService {

    private final CurrentUser currentUser;
    private final AppUserRepository appUserRepository;
    private final PropertyRepository propertyRepository;

    public PropertyService(
            CurrentUser currentUser,
            AppUserRepository appUserRepository,
            PropertyRepository propertyRepository
    ){
        this.currentUser = currentUser;
        this.appUserRepository = appUserRepository;
        this.propertyRepository = propertyRepository;
    }

    public Property create(
            String propertyName,
            String streetAddress,
            String suburb,
            String city,
            String province,
            String postalCode,
            String country,
            @Size(max = 100) String countried) {
        UUID ownerId = currentUser.userId();

        AppUser owner =
                appUserRepository
                        .findById(ownerId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Owner account was not found."
                                )
                        );

        return propertyRepository.save(
                new Property(
                        owner,
                        propertyName.trim(),
                        streetAddress.trim(),
                        normalize(suburb),
                        city.trim(),
                        province.trim(),
                        normalize(postalCode),
                        normalizeOrDefault(
                                country,
                                "South Africa"
                        )

                )
        );
    }

    @Transactional
    public List<Property> findAllForCurrentOwner(){

        return propertyRepository
                .findAllByOwnerIdOrderByCreatedAtDesc(
                        currentUser.userId()
                );

    }

    @Transactional
    public Property findForCurrentOwner(
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

    public Property update(
            UUID propertyId,
            String propertyName,
            String streetAddress,
            String suburb,
            String city,
            String province,
            String postalCode,
            String country

    ){
        Property property = findForCurrentOwner(propertyId);

        property.update(
                propertyName.trim(),
                streetAddress.trim(),
                normalize(suburb),
                city.trim(),
                province.trim(),
                normalize(postalCode),
                normalizeOrDefault(
                        country,
                        "South Africa"
                )
        );

        return property;
    }

    private String normalize(String value){

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();

    }

    private String normalizeOrDefault(
            String value,
            String defaultValue
    ){
        String normalized = normalize(value);

        return normalized == null
                ? defaultValue
                : normalized;
    }

}
