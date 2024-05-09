package account.api.admin;

import account.api.admin.dto.UserRoleUiDto;
import account.api.employee.dto.SuccessUiDto;
import account.api.security.UserAccountService;
import account.api.security.dto.UserUiDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/api/admin/user")
public class AdminUiController {
    private final UserAccountService userAccountService;

    public AdminUiController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping(value="/")
    public ResponseEntity<List<UserUiDto>> getAllUserAccountInformation(){
        return ResponseEntity.ok(userAccountService.findAllUsers());
    }

    @PutMapping(value="/role")
    public ResponseEntity<UserUiDto> changeUserRole(@RequestBody UserRoleUiDto userRoleUiDto){
        UserUiDto userUiDto = userAccountService.setUserAccountRoles(userRoleUiDto);
        return ResponseEntity.ok(userUiDto);
    }

    @DeleteMapping(value="/{email}")
    public ResponseEntity<SuccessUiDto> deleteUserAccount(Authentication authentication, @PathVariable String email){
        userAccountService.deleteUserAccount(email, authentication);
        return ResponseEntity.ok(new SuccessUiDto("Deleted successfully!", email));
    }


}