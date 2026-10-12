package za.co.umqasho.renting.common.security;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import za.co.umqasho.renting.identity.AppUser;
import za.co.umqasho.renting.identity.AppUserRepository;
import za.co.umqasho.renting.identity.UserStatus;

import java.io.IOException;
import java.util.List;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final FirebaseAuth firebaseAuth;
    private final AppUserRepository appUserReposirory;

    public FirebaseAuthenticationFilter(
            FirebaseAuth firebaseAuth,
            AppUserRepository appUserReposirory

    ) {
        this.firebaseAuth = firebaseAuth;
        this.appUserReposirory = appUserReposirory;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException{
        String authorization = request.getHeader("Authorization");

        if(
                authorization == null || !authorization.startsWith("Bearer")
        ){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7).trim();

        if (token.isBlank()){
            SecurityContextHolder.clearContext();
            SecurityErrorWriter.writeUnauthorized(
                    response,
                    "INVALID_BEARER_TOKEN",
                    "Bearer token is empty"
            );
        }

        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token, true);

            if (!decodedToken.isEmailVerified()){
                SecurityContextHolder.clearContext();

                SecurityErrorWriter.writeForbidden(
                        response,
                        "EMAIL_NOT_VERIFIED",
                        "A verified email address is required."
                );
                return;
            }

            String firebaseUid = decodedToken.getUid();

            AppUser appUser = appUserReposirory
                    .findByFirebaseUid(firebaseUid)
                    .orElse(null);

            if(appUser == null){

                SecurityContextHolder.clearContext();

                SecurityErrorWriter.writeForbidden(
                        response,
                        "USER_NOT_PROVISIONED",
                        "This Firebase account has not been provisioned in Umqasho."
                );
                return;
            }

            if (appUser.getStatus() != UserStatus.ACTIVE){

                SecurityContextHolder.clearContext();

                SecurityErrorWriter.writeForbidden(
                        response,
                        "USER_INACTIVE",
                        "This Umqasho account is not active."
                );

                return;

            }

            /**
             * Role Assignment comes from the umqasho database.
             *
             * The flutter application cannot submit:
             *
             * role = OWNER
             *
             * and elevate itself.
             *
             */
            List<SimpleGrantedAuthority> authorities =
                    List.of(
                            new SimpleGrantedAuthority("ROLE_OWNER")
                    );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            firebaseUid,
                            null,
                            authorities
                    );

            authentication.setDetails(
                    new UmqashoAuthenticationDetails(
                            appUser.getId(),
                            appUser.getEmail(),
                            appUser.getFirebaseUid()
                    )
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            filterChain.doFilter(request,response);
        } catch (FirebaseAuthException ex) {
            SecurityContextHolder.clearContext();

            SecurityErrorWriter.writeUnauthorized(
                    response,
                    "INVALID_FIREBASE_TOKEN",
                    "The Firebase authentication token is invalid or expired."
            );
        }

    }

}
