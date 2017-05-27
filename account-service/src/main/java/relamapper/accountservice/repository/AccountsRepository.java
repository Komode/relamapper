package relamapper.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import relamapper.accountservice.model.Account;

import java.util.List;

public interface AccountsRepository extends JpaRepository<Account, String> {

    Account getAccountByEmailIgnoreCase(String email);

    List<Account> getAccountsByEnabled(boolean enabled);

    List<Account> getAccountsByRole(String role);

}
