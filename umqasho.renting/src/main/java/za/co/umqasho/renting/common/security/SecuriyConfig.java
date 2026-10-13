package za.co.umqasho.renting.common.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.Security;

@Configuration
@EnableMethodSecurity
public class SecuriyConfig {

    private final FirebaseAuthenticationFilter firebaseAuthenticationFilter;

    public SecuriyConfig(
            FirebaseAuthenticationFilter firebaseAuthenticationFilter
    ){
        this.firebaseAuthenticationFilter = firebaseAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception{

        /**
         * The API is stateless and authenticated with
         * bearer tokens rather than browser sessions.
         * */
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/actuator/health",
                                "actuator/health/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/v1/owner/**"
                        ).hasRole("owner")

                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(
                        firebaseAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

}
