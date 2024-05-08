package account.api.employee;

import account.api.security.CustomUserDetails;
import account.api.security.dto.UserUiDto;
import account.domain.UserAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/empl")
public class PaymentUiController {
    @GetMapping(value="/payment")
    public ResponseEntity<UserUiDto> signup(Authentication authentication){
        UserAccount userAccount = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        return ResponseEntity.ok(new UserUiDto(userAccount.getId(), userAccount.getName(), userAccount.getLastname(), userAccount.getEmail()));
    }
}
