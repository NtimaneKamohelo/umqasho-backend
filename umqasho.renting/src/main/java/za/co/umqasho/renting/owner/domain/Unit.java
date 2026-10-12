package za.co.umqasho.renting.owner.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(
        name = "unit",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_unit_property_number",
                        columnNames = {"property_id", "unit_number"}
                )
        }
)
@Setter
@Getter
public class Unit {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "property_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_unit_property")
    )
    private Property property;

    @Column(name = "unit_number", nullable = false, length = 50)
    private String unitNumber;

    @Column
    private Integer floor;

    @Column(nullable = false)
    private Integer bedrooms;

    @Column(nullable = false)
    private Integer bathrooms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private UnitStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Getter
    @Version
    @Column(nullable = false)
    private long version;

    protected Unit() {

    }

    public Unit(
            Property property,
            String unitNumber,
            Integer floor,
            Integer bedrooms,
            Integer bathrooms
    ) {
        this.id = UUID.randomUUID();
        this.property = property;
        this.unitNumber = unitNumber;
        this.floor = floor;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.status = UnitStatus.VACANT;

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdated(){
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void update(
            String unitNumber,
            Integer floor,
            Integer bedrooms,
            Integer bathrooms
    ){
        this.unitNumber = unitNumber;
        this.floor = floor;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
    }


}
