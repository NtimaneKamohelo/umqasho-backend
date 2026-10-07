package za.co.umqasho.renting.identity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface appUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByFirebaseUid(String firebaseUid);

    Optional<AppUser> findByEmailIgnoreCase(String email);

    boolean existsByFirebaseUid(String firebaseUid);

    boolean existsByEmailIgnoreCase(String email);

}
