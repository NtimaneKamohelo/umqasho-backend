package za.co.umqasho.renting.owner.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePropertyRequest(

        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @Size(max = 255)
        String streetAddress,

        @Size(max = 150)
        String suburb,

        @NotBlank
        @Size(max = 150)
        String city,

        @NotBlank
        @Size(max = 100)
        String province,

        @Size(max = 20)
        String postalCode,

        @Size(max = 100)
        String country

) {
}
