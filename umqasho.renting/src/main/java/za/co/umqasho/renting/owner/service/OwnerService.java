package za.co.umqasho.renting.owner.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import za.co.umqasho.renting.common.security.CurrentUser;
import za.co.umqasho.renting.identity.AppUser;
import za.co.umqasho.renting.identity.AppUserRepository;
import za.co.umqasho.renting.owner.domain.OwnerProfile;
import za.co.umqasho.renting.owner.repository.OwnerProfileRepository;

import java.util.UUID;

@Service
@Transactional
public class OwnerService {

    private final CurrentUser currentUser;
    private final AppUserRepository appUserRepository;
    private final OwnerProfileRepository ownerProfileRepository;

    public OwnerService(
            CurrentUser currentUser,
            AppUserRepository appUserRepository,
            OwnerProfileRepository ownerProfileRepository
    ){
        this.currentUser = currentUser;
        this.appUserRepository = appUserRepository;
        this.ownerProfileRepository = ownerProfileRepository;
    }

    @Transactional
    public OwnerProfile getProfile(){

        UUID userId = currentUser.userId();

        return ownerProfileRepository
                .findByUserId(userId)
                .orElseGet(() ->{

                    AppUser user = appUserRepository
                            .findById(userId)
                            .orElseThrow(() ->
                                    new IllegalStateException(
                                            "Authenticated user does not exist."
                                    )
                            );

                    return ownerProfileRepository.save(
                            new OwnerProfile(
                                    user,
                                    null,
                                    user.getPhoneNumber()
                            )
                    );
                });
    }

    @Transactional
    public OwnerProfile updateProfile(
            String businessName,
            String phoneNumber
    ){
        OwnerProfile profile = getProfile();

        profile.update(
                businessName,
                phoneNumber
        );

        return profile;

    }
}
