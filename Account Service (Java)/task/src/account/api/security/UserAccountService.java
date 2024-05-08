package account.api.security;

import account.AccountServiceApplication;
import account.api.security.dto.NewPasswordUiDto;
import account.api.security.dto.PasswordUpdatedUiDto;
import account.api.security.dto.SignupUiDto;
import account.api.security.dto.UserUiDto;
import account.domain.UserAccount;
import account.domain.repositories.UserRepository;
import account.exception.NewPasswordMustBeDifferentException;
import account.exception.PasswordBreachedException;
import account.exception.UserExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
public class UserAccountService {
    private final UserRepository userRepository;

    public UserAccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserUiDto addUser(SignupUiDto signupUiDto){
        UserAccount userAccount = userRepository.findByEmailEqualsIgnoreCase(signupUiDto.getEmail()).orElse(null);
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
        userAccount.setPassword(signupUiDto.getPassword());
        UserAccount savedUserAccount = userRepository.save(userAccount);

        return new UserUiDto(savedUserAccount.getId(), savedUserAccount.getName(),
                savedUserAccount.getLastname(), savedUserAccount.getEmail());
    }

    public PasswordUpdatedUiDto updatePassword(NewPasswordUiDto newPasswordUiDto, Authentication authentication){
        UserAccount userAccount = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        if(userAccount.getPassword().equals(newPasswordUiDto.getNew_password())){
            throw new NewPasswordMustBeDifferentException();
        }

        BreachedPasswordList breachedPasswordList = getBreachedPasswordList();
        if(breachedPasswordList.getBreachedPasswords().contains(newPasswordUiDto.getNew_password())){
            throw new PasswordBreachedException();
        }

        userAccount.setPassword(newPasswordUiDto.getNew_password());
        UserAccount savedUserAccount = userRepository.save(userAccount);

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
}
