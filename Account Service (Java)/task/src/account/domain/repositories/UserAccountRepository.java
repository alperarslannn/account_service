package account.domain.repositories;

import account.domain.UserAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository extends CrudRepository<UserAccount, UUID> {

    @Override
    Optional<UserAccount> findById(UUID id);

    Optional<UserAccount> findByEmailEqualsIgnoreCase(String email);
}
