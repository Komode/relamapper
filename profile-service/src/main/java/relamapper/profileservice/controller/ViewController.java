package relamapper.profileservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import relamapper.profileservice.model.Message;
import relamapper.profileservice.model.Profile;
import relamapper.profileservice.model.ProfileJson;
import relamapper.profileservice.service.ProfileService;
import relamapper.profileservice.utilities.ControllerUtils;

@RestController
public class ViewController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ControllerUtils utils = new ControllerUtils();
    private final ProfileService profileService;

    @Autowired
    public ViewController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /** ROUTES **************************************************************/
    // get profile of UUID
    @RequestMapping(value = "/get/{uuid}")
    @ResponseBody
    public ProfileJson getProfile(@PathVariable String uuid) {
        logger.debug("get profile of {}", uuid);
        if(utils.validUUID(uuid)) {
            Profile profile = profileService.byUUID(uuid);
            ProfileJson json = new ProfileJson();
            json.setSuid(profile.getSuid());
            json.setFirstname(profile.getFirstname());
            json.setLastname(profile.getLastname());
            json.setMiddlename(profile.getMiddlename());
            logger.debug("json : {}", json);
            return json;
        }
        return null;
    }

    @RequestMapping(value = "/get/suid/{suid}")
    @ResponseBody
    public Profile getBySUID(@PathVariable String suid) {
        logger.debug("get profile by SUID : {}", suid);
        return profileService.bySUID(suid);
    }

    @RequestMapping(value = "/get/name/{uuid}")
    @ResponseBody
    public Message getName(@PathVariable String uuid) {
        logger.debug("get full name for UUID : {}", uuid);
        Message m = utils.defaultMessage();

        if(utils.validUUID(uuid)) {
            m.setError("Profile not found");
            Profile profile = profileService.byUUID(uuid);
            if (profile != null) {
                if (!profile.getFirstname().isEmpty() && !profile.getLastname().isEmpty()) {
                    String name = profile.getFirstname();
                    name += (!profile.getMiddlename().isEmpty()) ? " " + profile.getMiddlename() + " " : " ";
                    name += profile.getLastname();
                    m.setSuccess(name);
                } else {
                    m.setSuccess("name not set");
                }
            }
        } else {
            logger.warn("could not find profile of UUID : {}", uuid);
        }
        return m;
    }
}
