package relamapper.accountservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import relamapper.accountservice.model.Account;
import relamapper.accountservice.repository.AccountsRepository;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Service
public class AccountsServiceImpl implements AccountsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final PasswordEncoder passwordEncoder;
    private final AccountsRepository repository;

    @Autowired
    public AccountsServiceImpl(PasswordEncoder passwordEncoder, AccountsRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    @Override
    public Account getByUUID(String uuid) {
        logger.debug("get by UUID : {}", uuid);
        return repository.getOne(uuid);
    }

    @Override
    public Account getByEmail(String email) {
        logger.debug("get by email : {}", email);
        return repository.getAccountByEmailIgnoreCase(email);
    }

    @Override
    public List<Account> listAll() {
        logger.debug("list all accounts");
        return repository.findAll();
    }

    @Override
    public List<Account> listEnabled() {
        logger.debug("list all enabled");
        return repository.getAccountsByEnabled(true);
    }

    @Override
    public List<Account> listDisabled() {
        logger.debug("list all disabled");
        return repository.getAccountsByEnabled(false);
    }

    @Override
    public List<Account> listByRole(String role) {
        logger.debug("list all by role : {}", role);
        return repository.getAccountsByRole(role);
    }

    @Override
    public String getUUID(String email) {
        logger.debug("get UUID of email : {}", email);
        Account account = repository.getAccountByEmailIgnoreCase(email);
        return (account != null) ? account.getUuid() : null;
    }

    @Override
    public String register(String email, String password, String role) throws Exception {
        logger.debug("register account for email : {}", email);
        Account account = repository.getAccountByEmailIgnoreCase(email);
        logger.debug("[FOUND] account : {}", account);
        if(account != null) {
            logger.info("account for email address already registered");
            throw new Exception("email already registered");
        } else {
            password = passwordEncoder.encode(password);
            account = new Account(email, password, role);
            String uuid = account.getUuid();
            if(repository.exists(uuid)) {
                logger.warn("UUID collision");
                do {
                    logger.debug("regenerating UUID");
                    uuid = account.generateUUID();
                } while (repository.exists(uuid));
            }
            repository.save(account);
            return uuid;
        }
    }

    @Override
    public void updatePassword(String uuid, String password) throws AccountNotFoundException {
        logger.debug("update password for : {}", uuid);
        Account account = repository.getOne(uuid);
        if(account == null) {
            logger.warn("account not found");
            throw new AccountNotFoundException("account not found");
        } else {
            account.setPassword(passwordEncoder.encode(password));
            repository.save(account);
        }
    }

    @Override
    public void updateEmail(String uuid, String email) throws AccountNotFoundException {
        logger.debug("update email for : {}", uuid);
        Account account = repository.getOne(uuid);
        if(account == null) {
            logger.warn("account not found");
            throw new AccountNotFoundException("account not found");
        } else {
            account.setEmail(email);
            repository.save(account);
        }
    }

    @Override
    public void updateRole(String uuid, String role) throws AccountNotFoundException {
        logger.debug("update role for : {}", role);
        Account account = repository.getOne(uuid);
        if(account == null) {
            logger.warn("account not found");
            throw new AccountNotFoundException("account not found");
        } else {
            account.setRole(role);
            repository.save(account);
        }
    }

    @Override
    public void disable(String uuid) throws AccountNotFoundException {
        logger.debug("disable account : {}", uuid);
        Account account = repository.getOne(uuid);
        if(account == null) {
            logger.warn("account not found");
            throw new AccountNotFoundException("account not found");
        } else {
            account.disable();
            repository.save(account);
        }
    }

    @Override
    public void enable(String email) throws AccountNotFoundException {
        logger.debug("enable account with email : {}", email);
        Account account = repository.getAccountByEmailIgnoreCase(email);
        if(account == null) {
            logger.warn("account not found");
            throw new AccountNotFoundException("account not found");
        } else {
            account.enable();
            repository.save(account);
        }
    }

    @Override
    public void delete(String uuid) throws Exception {
        logger.debug("delete account : {}", uuid);
        Account account = repository.getOne(uuid);
        if(account == null) {
            logger.warn("account not found");
            throw new Exception("account not found");
        } else if(account.isEnabled()) {
            logger.warn("can't delete enabled accounts");
            throw new Exception("can't delete enabled accounts");
        } else {
            repository.delete(uuid);
        }
    }
}
