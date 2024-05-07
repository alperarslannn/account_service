package account.api.security;

public class PasswordUpdatedUiDto {
    private final String email;
    private final String status;

    public PasswordUpdatedUiDto(String email, String status) {
        this.email = email;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}
