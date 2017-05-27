package relamapper.relationservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import relamapper.relationservice.model.Message;
import relamapper.relationservice.service.RelationService;
import relamapper.relationservice.utilities.ControllerUtils;

@RestController
public class EditController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ControllerUtils utils = new ControllerUtils();
    private final RelationService relationService;

    @Autowired
    public EditController(RelationService relationService) {
        this.relationService = relationService;
    }

    /** ROUTES **************************************************************/
    @RequestMapping(value = {"/register", "/add"}, method = RequestMethod.POST)
    @ResponseBody
    public Message registerRelation(@RequestParam String uuid1, @RequestParam String uuid2, @RequestParam String type) {
        logger.debug("registering relation");
        Message m = utils.defaultMessage();
        if(utils.validUUID(uuid1) && utils.validUUID(uuid2) && !type.isEmpty()) {
            try {
                relationService.register(uuid1, uuid2, type);
                m.setSuccess("relation registered");
            } catch (Exception e) {
                logger.error("relation registration error : {}", e.getMessage());
                m.setError(e.getMessage());
            }
        }
        return m;
    }

    @RequestMapping(value = {"/update", "/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public Message updateRelation(@RequestParam String uuid, @RequestParam int id, @RequestParam String type) {
        logger.debug("updating relation : {} for {}", id, uuid);
        Message m = utils.defaultMessage();
        if(utils.validUUID(uuid)) {
            try {
                relationService.update(id, uuid, type);
                m.setSuccess("relation updated");
            } catch (Exception e) {
                logger.error("relation update error : {}", e.getMessage());
                m.setError(e.getMessage());
            }
        }
        return m;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message deleteRelation(@RequestParam String uuid, @RequestParam int id) {
        logger.warn("deleting relation {} by {}", id, uuid);
        Message m = utils.defaultMessage();
        if(utils.validUUID(uuid)) {
            try {
                relationService.delete(id, uuid);
                m.setSuccess("relation deleted");
            } catch (Exception e) {
                m.setError(e.getMessage());
            }
        }
        return m;
    }
}
