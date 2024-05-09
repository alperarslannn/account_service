package account.domain.repositories;

import account.domain.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Optional<Employee> findByPeriodAndUserAccountEmailEqualsIgnoreCaseOrderByPeriodDesc(Date period, String email);
    List<Employee> findByUserAccountEmailEqualsIgnoreCaseOrderByPeriodDesc(String email);
    Optional<Employee> findByUserAccountEmailEqualsIgnoreCaseAndPeriod(String email, Date period);
}
