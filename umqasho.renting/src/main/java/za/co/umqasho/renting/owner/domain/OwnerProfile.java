package za.co.umqasho.renting.owner.domain;

import com.google.storage.v2.Owner;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import za.co.umqasho.renting.identity.AppUser;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(
        name = "owner_profile",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_owner_profile_user",
                        columnNames = "user_id"
                )
        }
)
@Getter
@Setter
public class OwnerProfile {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_owner_profile_user")
    )
    private AppUser user;

    @Column(name = "business_name", length = 225)
    private String businessName;

    @Column(name = "phone_number", length = 32)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(nullable = false)
    private long version;

    protected OwnerProfile(){

    }

    public OwnerProfile(
            AppUser user,
            String businessName,
            String phoneNumber
    ) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.businessName = businessName;
        this.phoneNumber = phoneNumber;

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        this.createdAt = now;
        this.updatedAt = now;
    }



















}
