package account.api.employee;

import account.api.employee.dto.EmployeeUiDto;
import account.domain.Employee;
import account.domain.UserAccount;
import account.domain.repositories.EmployeeRepository;
import account.domain.repositories.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class EmployeeService {
    private final UserAccountRepository userAccountRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeService(UserAccountRepository userAccountRepository, EmployeeRepository employeeRepository) {
        this.userAccountRepository = userAccountRepository;
        this.employeeRepository = employeeRepository;
    }

    public void saveEmployeePayrolls(List<EmployeeUiDto> employeeUiDtoList){
        List<Employee> employeeList = new ArrayList<>();
        employeeUiDtoList.forEach(employeeUiDto -> {
            //todo handle exception
            UserAccount userAccount = userAccountRepository.findByEmailEqualsIgnoreCase(employeeUiDto.getEmployee()).orElseThrow();
            Employee employee = new Employee();
            employee.setUserAccount(userAccount);
            employee.setPeriod(employeeUiDto.getPeriod().toString());
            employee.setSalary(employeeUiDto.getSalaryInCent());
            employeeList.add(employee);
        });

        employeeRepository.saveAll(employeeList);
    }
}
