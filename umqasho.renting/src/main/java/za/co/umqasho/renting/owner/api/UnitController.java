package za.co.umqasho.renting.owner.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.umqasho.renting.owner.service.UnitService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/owner/properties/{propertyId}/units")
public class UnitController {

    private final UnitService unitService;

    public UnitController(
            UnitService unitService
    ) {
        this.unitService = unitService;
    }

    @PostMapping
    public ResponseEntity<UnitResponse> create(
            @PathVariable UUID propertyId,

            @Valid
            @RequestBody
            CreateUnitRequest request

    ){
        var unit =
                unitService.create(
                        propertyId,
                        request.unitNumber(),
                        request.floor(),
                        request.bedrooms(),
                        request.bathrooms()
                );

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/v1/owner/properties/"
                                + propertyId
                                + "/units/"
                                + unit.getId()
                        )
                )
                .body(
                        UnitResponse.from(unit)
                );
    }

    @GetMapping
    public ResponseEntity<List<UnitResponse>> list(
            @PathVariable UUID propertyId
    ) {
        return ResponseEntity.ok(
                unitService
                        .findAll(propertyId)
                        .stream()
                        .map(UnitResponse::from)
                        .toList()
        );
    }

    @GetMapping("/{unitId}")
    public ResponseEntity<UnitResponse> get(
            @PathVariable UUID propertyId,
            @PathVariable UUID unitId
    ) {
        return ResponseEntity.ok(
                UnitResponse.from(
                        unitService.find(
                                propertyId,
                                unitId
                        )
                )
        );
    }

    @PatchMapping("/{unitId}")
    public ResponseEntity<UnitResponse> update(
            @PathVariable UUID propertyId,
            @PathVariable UUID unitId,

            @Valid
            @RequestBody
            UpdateUnitRequest request
    ){
        return ResponseEntity.ok(
                UnitResponse.from(
                        unitService.update(
                                propertyId,
                                unitId,
                                request.unitNumber(),
                                request.floor(),
                                request.bedrooms(),
                                request.bathrooms()
                        )
                )
        );
    }

}
