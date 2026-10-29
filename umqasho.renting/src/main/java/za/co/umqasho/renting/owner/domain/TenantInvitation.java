package za.co.umqasho.renting.owner.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static za.co.umqasho.renting.identity.AppUser.normalizeEmail;

@Setter
@Getter
@Entity
@Table(name = "tenant_invitation")
public class TenantInvitation {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "property_id",
            nullable = false
    )
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "unit_id",
            nullable = false
    )
    private Unit unit;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(name = "phone_number", length = 32)
    private String phoneNumber;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(
            name = "lease_start_date",
            nullable = false
    )
    private LocalDate leaseStartDate;

    @Column(
            name = "lease_end_date",
            nullable = false
    )
    private LocalDate leaseEndDate;

    @Column(
            name = "monthly_rent",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal monthlyRent;

    @Column(
            name = "deposit_amount",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal depositAmount;

    @Column(name = "due_day", nullable = false)
    private int dueDay;

    @Column(
            name = "token_hash",
            nullable = false,
            unique = true,
            length = 128
    )
    private String tokenHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TenantInvitationStatus status;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "accepted_at")
    private OffsetDateTime acceptedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private za.co.umqasho.renting.identity.AppUser createdBy;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(nullable = false)
    private long version;

    protected TenantInvitation(){

    }

    public TenantInvitation(
            Property property,
            Unit unit,
            String email,
            String phoneNumber,
            String firstName,
            String lastName,
            LocalDate leaseStartDate,
            LocalDate leaseEndDate,
            BigDecimal monthlyRent,
            BigDecimal depositAmount,
            int dueDay,
            String tokenHash,
            OffsetDateTime expiresAt,
            za.co.umqasho.renting.identity.AppUser createdBy
    ){
        this.id = UUID.randomUUID();

        this.property = property;
        this.unit = unit;

        this.email = normalizeEmail(email);
        this.phoneNumber = phoneNumber;

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();

        this.leaseStartDate = leaseStartDate;
        this.leaseEndDate = leaseEndDate;

        this.monthlyRent = monthlyRent;
        this.depositAmount = depositAmount;
        this.dueDay = dueDay;

        this.tokenHash = tokenHash;

        this.status =
                TenantInvitationStatus.PENDING;

        this.expiresAt = expiresAt;

        this.createdBy = createdBy;

        OffsetDateTime now =
                OffsetDateTime.now(ZoneOffset.UTC);

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void markExpired(){
        if (
                status == TenantInvitationStatus.PENDING
        ){
            status = TenantInvitationStatus.EXPIRED;
        }
    }

    private static String normalizeEmail(String email){
        return email
                .trim()
                .toLowerCase();
    }






















}
