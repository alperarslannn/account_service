package account.domain.repositories;

import account.domain.UserAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserAccount, Long> {

    @Override
    Optional<UserAccount> findById(Long id);

    Optional<UserAccount> findByEmailEqualsIgnoreCase(String email);
}
