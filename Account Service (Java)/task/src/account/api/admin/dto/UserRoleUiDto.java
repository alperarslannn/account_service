package account.api.admin.dto;

import account.api.security.Roles;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleUiDto {
    @NotNull
    private String user;
    @NotNull
    private Roles role;
    @NotNull
    private OperationType operation;

    public enum OperationType{
        GRANT, REMOVE
    }

    public String getUser() {
        return user;
    }

    public Roles getRole() {
        return role;
    }

    public OperationType getOperation() {
        return operation;
    }
}