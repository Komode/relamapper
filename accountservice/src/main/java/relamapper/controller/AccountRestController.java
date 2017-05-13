package relamapper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import relamapper.exception.EmailExistsException;
import relamapper.model.*;
import relamapper.service.AccountService;
import relamapper.service.ResetTokenService;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class AccountRestController {

    /*************************
     * variables
     *************************/
    private final String uuidPattern = "^[\\d\\S]{8}-[\\d\\S]{4}-[\\d\\S]{4}-[\\d\\S]{4}-[\\d\\S]{12}$";

    /*************************
     * utilities
     *************************/
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private AccountService accountService;
    private ResetTokenService resetTokenService;

    /*************************
     * constructor
     *************************/
    @Autowired
    public AccountRestController(AccountService accountService, ResetTokenService resetTokenService) {
        this.accountService = accountService;
        this.resetTokenService = resetTokenService;
    }

    /*************************
     * convenience methods
     *************************/
    private boolean validUUID(String uuid) {
        logger.debug("validating UUID : {}", uuid);
        if(uuid.isEmpty())
            return false;
        Pattern pattern = Pattern.compile(this.uuidPattern);
        Matcher matcher = pattern.matcher(uuid);
        return matcher.find();
    }

    private boolean validType(String type) {
        return (type.equals("owner") || type.equals("administrator")
                || type.equals("moderator") || type.equals("member"));
    }

    private boolean validResetToken(String token, String uuid) {
        logger.debug("validating reset token and UUID : {} {}", token, uuid);
        if(!token.isEmpty() && !uuid.isEmpty())
            return resetTokenService.validToken(token, uuid);
        return false;
    }

    private String normalizeType(String type) {
        type = type.toLowerCase();
        if(validType(type))
            return type;
        return "member";
    }

    private Account fetchAccountUUID(String uuid) {
        Optional<Account> t = accountService.getByUUID(uuid);
        if(t.isPresent())
            return t.get();
        return null;
    }

    private Account fetchAccountEmail(String email) {
        Optional<Account> t = accountService.getByEmail(email);
        if(t.isPresent())
            return t.get();
        return null;
    }

    private AccountInfo findInfoUUID(String uuid) {
        Account a = fetchAccountUUID(uuid);
        if(a != null)
            return new AccountInfo(a);
        return null;
    }

    private AccountInfo findInfoEmail(String email) {
        Account a = fetchAccountEmail(email);
        if(a != null)
            return new AccountInfo(a);
        return null;
    }

    private AccountCredentials findCredentials(String email) {
        Account a = fetchAccountEmail(email);
        if(a != null)
            return new AccountCredentials(a);
        return null;
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private Collection<GrantedAuthority> getAuthorities() {
        return (Collection<GrantedAuthority>) getAuthentication().getAuthorities();
    }

    private boolean isCurrentUser(String uuid) {
        String current = getAuthentication().getName();
        if(uuid != null)
            return current.equals(uuid);
        return false;
    }

    private boolean isAuthorized(String minimumRequired) {
        minimumRequired = minimumRequired.toUpperCase();
        Collection<GrantedAuthority> authorities = getAuthorities();
        for(GrantedAuthority authority : authorities) {
            if(authority.equals(minimumRequired))
                return true;
        }
        return false;
    }

    /*************************
     * Mappings
     *************************/
    // default, home
    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    @ResponseBody
    public String defaultResponse() {
        return "{\"message\":\"hi\"}";
    }

    // retrieve UUID for email
    @RequestMapping(value = "/get-uuid/{email:.+}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    @ResponseBody
    public ActionResponse getAccountUUID(@PathVariable String email) {
        ActionResponse ar = new ActionResponse("get-uuid");
        ar.setStatusError();
        ar.setResultInvalidParam();

        if(!email.isEmpty()) {
            String uuid = accountService.getAccountUUID(email);
            if(uuid != null) {
                ar.setStatusSuccess();
                ar.setResult(uuid);
            } else {
                ar.setStatusFailure();
                ar.setResult("account not found");
            }
        }

        return ar;
    }

    // retrieve credentials (for authentication)
    @RequestMapping(value = "/credentials", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public AccountCredentials getCredentials(@RequestParam String email) {
        if(email.isEmpty())
            return null;
        return findCredentials(email);
    }

    // retrieve account by email or uuid (pattern matching in {needle:.+} to avoid truncating the domain in emails)
    @RequestMapping(value = "/get/{needle:.+}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    @ResponseBody
    public AccountInfo getByNeedle(@PathVariable String needle) {
        if(needle.isEmpty())
            return null;
        if(validUUID(needle))
            return findInfoUUID(needle);
        return findInfoEmail(needle);
    }

    // register account with data received over POST
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ActionResponse registerAccount(@RequestParam String email,
                                          @RequestParam String password,
                                          @RequestParam(required = false, defaultValue = "member") String type) {
        ActionResponse ar = new ActionResponse("register");
        ar.setStatusError();
        ar.setResultInvalidParam();
        if(!email.isEmpty() && !password.isEmpty() && !type.isEmpty()) {
            logger.info("registering new account with email : {}", email);
            AccountForm af = new AccountForm(email, password, type);
            try {
                Account account = accountService.register(af);
                if(account != null) {
                    ar.setStatusSuccess();
                    ar.setResult(account.getUuid());
                }
            } catch (EmailExistsException e) {
                ar.setStatusFailure();
                ar.setResult(e.getMessage());
            }
        }
        return ar;
    }

    // reset account password
    @RequestMapping(value = "/password-reset-token/{email:.+}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ActionResponse createResetToken(@PathVariable String email) {
        ActionResponse ar = new ActionResponse("password-reset-token");
        ar.setStatusError();
        ar.setResultInvalidParam();

        if(!email.isEmpty()) {
            String uuid = accountService.getAccountUUID(email);
            if(uuid != null) {
                String token = resetTokenService.createToken(uuid);
                if(token != null && !token.isEmpty()) {
                    ar.setStatusSuccess();
                    ar.setResult(token);
                }
            }
        }

        return ar;
    }

    // change password
    @RequestMapping(value = "/password-change", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ActionResponse changePassword(@RequestParam String uuid,
                                         @RequestParam String oldPassword,
                                         @RequestParam String newPassword) {
        logger.debug("changing account password for UUID : {}", uuid);
        ActionResponse ar = new ActionResponse("password-change");
        ar.setStatusError();
        ar.setResultInvalidParam();
        if(validUUID(uuid) && !oldPassword.isEmpty() && !newPassword.isEmpty()) {
            if(isCurrentUser(uuid)) {
                try {
                    accountService.changePassword(uuid, oldPassword, newPassword);
                    ar.setStatusSuccess();
                    ar.setResult("password changed");
                } catch (Exception e) {
                    ar.setStatusFailure();
                    ar.setResult(e.getMessage());
                }
            } else {
                ar.setResultUnauthorized();
            }
        }
        return ar;
    }

    // reset password
    @RequestMapping(value = "/password-reset", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ActionResponse resetPassword(@RequestParam String token,
                                        @RequestParam String email,
                                        @RequestParam String password) {
        logger.debug("resetting password for email : {} with token : {}", email, token);
        ActionResponse ar = new ActionResponse("password-reset");
        ar.setStatusError();
        ar.setResultInvalidParam();
        if(!token.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            String uuid = accountService.getAccountUUID(email);
            if(validResetToken(token, uuid)) {
                try {
                    accountService.resetPassword(uuid, password);
                    resetTokenService.tokenUsed(token);
                    ar.setStatusSuccess();
                    ar.setResult("Password reset");
                } catch (Exception e) {
                    ar.setResult(e.getMessage());
                }
            } else {
                ar.setResult("Invalid token");
            }
        }

        return ar;
    }

    // set role
    @RequestMapping(value = "/set-type", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ActionResponse setAccountType(@RequestParam String uuid,
                                         @RequestParam String type) {
        logger.info("changing type of account with UUID : {}", uuid);
        ActionResponse ar = new ActionResponse("set-role");
        ar.setStatusError();
        ar.setResultInvalidParam();
        if(validUUID(uuid) && validType(type)) {
            if(isAuthorized("ROLE_ADMINISTRATOR")) {
                type = normalizeType(type);
                Account account = fetchAccountUUID(uuid);
                if(account != null) {
                    try {
                        account.setType(type);
                        accountService.update(account);
                        ar.setStatusSuccess();
                        ar.setResult("account updated");
                    } catch (AccountNotFoundException e) {
                        ar.setStatusFailure();
                        ar.setResult(e.getMessage());
                    }
                } else {
                    ar.setStatusFailure();
                    ar.setResult("account not found");
                }
            } else {
                ar.setStatusFailure();
                ar.setResultUnauthorized();
            }
        }
        return ar;
    }

    // disable account - user
    @RequestMapping(value = "/disable/{uuid}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    @ResponseBody
    public ActionResponse disableAccount(@PathVariable String uuid) {
        ActionResponse ar = new ActionResponse("disable-account");
        ar.setStatusError();
        ar.setResultInvalidParam();

        if(validUUID(uuid) && isCurrentUser(uuid)) {
            try {
                accountService.disableAccount(uuid);
                ar.setStatusSuccess();
                ar.setResult("account disabled");
            } catch (AccountNotFoundException e) {
                ar.setStatusFailure();
                ar.setResult(e.getMessage());
            }
        } else {
            ar.setStatusFailure();
            ar.setResultUnauthorized();
        }

        return ar;
    }

    // enable account - minimum moderator
    @RequestMapping(value = "/enable/{email:.+}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    @ResponseBody
    public ActionResponse enableAccount(@PathVariable String email) {
        ActionResponse ar = new ActionResponse("enable-account");
        ar.setStatusError();
        ar.setResultInvalidParam();

        if(!email.isEmpty()) {
            if(isAuthorized("ROLE_MODERATOR")) {
                try {
                    accountService.enableAccount(email);
                    ar.setStatusSuccess();
                    ar.setResult("account enabled");
                } catch (AccountNotFoundException e) {
                    ar.setStatusFailure();
                    ar.setResultUnauthorized();
                }
            } else {
                ar.setStatusFailure();
                ar.setResultUnauthorized();
            }
        }

        return ar;
    }

    // lock account - administrator
    @RequestMapping(value = "/lock/{uuid}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    @ResponseBody
    public ActionResponse lockAccount(@PathVariable String uuid) {
        ActionResponse ar = new ActionResponse("lock-account");
        ar.setStatusError();
        ar.setResultInvalidParam();

        if(validUUID(uuid)) {
            if(isAuthorized("ROLE_ADMINISTRATOR")) {
                try {
                    accountService.lockAccount(uuid);
                    ar.setStatusSuccess();
                    ar.setResult("account locked");
                } catch (AccountNotFoundException e) {
                    ar.setStatusFailure();
                    ar.setResultUnauthorized();
                }
            } else {
                ar.setStatusFailure();
                ar.setResultUnauthorized();
            }
        }

        return ar;
    }

    // unlock account - administrator
    @RequestMapping(value = "/unlock/{email:.+}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    @ResponseBody
    public ActionResponse unlockAccount(@PathVariable String email) {
        ActionResponse ar = new ActionResponse("unlock-account");
        ar.setStatusError();
        ar.setResultInvalidParam();

        if(!email.isEmpty()) {
            if(isAuthorized("ROLE_ADMINISTRATOR")) {
                try {
                    accountService.unlockAccount(email);
                    ar.setStatusSuccess();
                    ar.setResult("account unlocked");
                } catch (AccountNotFoundException e) {
                    ar.setStatusFailure();
                    ar.setResult(e.getMessage());
                }
            } else {
                ar.setStatusFailure();
                ar.setResultUnauthorized();
            }
        }

        return ar;
    }

    // delete account - administrator
    @RequestMapping(value = "/remove/{uuid}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    @ResponseBody
    public ActionResponse removeAccount(@PathVariable String uuid) {
        ActionResponse ar = new ActionResponse("remove-account");
        ar.setStatusError();
        ar.setResultInvalidParam();

        if(validUUID(uuid)) {
            if(isAuthorized("ROLE_ADMINISTRATOR")) {
                try {
                    accountService.delete(uuid);
                    ar.setStatusSuccess();
                    ar.setResult("account removed");
                } catch (AccountNotFoundException e) {
                    ar.setStatusFailure();
                    ar.setResult(e.getMessage());
                }
            } else {
                ar.setStatusFailure();
                ar.setResultUnauthorized();
            }
        }

        return ar;
    }
}
