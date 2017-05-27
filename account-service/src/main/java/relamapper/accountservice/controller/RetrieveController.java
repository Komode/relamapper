package relamapper.accountservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import relamapper.accountservice.model.Account;
import relamapper.accountservice.model.Credential;
import relamapper.accountservice.model.Information;
import relamapper.accountservice.model.Message;
import relamapper.accountservice.service.AccountsService;
import relamapper.accountservice.service.ProfileService;
import relamapper.accountservice.utilities.ControllerUtils;

import java.util.List;

@RestController
public class RetrieveController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ControllerUtils utils = new ControllerUtils();
    private final AccountsService accountsService;
    private final ProfileService profileService;

    @Autowired
    public RetrieveController(AccountsService accountsService, ProfileService profileService) {
        this.accountsService = accountsService;
        this.profileService = profileService;
    }

    /** ROUTES **************************************************************/
    @RequestMapping(value = "/credentials", method = RequestMethod.POST)
    @ResponseBody
    public Credential getCredential(@RequestParam String email) {
        logger.debug("get credentials for : {}", email);
        if(!email.isEmpty()) {
            Account account = accountsService.getByEmail(email);
            if(account != null) {
                return new Credential(account);
            }
        }
        return null;
    }

    // get current
    @RequestMapping(value = "/get/{uuid}")
    @ResponseBody
    public Information getCurrent(@PathVariable String uuid) {
        logger.debug("get information for current user");
        if(utils.validUUID(uuid)) {
            Account account = accountsService.getByUUID(uuid);
            return new Information(account);
        }
        return null;
    }

    // get UUID
    @RequestMapping(value = "/get_uuid", method = RequestMethod.POST)
    @ResponseBody
    public Message getUUID(@RequestParam String email) {
        logger.debug("get UUID of : {}", email);
        Message m = utils.defaultMessage();

        if(!email.isEmpty()) {
            String uuid = accountsService.getUUID(email);
            if(uuid != null) {
                m.setSuccess(uuid);
            } else {
                m.setResult("not found");
            }
        }
        return m;
    }

    // list accounts
    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Information> listAll() {
        logger.debug("list all accounts");
        List<Account> accounts = accountsService.listAll();
        return utils.listTransformToInformation(accounts);
    }

    @RequestMapping(value = "/list/enabled")
    @ResponseBody
    public List<Information> listEnabled() {
        logger.debug("list enabled accounts");
        List<Account> accounts = accountsService.listEnabled();
        return utils.listTransformToInformation(accounts);
    }

    @RequestMapping(value = "/list/disabled")
    @ResponseBody
    public List<Information> listDisabled() {
        logger.debug("list disabled accounts");
        List<Account> accounts = accountsService.listDisabled();
        return utils.listTransformToInformation(accounts);
    }

    // list by role defaults to member if role is invalid
    @RequestMapping(value = "/list/by/{role}")
    @ResponseBody
    public List<Information> listByRole(@PathVariable String role) {
        role = utils.normalizeRole(role);
        logger.debug("list accounts by role : ", role);
        List<Account> accounts = accountsService.listByRole(role);
        return utils.listTransformToInformation(accounts);
    }

}
