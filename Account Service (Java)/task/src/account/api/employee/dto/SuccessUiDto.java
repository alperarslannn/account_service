package account.api.employee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessUiDto {
    private String status;
    private String user;

    public SuccessUiDto(String status) {
        this.status = status;
    }

    public SuccessUiDto(String status, String email) {
        this.status = status;
        this.user = email;
    }

    public String getStatus() {
        return status;
    }

    public String getUser() {
        return user;
    }
}
