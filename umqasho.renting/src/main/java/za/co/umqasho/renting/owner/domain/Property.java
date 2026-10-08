package za.co.umqasho.renting.owner.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import za.co.umqasho.renting.identity.AppUser;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "property")
@Getter
@Setter
public class Property {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "owner_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_property_owner")
    )
    private AppUser owner;

    @Column(nullable = false, length = 255)
    private String propertyName;

    @Column(name = "Street_address", nullable = false)
    private String streetAddress;

    @Column(length = 150)
    private String suburb;

    @Column(nullable = false, length = 150)
    private String city;

    @Column(nullable = false, length = 100)
    private String province;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(nullable = false, length = 100)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PropertyStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(nullable = false)
    private long version;

    protected Property(){

    }

    public Property(
            AppUser owner,
            String propertyName,
            String streetAddress,
            String suburb,
            String city,
            String province,
            String postalCode,
            String country
    ){
        this.id = UUID.randomUUID();
        this.owner = owner;
        this.propertyName = propertyName;
        this.streetAddress = streetAddress;
        this.suburb = suburb;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
        this.status = PropertyStatus.ACTIVE;

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
    }

    @PreUpdate
    protected void onUpdate(){
            this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void update(
            String propertyName,
            String streetAddress,
            String suburb,
            String city,
            String province,
            String postalCode,
            String country

    ) {
        this.propertyName = propertyName;
        this.streetAddress = streetAddress;
        this.suburb = suburb;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
    }

    public void archive(){
        this.status = PropertyStatus.ARCHIVED;
    }










}