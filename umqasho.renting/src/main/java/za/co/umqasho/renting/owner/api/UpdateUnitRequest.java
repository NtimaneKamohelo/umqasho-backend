package za.co.umqasho.renting.owner.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUnitRequest(

        @NotBlank
        @Size(max = 50)
        String unitNumber,

        Integer floor,

        @Min(0)
        Integer bedrooms,

        @Min(0)
        Integer bathrooms
) {
}
