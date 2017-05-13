package relamapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import relamapper.model.Account;

import java.util.Collection;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> getAccountByEmailIgnoreCase(String email);

    Collection<Account> queryAccountsByLockedIsTrueOrDisabledIsTrue();

}
