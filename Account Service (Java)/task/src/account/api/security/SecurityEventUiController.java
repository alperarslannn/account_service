package account.api.security;

import account.api.security.dto.SecurityEventUiDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/api/security")
public class SecurityEventUiController {
    private final UserAccountService userAccountService;
    private final SecurityEventService securityEventService;

    public SecurityEventUiController(UserAccountService userAccountService, SecurityEventService securityEventService) {
        this.userAccountService = userAccountService;
        this.securityEventService = securityEventService;
    }

    @GetMapping(value="/events")
    public ResponseEntity<List<SecurityEventUiDto>> getSecurityEvents(){
        return ResponseEntity.ok(securityEventService.findAllSecurityEvents());
    }
}