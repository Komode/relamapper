package relamapper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import relamapper.model.ActionResponse;
import relamapper.remoteservice.AccountService;

@RestController
public class AuthRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AccountService accountService;

    @Autowired
    public AuthRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    // reset POST
    @RequestMapping(value = "/reset", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ActionResponse requestResetToken(@RequestParam String email) {
        logger.debug("reset request received");
        if(!email.isEmpty())
            return accountService.requestResetToken(email);
        ActionResponse ar = new ActionResponse();
        ar.setTask("reset-password");
        ar.setStatus("error");
        ar.setResult("invalid parameter");
        return null;
    }

    // password POST
    @RequestMapping(value = "/password", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ActionResponse resetPassword(@RequestParam String email,
                                        @RequestParam String password,
                                        @RequestParam String token) {
        logger.debug("password request received");
        if(!email.isEmpty() && !password.isEmpty() && !token.isEmpty()) {
            return accountService.requestPasswordReset(email, password, token);
        }
        ActionResponse ar = new ActionResponse();
        ar.setTask("set-password");
        ar.setStatus("error");
        ar.setResult("invalid parameter");
        return null;
    }
}
