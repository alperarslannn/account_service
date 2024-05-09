package account.api.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.Date;
public class EmployeeUiDto {

    @NotEmpty
    private String employee;
    @JsonFormat(pattern="MM-yyyy")
    private Date period;
    @Positive
    @JsonProperty(value = "salary")
    private Long salaryInCent;

    public String getEmployee() {
        return employee;
    }

    public Date getPeriod() {
        return period;
    }

    public Long getSalaryInCent() {
        return salaryInCent;
    }
}
