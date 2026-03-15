package edu.hpi.api;

import edu.hpi.api.dto.CreateInstitutionRequest;
import edu.hpi.api.dto.InstitutionResponse;
import edu.hpi.application.InstitutionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    private final InstitutionService service;

    public InstitutionController(InstitutionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InstitutionResponse> create(
            @RequestBody @Valid CreateInstitutionRequest request) {

        var institution = service.create(request.name());

        return ResponseEntity.ok(
                new InstitutionResponse(
                        institution.getId(),
                        institution.getName()
                )
        );
    }
}
