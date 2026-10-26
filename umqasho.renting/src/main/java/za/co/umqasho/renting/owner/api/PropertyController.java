package za.co.umqasho.renting.owner.api;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.umqasho.renting.owner.service.PropertyService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/owner/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public ResponseEntity<PropertyResponse> create(
            @Valid
            @RequestBody
            CreatePropertyRequest request
    ){
        var property = propertyService.create(
                request.propertyName(),
                request.streetAddress(),
                request.suburb(),
                request.city(),
                request.province(),
                request.province(),
                request.postalCode(),
                request.country()

        );

        return ResponseEntity.created(
                URI.create(
                        "/api/v1/owner/properties/" + property.getId()
                )
        ).body(
                PropertyResponse.from(property)
        );
    }

    @GetMapping
    public ResponseEntity<List<PropertyResponse>> list() {

        return ResponseEntity.ok(
                propertyService.findAllForCurrentOwner()
                        .stream()
                        .map(PropertyResponse::from)
                        .toList()
        );

    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyResponse> get(
            @PathVariable UUID propertyId
    ){
        return ResponseEntity.ok(
                PropertyResponse.from(
                        propertyService.findForCurrentOwner(
                                propertyId
                        )
                )
        );
    }

    @PatchMapping("/{propertyId}")
    public ResponseEntity<PropertyResponse> update(
            @PathVariable UUID propertyId,

            @Valid@RequestBody
            UpdatePropertyRequest request
    ){
        return ResponseEntity.ok(
                PropertyResponse.from(
                        propertyService.update(
                                propertyId,
                                request.propertyName(),
                                request.streetAddress(),
                                request.suburb(),
                                request.city(),
                                request.province(),
                                request.postalCode(),
                                request.country()
                        )
                )
        );
    }
}
