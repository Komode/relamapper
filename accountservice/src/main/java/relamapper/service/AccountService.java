package relamapper.service;

import relamapper.exception.EmailExistsException;
import relamapper.model.Account;
import relamapper.model.AccountForm;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Collection;
import java.util.Optional;

public interface AccountService {

    Optional<Account> getByUUID(String uuid);

    Optional<Account> getByEmail(String email);

    Collection<Account> getDisabledAndLocked();

    String getAccountUUID(String email);

    Account register(AccountForm accountForm) throws EmailExistsException;

    void changePassword(String uuid, String oldPassword, String newPassword) throws Exception;

    void resetPassword(String uuid, String password) throws AccountNotFoundException;

    void update(Account account) throws AccountNotFoundException;

    void delete(String uuid) throws AccountNotFoundException;

    void disableAccount(String uuid) throws AccountNotFoundException;

    void enableAccount(String email) throws AccountNotFoundException;

    void lockAccount(String uuid) throws AccountNotFoundException;

    void unlockAccount(String email) throws AccountNotFoundException;

}
