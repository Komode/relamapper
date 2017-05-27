package relamapper.viewservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import relamapper.viewservice.model.Message;
import relamapper.viewservice.service.AccountService;
import relamapper.viewservice.service.ProfileService;
import relamapper.viewservice.service.RelationService;

@Controller
public class ActionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AccountService accountService;
    private final ProfileService profileService;
    private final RelationService relationService;

    @Value("${relamapper.site-title}")
    private String siteTitle;

    @Autowired
    public ActionController(AccountService accountService, ProfileService profileService, RelationService relationService) {
        this.accountService = accountService;
        this.profileService = profileService;
        this.relationService = relationService;
    }

    /** CONVENIENCE METHODS **************************************************************/

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean isAuthenticated() {
        return !(getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    private String currentUUID() {
        return getAuthentication().getName();
    }

    /** ROUTES **************************************************************************/
    @RequestMapping(value = "/action/set/email", method = RequestMethod.POST)
    public String setEmail(@RequestParam String email) {
        String uuid = currentUUID();
        if(uuid != null) {
            accountService.setEmail(uuid, email);
        }
        return "redirect:/profile";
    }

    @RequestMapping(value = "/action/update/profile", method = RequestMethod.POST)
    public String updateProfile(@RequestParam String firstname,
                                @RequestParam String lastname,
                                @RequestParam(required = false, defaultValue = "") String middlename) {
        String uuid = currentUUID();
        if(uuid != null) {
            profileService.updateProfile(uuid, firstname, lastname, middlename);
        }
        return "redirect:/profile";
    }

    @RequestMapping(value = "/action/register", method = RequestMethod.POST)
    public String registerMember(@RequestParam String email, @RequestParam String password) {
        if(!email.isEmpty() && !password.isEmpty()) {
            accountService.registerMember(email, password);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/action/register/other", method = RequestMethod.POST)
    public String registerOther(@RequestParam String email, @RequestParam String password, @RequestParam String role) {
        if(!email.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
            accountService.registerOther(email, password, role);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/action/relation/add", method = RequestMethod.POST)
    public String addRelation(@RequestParam String uuid, @RequestParam String type) {
        if(!uuid.isEmpty() && !type.isEmpty()) {
            Message result = relationService.add(currentUUID(), uuid, type);
            logger.debug("add relation result : {} {}", result.getStatus(), result.getResult());
        }
        return "redirect:/relations";
    }

    @RequestMapping(value = "/action/relation/edit", method = RequestMethod.POST)
    public String editRelation(@RequestParam int rel_id, @RequestParam String type) {
        if(rel_id > 0 && !type.isEmpty()) {
            logger.debug("edit relation : {}", rel_id);
            Message result = relationService.edit(currentUUID(), rel_id, type);
            logger.debug("edit result : {} {}", result.getStatus(), result.getResult());
        }
        return "redirect:/relations";
    }

    @RequestMapping(value = "/action/relation/remove/{id}")
    public String removeRelation(@PathVariable int id) {
        logger.debug("remove relation {}", id);
        Message result = relationService.remove(currentUUID(), id);
        logger.debug("remove relation result : {} {}", result.getStatus(), result.getResult());
        return "redirect:/relations";
    }
}
