package relamapper.accountservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import relamapper.accountservice.model.Account;
import relamapper.accountservice.model.Message;
import relamapper.accountservice.service.AccountsService;
import relamapper.accountservice.service.ProfileService;
import relamapper.accountservice.utilities.ControllerUtils;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
public class EditController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ControllerUtils utils = new ControllerUtils();
    private final AccountsService accountsService;
    private final ProfileService profileService;

    @Autowired
    public EditController(AccountsService accountsService, ProfileService profileService) {
        this.accountsService = accountsService;
        this.profileService = profileService;
    }

    /** ROLE REQUIREMENTS ***************************************************/
    private final String REQ_SET_ROLE_MOD   = "ROLE_ADMIN";
    private final String REQ_SET_ROLE_ADM   = "ROLE_OWNER";
    private final String REQ_REG_OTHER      = "ROLE_OWNER";
    private final String REQ_DISABLE        = "ROLE_ADMIN";
    private final String REQ_ENABLE         = "ROLE_MODERATOR";
    private final String REQ_DELETE         = "ROLE_DELETE";

    /** CONVENIENCE *********************************************************/

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private List<GrantedAuthority> getAuthorities() {
        return (List<GrantedAuthority>) getAuthentication().getAuthorities();
    }

    private boolean isAuthenticated() {
        return !(getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    private boolean isAuthorized(String required) {
        List<GrantedAuthority> authorities = getAuthorities();
        for(GrantedAuthority authority : authorities) {
            if(authority.getAuthority().equals(required))
                return true;
        }
        return false;
    }

    private String requiredRoleToSetRole(String role) {
        switch (role) {
            case "owner":
            case "admin":
                return REQ_SET_ROLE_ADM;
            default:
                return REQ_SET_ROLE_MOD;
        }
    }

    private boolean isCurrent(String uuid) {
        String current = getAuthentication().getName();
        logger.debug("current : {} , requested : {}", current, uuid);
        return current.equals(uuid);
    }

    /** ROUTES **************************************************************/
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Message registerMember(@RequestParam String email, @RequestParam String password) {
        logger.debug("registering member with email : {}", email);
        Message m = utils.defaultMessage();

        if(!email.isEmpty() && !password.isEmpty()) {
            String role = "member";
            try {
                String uuid = accountsService.register(email, password, role);
                boolean profileCreated = profileService.registerProfile(uuid);
                if(profileCreated) {
                    logger.info("profile registered");
                } else {
                    logger.warn("failed to register profile");
                }
                m.setSuccess(uuid);
            } catch (Exception e) {
                m.setResult(e.getMessage());
            }
        }
        return m;
    }

    @RequestMapping(value = "/register/other", method = RequestMethod.POST)
    @ResponseBody
    public Message registerOther(@RequestParam String email, @RequestParam String password, @RequestParam String role) {
        logger.debug("registering other with email : {} and role : {}", email, role);
        Message m = utils.defaultMessage();

        role = utils.normalizeRole(role);
        if(!email.isEmpty() && !password.isEmpty()) {
            try {
                logger.debug("attempting to register account");
                String uuid = accountsService.register(email, password, role);
                boolean profileCreated = profileService.registerProfile(uuid);
                if(profileCreated) {
                    logger.info("profile registered");
                } else {
                    logger.warn("failed to register profile");
                }
                m.setSuccess(uuid);
            } catch (Exception e) {
                logger.warn("account registration failed");
                m.setResult(e.getMessage());
            }
        }
        return m;
    }

    // set account info
    @RequestMapping(value = "/set/email", method = RequestMethod.POST)
    @ResponseBody
    public Message setEmail(@RequestParam String uuid, @RequestParam String email) {
        logger.debug("set email : {} for {}", email, uuid);
        Message m = utils.defaultMessage();

        if(utils.validUUID(uuid) && !email.isEmpty()) {
            try {
                accountsService.updateEmail(uuid, email);
                m.setSuccess("email updated");
            } catch (AccountNotFoundException e) {
                m.setResult(e.getMessage());
            }
        }
        return m;
    }

    @RequestMapping(value = "/set/password", method = RequestMethod.POST)
    @ResponseBody
    public Message setPassword(@RequestParam String uuid, @RequestParam String password) {
        logger.debug("set password for : {}", uuid);
        Message m = utils.defaultMessage();

        if(utils.validUUID(uuid) && !password.isEmpty()) {
            if(isCurrent(uuid)) {
                try {
                    accountsService.updatePassword(uuid, password);
                    m.setSuccess("password updated");
                } catch (AccountNotFoundException e) {
                    m.setResult(e.getMessage());
                }
            } else {
                m.setUnauthorized();
            }
        }
        return m;
    }

    @RequestMapping(value = "/set/role", method = RequestMethod.POST)
    @ResponseBody
    public Message setRole(@RequestParam String uuid, @RequestParam String role) {
        logger.debug("set role : {} for : {}", role, uuid);
        Message m = utils.defaultMessage();

        if(utils.validUUID(uuid) && !role.isEmpty()) {
            role = utils.normalizeRole(role);
            if(isAuthorized(requiredRoleToSetRole(role))) {
                try {
                    accountsService.updateRole(uuid, role);
                    m.setSuccess("role updated");
                } catch (Exception e) {
                    m.setResult(e.getMessage());
                }
            } else {
                m.setUnauthorized();
            }
        }
        return m;
    }

    // disable/enable account
    @RequestMapping(value = "/set/disabled", method = RequestMethod.POST)
    @ResponseBody
    public Message setDisabled(@RequestParam String uuid) {
        logger.debug("disable account with UUID : {}", uuid);
        Message m = utils.defaultMessage();

        if(utils.validUUID(uuid)) {
            if(isCurrent(uuid) || isAuthorized(REQ_DISABLE)) {
                try {
                    accountsService.disable(uuid);
                    m.setSuccess("account disabled");
                } catch (AccountNotFoundException e) {
                    m.setResult(e.getMessage());
                }
            } else {
                m.setUnauthorized();
            }
        }
        return m;
    }

    @RequestMapping(value = "/set/enabled", method = RequestMethod.POST)
    @ResponseBody
    public Message setEnabled(@RequestParam String email) {
        logger.debug("enable account with email : {}", email);
        Message m = utils.defaultMessage();

        if(!email.isEmpty()) {
            if(isAuthorized(REQ_ENABLE)) {
                try {
                    accountsService.enable(email);
                    m.setSuccess("account enabled");
                } catch (AccountNotFoundException e) {
                    m.setResult(e.getMessage());
                }
            } else {
                m.setUnauthorized();
            }
        }
        return m;
    }

    // delete
    @RequestMapping(value = "/delete/{uuid}")
    @ResponseBody
    public Message deleteAccount(@PathVariable String uuid) {
        logger.debug("delete account : {}", uuid);
        Message m = utils.defaultMessage();

        if(utils.validUUID(uuid)) {
            if(isAuthorized(REQ_DELETE)) {
                try {
                    accountsService.delete(uuid);
                    m.setSuccess("account deleted");
                } catch (Exception e) {
                    m.setResult(e.getMessage());
                }
            } else {
                m.setUnauthorized();
            }
        }
        return m;
    }
}
