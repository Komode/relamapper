package relamapper.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import relamapper.exception.EmailExistsException;
import relamapper.exception.PasswordMismatchException;
import relamapper.model.Account;
import relamapper.model.AccountForm;
import relamapper.repository.AccountRepository;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Account> getByUUID(String uuid) {
        logger.debug("retrieving account with UUID : {}", uuid);
        return Optional.ofNullable(repository.getOne(uuid));
    }

    @Override
    public Optional<Account> getByEmail(String email) {
        logger.debug("retrieving account with email : {}", email);
        return repository.getAccountByEmailIgnoreCase(email);
    }

    @Override
    public Collection<Account> getDisabledAndLocked() {
        logger.debug("retrieving locked and disabled accounts");
        return repository.queryAccountsByLockedIsTrueOrDisabledIsTrue();
    }

    @Override
    public String getAccountUUID(String email) {
        Optional<Account> t = repository.getAccountByEmailIgnoreCase(email);
        if(t.isPresent()) {
            Account a = t.get();
            return a.getUuid();
        }
        return null;
    }

    @Override
    public Account register(AccountForm accountForm) throws EmailExistsException {
        logger.info("registering new account, with email : {}", accountForm.getEmail());
        Optional<Account> t = repository.getAccountByEmailIgnoreCase(accountForm.getEmail());
        if(t.isPresent())
            throw new EmailExistsException("Email is already registered");
        accountForm.setPassword(passwordEncoder.encode(accountForm.getPassword()));
        Account account = new Account(accountForm);
        if(repository.exists(account.getUuid())) {
            String uuid = account.getUuid();
            do {
                logger.debug("UUID {} exists, regenerating", uuid);
                uuid = account.regenUUID();
            } while (repository.exists(uuid));
        }
        logger.debug("saving new account");
        return repository.save(account);
    }

    @Override
    public void changePassword(String uuid, String oldPassword, String newPassword) throws Exception {
        Account account = repository.findOne(uuid);
        if(account == null)
            throw new AccountNotFoundException("The account UUID is not registered");
        if(!passwordEncoder.matches(oldPassword, account.getPassword()))
            throw new PasswordMismatchException("Password mismatch");
        account.setPassword(passwordEncoder.encode(newPassword));
        repository.save(account);
    }

    @Override
    public void resetPassword(String uuid, String password) throws AccountNotFoundException {
        Account account = repository.findOne(uuid);
        if(account == null)
            throw new AccountNotFoundException("The email is not registered");
        account.setPassword(passwordEncoder.encode(password));
        repository.save(account);
    }

    @Override
    public void update(Account account) throws AccountNotFoundException {
        Optional<Account> t = Optional.ofNullable(repository.findOne(account.getUuid()));
        if(!t.isPresent())
            throw new AccountNotFoundException("The account UUID is not registered");
        Account a = t.get();
        a.setEmail(account.getEmail());
        a.setType(account.getType());
        a.setDisabled(account.isDisabled());
        a.setLocked(account.isLocked());
        a.setDisabledDate(account.getDisabledDate());
        repository.save(a);
    }

    @Override
    public void delete(String uuid) throws AccountNotFoundException {
        if(!repository.exists(uuid))
            throw new AccountNotFoundException("The account UUID is not registered");
        repository.delete(uuid);
    }

    @Override
    public void disableAccount(String uuid) throws AccountNotFoundException {
        Optional<Account> t = Optional.ofNullable(repository.getOne(uuid));
        if(!t.isPresent())
            throw new AccountNotFoundException("The account UUID is not registered");
        Account a = t.get();
        a.disable();
        repository.save(a);
    }

    @Override
    public void enableAccount(String email) throws AccountNotFoundException {
        Optional<Account> t = repository.getAccountByEmailIgnoreCase(email);
        if(!t.isPresent())
            throw new AccountNotFoundException("The account UUID is not registered");
        Account a = t.get();
        a.enable();
        repository.save(a);
    }

    @Override
    public void lockAccount(String uuid) throws AccountNotFoundException {
        Optional<Account> t = Optional.ofNullable(repository.getOne(uuid));
        if(!t.isPresent())
            throw new AccountNotFoundException("The account UUID is not registered");
        Account a = t.get();
        a.lock();
        repository.save(a);
    }

    @Override
    public void unlockAccount(String email) throws AccountNotFoundException {
        Optional<Account> t = repository.getAccountByEmailIgnoreCase(email);
        if(!t.isPresent())
            throw new AccountNotFoundException("The account UUID is not registered");
        Account a = t.get();
        a.unlock();
        repository.save(a);
    }
}
