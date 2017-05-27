package relamapper.viewservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import relamapper.viewservice.model.*;
import relamapper.viewservice.service.AccountService;
import relamapper.viewservice.service.ProfileService;
import relamapper.viewservice.service.RelationService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedAssignment")
@Controller
public class ViewController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AccountService accountService;
    private final ProfileService profileService;
    private final RelationService relationService;

    @Value("${relamapper.site-title}")
    private String siteTitle;

    @Autowired
    public ViewController(AccountService accountService, ProfileService profileService, RelationService relationService) {
        this.accountService = accountService;
        this.profileService = profileService;
        this.relationService = relationService;
    }

    /** CONVENIENCE METHODS **************************************************************/
    private ModelMap mapFiller(ModelMap map) {
        map.addAttribute("site_title", siteTitle);
        map.addAttribute("authenticated", isAuthenticated());
        if(isAuthenticated()) {
            map.addAttribute("current", currentUUID());
            String name = profileService.getFullName(currentUUID());
            map.addAttribute("membername", name);
        }
        return map;
    }

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
    // default home page
    @RequestMapping(value = "")
    public String indexPage(ModelMap map) {
        map = mapFiller(map);
        if(isAuthenticated()) {
            return "home/authenticated";
        } else {
            return "home/anonymous";
        }
    }

    // registration
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(ModelMap map) {
        map = mapFiller(map);
        return "register/member";
    }

    @RequestMapping(value = "/register/other", method = RequestMethod.GET)
    public String registerOther(ModelMap map) {
        map = mapFiller(map);
        return "register/other";
    }

    // profile
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profilePage(Principal principal, ModelMap map) {
        map = mapFiller(map);
        logger.debug("[PROFILEPAGE] before services");
        logger.debug("PRINCIPAL : {}", principal.getName());
        logger.debug("CURRENTUUID : {}", currentUUID());
        String uuid = principal.getName();
        Profile profile = profileService.getUUID(uuid);
        Information account = accountService.getUUID(uuid);
        map.addAttribute("profile", profile);
        map.addAttribute("account", account);
        logger.debug("[PROFILEPAGE] after services");
        return "profile/edit";
    }

    @RequestMapping(value = "/members", method = RequestMethod.GET)
    public String memberList(ModelMap map) {
        map = mapFiller(map);
        List<Information> members = accountService.listAllEnabled();
        map.addAttribute("members", members);
        return "profile/list";
    }

    @RequestMapping(value = "/members/{uuid}", method = RequestMethod.GET)
    public String memberPage(@PathVariable String uuid, ModelMap map) {
        map = mapFiller(map);
        if(!uuid.isEmpty()) {
            Profile profile = profileService.getUUID(uuid);
            map.addAttribute("viewing", uuid);
            map.addAttribute("profile", profile);
            if(isAuthenticated()) {
                boolean established = relationService.relationEstablished(currentUUID(), uuid);
                map.addAttribute("relation_established", established);
            }
            return "profile/view";
        } else {
            return "redirect:/members";
        }
    }

    // relations
    @RequestMapping(value = "/relations", method = RequestMethod.GET)
    public String relationsPage(ModelMap map) {
        map = mapFiller(map);
        if(isAuthenticated()) {
            String uuid = currentUUID();
            map.addAttribute("uuid", uuid);

            // add list of relations to map
            RelationContainer relations = relationService.getRelations(uuid);
            List<Relation> r = relations.getRelations();
            for(Relation rel : r) {
                logger.debug("relation : {} {} {}", rel.getId(), rel.getMasterType(), rel.getMasterType());
            }
            map.addAttribute("relations", relations.getRelations());

            return "relations/list";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/relations/add/{uuid}", method = RequestMethod.GET)
    public String relationFormUUID(@PathVariable String uuid, ModelMap map) {
        map = mapFiller(map);
        map.addAttribute("uuid", uuid);
        return "relations/form";
    }

    @RequestMapping(value = "/relations/edit/{id}", method = RequestMethod.GET)
    public String relationEditID(@PathVariable int id, ModelMap map) {
        logger.debug("CONTROLLER -- get relation with ID : {}", id);
        map = mapFiller(map);

        RelationResponse relation = relationService.getByID(id);

        if(relation != null) {
            logger.debug("ENTERED RELATIONS");
            Map<String, String> rel = new HashMap<>();
            rel.put("rel_id", String.valueOf(id));
            rel.put("with", relation.getUUIDOfRelation(currentUUID()));
            rel.put("type", relation.getUUIDRelation(currentUUID()));
            map.addAttribute("relation", rel);

            return "relations/edit";
        }
        logger.debug("relation not found");
        return "redirect:/relations";
    }
}
