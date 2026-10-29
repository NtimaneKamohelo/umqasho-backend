package za.co.umqasho.renting.owner.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class InvitationTokenService {

    private static final int TOKEN_BYTES = 32;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateToken() {

        byte[] tokenBytes = new byte[TOKEN_BYTES];

        secureRandom.nextBytes(tokenBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(tokenBytes);
    }

    public String hash(String token){

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(
                    "SHA-256 is not available.",
                    ex
            );
        }

    }

    private String bytesToHex(
            byte[] bytes
    ){
        StringBuilder result = new StringBuilder(bytes.length * 2);

        for (byte value : bytes){
            result.append(
                    String.format(
                            "%02x",
                            value
                    )
            );
        }

        return result.toString();
    }


}
