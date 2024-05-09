package account.api.employee;

import account.api.security.CustomUserDetails;
import account.api.security.dto.UserUiDto;
import account.domain.UserAccount;
import account.domain.repositories.UserAccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/empl")
public class EmployeeUiController {
    private final UserAccountRepository userAccountRepository;

    public EmployeeUiController(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping(value="/payment")
    public ResponseEntity<UserUiDto> getEmployeePayroll(Authentication authentication){
        /*Salary information is provided to an employee upon successful authentication
        on the corresponding endpoint, while the request specifies the period for which
        the information should be provided.
         If the period is not specified, provide all available information in the form
         of an array of JSON objects. For convenience, convert the salary to X dollar(s)
         Y cent(s).
        */
        UserAccount userAccount = userAccountRepository.findByEmailEqualsIgnoreCase(((CustomUserDetails) authentication.getPrincipal()).getUsername()).orElseThrow(() -> new IllegalStateException("User not found!"));
        return ResponseEntity.ok(new UserUiDto(userAccount.getId(), userAccount.getName(), userAccount.getLastname(), userAccount.getEmail()));
    }
}
