package account.api.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLockUiDto {
    @NotNull
    private String user;
    @NotNull
    private OperationType operation;

    public enum OperationType{
        LOCK, UNLOCK
    }

    public String getUser() {
        return user;
    }

    public OperationType getOperation() {
        return operation;
    }

    public String lockingString() {
        if(operation.equals(OperationType.LOCK)){
            return "locked";
        }else{
            return "unlocked";
        }
    }
}
