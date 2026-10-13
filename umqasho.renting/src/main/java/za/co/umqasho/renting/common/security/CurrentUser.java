package za.co.umqasho.renting.common.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUser {

    public UUID userId(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null ||
                !(authentication.getDetails()
                instanceof UmqashoAuthenticationDetails details)
        ){
            throw new IllegalStateException(
                    "Authenticated Umqasho user is unavailable."
            );
        }
        return details.userId();
    }

    public String email(){

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (
                authentication == null ||
                        !(authentication.getDetails()
                        instanceof UmqashoAuthenticationDetails details)
        ){
            throw new IllegalStateException(
                    "Authenticated Umqasho user is unavailable."
            );
        }
        return details.email();

    }

}
