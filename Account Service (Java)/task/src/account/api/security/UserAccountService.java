package account.api.security;

import account.AccountServiceApplication;
import account.api.admin.dto.UserRoleUiDto;
import account.api.security.dto.NewPasswordUiDto;
import account.api.security.dto.PasswordUpdatedUiDto;
import account.api.security.dto.SignupUiDto;
import account.api.security.dto.UserUiDto;
import account.domain.Group;
import account.domain.UserAccount;
import account.domain.repositories.GroupRepository;
import account.domain.repositories.UserAccountRepository;
import account.exception.AdminCannotDeleteThemselfOrAdminRoleCannotBeRemovedException;
import account.exception.NewPasswordMustBeDifferentException;
import account.exception.PasswordBreachedException;
import account.exception.RoleNotFoundException;
import account.exception.UserDoesNotHaveRoleException;
import account.exception.UserExistsException;
import account.exception.UserHasOnlyOneRoleException;
import account.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final GroupRepository groupRepository;
    private final CustomBCryptPasswordEncoder encoder;


    public UserAccountService(UserAccountRepository userAccountRepository, GroupRepository groupRepository, CustomBCryptPasswordEncoder encoder) {
        this.userAccountRepository = userAccountRepository;
        this.groupRepository = groupRepository;
        this.encoder = encoder;
    }

    public List<UserUiDto> findAllUsers(){
        List<UserUiDto> userUiDtoList = new ArrayList<>();
        userAccountRepository.findAllByOrderByIdAsc().forEach(userAccount -> {
            UserUiDto userUiDto = new UserUiDto(userAccount.getId(), userAccount.getName(), userAccount.getLastname(), userAccount.getEmail(), userAccount.getRolesAsString());
            userUiDtoList.add(userUiDto);
        });
        return userUiDtoList;
    }


    public UserUiDto addUser(SignupUiDto signupUiDto){

        String salt = BCrypt.gensalt();
        String hashedPassword = hashPassword(signupUiDto.getPassword(), salt);

        UserAccount userAccount = userAccountRepository.findByEmailEqualsIgnoreCase(signupUiDto.getEmail()).orElse(null);
        if(Objects.nonNull(userAccount)){
            throw new UserExistsException();
        }

        BreachedPasswordList breachedPasswordList = getBreachedPasswordList();
        if(breachedPasswordList.getBreachedPasswords().contains(signupUiDto.getPassword())){
            throw new PasswordBreachedException();
        }

        userAccount = new UserAccount();
        userAccount.setEmail(signupUiDto.getEmail().toLowerCase());
        userAccount.setName(signupUiDto.getName());
        userAccount.setLastname(signupUiDto.getLastname());
        userAccount.setPassword(hashedPassword);
        userAccount.setSalt(salt);

        List<Group> roles = new ArrayList<>();
        if (userAccountRepository.findFirstByRoles(groupRepository.findByName(Roles.ROLE_ADMINISTRATOR.toString())).isPresent()) {
            roles.add(groupRepository.findByName(Roles.ROLE_USER.toString()));
        } else {
            roles.add(groupRepository.findByName(Roles.ROLE_ADMINISTRATOR.toString()));
        }
        userAccount.setRoles(roles);
        UserAccount savedUserAccount = userAccountRepository.save(userAccount);

        return new UserUiDto(savedUserAccount.getId(), savedUserAccount.getName(),
                savedUserAccount.getLastname(), savedUserAccount.getEmail(), savedUserAccount.getRolesAsString());
    }

    public PasswordUpdatedUiDto updatePassword(NewPasswordUiDto newPasswordUiDto, Authentication authentication){
        UserAccount userAccount = userAccountRepository.findByEmailEqualsIgnoreCase(((CustomUserDetails) authentication.getPrincipal()).getUsername()).orElseThrow(() -> new IllegalStateException("User not found!"));
        if(checkNewPasswordIsTheSame(newPasswordUiDto.getNew_password(), userAccount.getPassword())){
            throw new NewPasswordMustBeDifferentException();
        }

        BreachedPasswordList breachedPasswordList = getBreachedPasswordList();
        if(breachedPasswordList.getBreachedPasswords().contains(newPasswordUiDto.getNew_password())){
            throw new PasswordBreachedException();
        }

        String salt = BCrypt.gensalt();
        String hashedPassword = hashPassword(newPasswordUiDto.getNew_password(), salt);

        userAccount.setPassword(hashedPassword);
        userAccount.setSalt(salt);
        UserAccount savedUserAccount = userAccountRepository.save(userAccount);

        return new PasswordUpdatedUiDto(savedUserAccount.getEmail());
    }

    private static BreachedPasswordList getBreachedPasswordList() {
        String jsonFilePath = "other/breached-passwords.json";

        BreachedPasswordList dataObject;
        try {
            InputStream inputStream = AccountServiceApplication.class.getClassLoader().getResourceAsStream(jsonFilePath);
            if (inputStream != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                dataObject = objectMapper.readValue(inputStream, BreachedPasswordList.class);
            } else {
                throw new IllegalStateException("BreachedPasswordList cannot be found!, inputStream is null");
            }
        } catch (IOException e) {
            throw new IllegalStateException("BreachedPasswordList cannot be found!, e:", e);
        }
        return dataObject;
    }

    private String hashPassword(String password, String salt) {
        return encoder.encode(password + salt);
    }

    private boolean checkNewPasswordIsTheSame(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }

    public UserAccount findByUsername(String username) {
        return userAccountRepository.findByEmailEqualsIgnoreCase(username).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void deleteUserAccount(String email, Authentication authentication) {
        findByUsername(email);
        if(((CustomUserDetails) authentication.getPrincipal()).getUsername().equals(email)){
            throw new AdminCannotDeleteThemselfOrAdminRoleCannotBeRemovedException();
        }
        userAccountRepository.deleteUserAccountByEmail(email);
    }

    @Transactional
    public UserUiDto setUserAccountRoles(UserRoleUiDto userRoleUiDto) {
        UserAccount userAccount = userAccountRepository.findByEmailEqualsIgnoreCase(userRoleUiDto.getUser()).orElseThrow(UserNotFoundException::new);
        Group requiredRole = groupRepository.findByName(userRoleUiDto.getRole().toString());
        Group administratorRole = groupRepository.findByName(Roles.ROLE_ADMINISTRATOR.name());
        Group userRole = groupRepository.findByName(Roles.ROLE_USER.name());
        Group accountantRole = groupRepository.findByName(Roles.ROLE_ACCOUNTANT.name());

        //todo fix business exceptions
        if(Objects.isNull(requiredRole)) throw new RoleNotFoundException();
        if (!userAccount.getRoles().contains(requiredRole)){
            throw new UserDoesNotHaveRoleException();
        }
        if (userAccount.getRoles().size() == 1 && userRoleUiDto.getOperation().equals(UserRoleUiDto.OperationType.REMOVE)){
            throw new UserHasOnlyOneRoleException();
        }
        if (userRoleUiDto.getRole().equals(Roles.ROLE_ADMINISTRATOR) && userRoleUiDto.getOperation().equals(UserRoleUiDto.OperationType.REMOVE)){
            throw new AdminCannotDeleteThemselfOrAdminRoleCannotBeRemovedException();
        }
        if(userRoleUiDto.getOperation().equals(UserRoleUiDto.OperationType.GRANT)){
            if ((userRoleUiDto.getRole().equals(Roles.ROLE_ADMINISTRATOR) && (userAccount.getRoles().contains(userRole) || userAccount.getRoles().contains(accountantRole)))
                    || ((userRoleUiDto.getRole().equals(Roles.ROLE_USER) || userRoleUiDto.getRole().equals(Roles.ROLE_ACCOUNTANT)) && userAccount.getRoles().contains(administratorRole))){
                throw new AdminCannotDeleteThemselfOrAdminRoleCannotBeRemovedException();
            }
        }

        if (userRoleUiDto.getOperation().equals(UserRoleUiDto.OperationType.GRANT)){
            userAccount.addRole(requiredRole);
        } else {
            userAccount.removeRole(requiredRole);
        }
        userAccountRepository.save(userAccount);
        return new UserUiDto(userAccount.getId(), userAccount.getName(), userAccount.getLastname(), userAccount.getEmail(), userAccount.getRolesAsString());
    }
}
