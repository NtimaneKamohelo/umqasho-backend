package za.co.umqasho.renting.owner.api;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.umqasho.renting.owner.service.OwnerService;

@RestController
@RequestMapping("/api/v1/owner")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(
            OwnerService ownerService
    ){
        this.ownerService = ownerService;
    }

    @GetMapping("/profile")
    public ResponseEntity<OwnerProfileResponse> getProfile(){
        return ResponseEntity.ok(
                OwnerProfileResponse.from(
                        ownerService.getProfile()
                )
        );
    }

    @PatchMapping("/profile")
    public ResponseEntity<OwnerProfileResponse> updateProfile(
            @Valid
            @RequestBody
            UpdateOwnerProfileRequest request
    ) {
        return ResponseEntity.ok(
                OwnerProfileResponse.from(
                        ownerService.updateProfile(
                                request.businessName(),
                                request.phoneNumber()
                        )
                )
        );
    }

}
