package account.api.security;

import account.api.security.dto.NewPasswordUiDto;
import account.api.security.dto.PasswordUpdatedUiDto;
import account.api.security.dto.SignupUiDto;
import account.api.security.dto.UserUiDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/auth")
public class AuthUiController {
    private final UserAccountService userAccountService;

    public AuthUiController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping(value="/signup")
    public ResponseEntity<UserUiDto> signup(@Validated @RequestBody SignupUiDto signupUiDto){
        return ResponseEntity.ok(userAccountService.addUser(signupUiDto));
    }

    @PostMapping(value="/changepass")
    public ResponseEntity<PasswordUpdatedUiDto> changePassword(@Validated @RequestBody NewPasswordUiDto newPasswordUiDto, Authentication authentication){
        return ResponseEntity.ok(userAccountService.updatePassword(newPasswordUiDto, authentication));
    }
}
