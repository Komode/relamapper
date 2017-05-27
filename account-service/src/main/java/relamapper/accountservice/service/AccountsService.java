package relamapper.accountservice.service;

import relamapper.accountservice.model.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AccountsService {

    Account getByUUID(String uuid);

    Account getByEmail(String email);

    List<Account> listAll();

    List<Account> listEnabled();

    List<Account> listDisabled();

    List<Account> listByRole(String role);

    String getUUID(String email);

    String register(String email, String password, String role) throws Exception;

    void updatePassword(String uuid, String password) throws AccountNotFoundException;

    void updateEmail(String uuid, String email) throws AccountNotFoundException;

    void updateRole(String uuid, String role) throws AccountNotFoundException;

    void disable(String uuid) throws AccountNotFoundException;

    void enable(String email) throws AccountNotFoundException;

    void delete(String uuid) throws Exception;

}
