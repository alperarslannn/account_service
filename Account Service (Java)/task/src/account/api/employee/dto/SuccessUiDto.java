package account.api.employee.dto;

public class SuccessUiDto {
    private String status;

    public SuccessUiDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
