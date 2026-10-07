package za.co.umqasho.renting.identity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_app_user_firebase_uid",
                        columnNames = "firebase_uid"
                ),
                @UniqueConstraint(
                        name = "uq_app_user_email",
                        columnNames = "email"
                )
        }
)
@Getter
@Setter
public class AppUser {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "firebase_uid", nullable = false, length = 128)
    private String firebaseUid;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "phone_number", length = 32)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(nullable = false)
    private long version;

    protected AppUser(){

    }

    public AppUser(
            String firebaseUid,
            String email,
            boolean emailVerified
    ){
        this.id = UUID.randomUUID();
        this.firebaseUid = firebaseUid;
        this.email = normalizeEmail(email);
        this.emailVerified = emailVerified;
        this.status = UserStatus.ACTIVE;

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void updateProfile(
            String firstName,
            String lastName,
            String phoneNumber
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public void markEmailVerified(){
        this.emailVerified = true;
    }

    private static String normalizeEmail(String email){
        return email == null
                ? null
                : email.trim().toLowerCase();
    }

}
