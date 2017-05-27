package relamapper.profileservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import relamapper.profileservice.model.Message;
import relamapper.profileservice.model.Profile;
import relamapper.profileservice.service.ProfileService;
import relamapper.profileservice.utilities.ControllerUtils;

@RestController
public class EditController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ControllerUtils utils = new ControllerUtils();
    private final ProfileService profileService;

    @Autowired
    public EditController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /** ROUTES **************************************************************/
    // register profile
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Message registerProfile(@RequestParam String uuid) {
        logger.debug("register profile for : {}", uuid);
        Message m = utils.defaultMessage();

        if(utils.validUUID(uuid)) {
            try {
                String suid = profileService.register(uuid);
                m.setSuccess("profile registered");
            } catch (Exception e) {
                m.setResult(e.getMessage());
            }
        }
        return m;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message updateProfile(@RequestParam String uuid,
                                 @RequestParam String firstname,
                                 @RequestParam String lastname,
                                 @RequestParam(required = false, defaultValue = "") String middlename) {
        logger.debug("update profile for : {}", uuid);
        Message m = utils.defaultMessage();

        if(utils.validUUID(uuid) && !firstname.isEmpty() && !lastname.isEmpty()) {
            Profile profile = profileService.byUUID(uuid);
            if(profile != null) {
                profile.setFirstname(firstname);
                profile.setLastname(lastname);
                profile.setMiddlename(middlename);
                try {
                    profileService.update(profile);
                    m.setSuccess("profile updated");
                } catch (Exception e) {
                    m.setResult(e.getMessage());
                }
            } else {
                m.setResult("profile not found");
            }
        }
        return m;
    }

}
