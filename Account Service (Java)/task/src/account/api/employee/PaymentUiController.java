package account.api.employee;

import account.api.employee.dto.EmployeeUiDto;
import account.api.employee.dto.SuccessUiDto;
import account.api.security.CustomUserDetails;
import account.api.security.dto.UserUiDto;
import account.domain.UserAccount;
import account.domain.repositories.UserAccountRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping(value="/api/acct")
public class PaymentUiController {
    private final UserAccountRepository userAccountRepository;
    private final EmployeeService employeeService;

    public PaymentUiController(UserAccountRepository userAccountRepository, EmployeeService employeeService) {
        this.userAccountRepository = userAccountRepository;
        this.employeeService = employeeService;
    }

    @PostMapping(value="/payments")
    public ResponseEntity<SuccessUiDto> uploadPayrolls(@Valid @RequestBody List<EmployeeUiDto> employeeUiDtoList){
        /*Information about the salary of employees is transmitted as an array of
        JSON objects. This operation must be transactional.
        That is, if an error occurs during an update, perform a rollback to the original state.

        -An employee must be among the users of our service.
        -The period for which the salary is paid must be unique for each employee (for POST).
        -Salary is calculated in cents and cannot be negative.
        */
        employeeService.saveEmployeePayrolls(employeeUiDtoList);
        return ResponseEntity.ok(new SuccessUiDto("Added successfully!"));
    }

    @PutMapping(value="/payments")
    public ResponseEntity<UserUiDto> changeSalaryOfEmployee(Authentication authentication){
        /*Information about the salary of employees is transmitted as an array of
        JSON objects. This operation must be transactional.
        That is, if an error occurs during an update, perform a rollback to the original state.

        An employee must be among the users of our service.
        Salary is calculated in cents and cannot be negative.
        */
        UserAccount userAccount = userAccountRepository.findByEmailEqualsIgnoreCase(((CustomUserDetails) authentication.getPrincipal()).getUsername()).orElseThrow(() -> new IllegalStateException("User not found!"));
        return ResponseEntity.ok(new UserUiDto(userAccount.getId(), userAccount.getName(), userAccount.getLastname(), userAccount.getEmail()));
    }
}