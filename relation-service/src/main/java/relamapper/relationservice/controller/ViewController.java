package relamapper.relationservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import relamapper.relationservice.model.Message;
import relamapper.relationservice.model.Relation;
import relamapper.relationservice.model.RelationJson;
import relamapper.relationservice.service.RelationService;
import relamapper.relationservice.utilities.ControllerUtils;

import java.util.List;

@RestController
public class ViewController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ControllerUtils utils = new ControllerUtils();
    private final RelationService relationService;

    @Autowired
    public ViewController(RelationService relationService) {
        this.relationService = relationService;
    }

    /** ROUTES **************************************************************/
    @RequestMapping("/check/{uuid1}/{uuid2}")
    @ResponseBody
    public Message isEstablished(@PathVariable String uuid1, @PathVariable String uuid2) {
        logger.debug("check if relation is established between : {} and {}", uuid1, uuid2);

        Message m = utils.defaultMessage();

        if(utils.validUUID(uuid1) && utils.validUUID(uuid2)) {
            Relation relation = relationService.between(uuid1, uuid2);
            if (relation != null) {
                m.setSuccess("established");
            } else {
                m.setSuccess("not established");
            }
        }

        return m;
    }

    @RequestMapping("/get/{uuid1}/{uuid2}")
    @ResponseBody
    public RelationJson getRelationBetween(@PathVariable String uuid1, @PathVariable String uuid2) {
        logger.debug("retrieve relation between {} and {}", uuid1, uuid2);
        if(utils.validUUID(uuid1) && utils.validUUID(uuid2)) {
            Relation relation = relationService.between(uuid1, uuid2);
            if(relation != null)
                return new RelationJson(relation);
        }
        logger.debug("relation not found");
        return null;
    }

    @RequestMapping("/get/{uuid}")
    @ResponseBody
    public List<RelationJson> getRelationsOf(@PathVariable String uuid) {
        logger.debug("retrieve all relations with {}", uuid);
        if(utils.validUUID(uuid)) {
            List<Relation> relations = relationService.byUUID(uuid);
            for(Relation r : relations) {
                if(r.getId() != null) {
                    logger.debug("GRUID ID : {}", r.getId());
                } else {
                    logger.warn("GRUID missing ID");
                }
            }

            List<RelationJson> relJson = utils.transformToJson(relations);
            for(RelationJson rj : relJson) {
                if(rj.getId() != null) {
                    logger.debug("GRUIDJ ID : {}", rj.getId());
                } else {
                    logger.warn("GRUIDJ missing ID");
                }
            }
            if(relations.size() > 0)
                return utils.transformToJson(relations);
        }
        logger.debug("no relations found");
        return null;
    }

    @RequestMapping("/get/id/{id}")
    @ResponseBody
    public RelationJson getRelationOfID(@PathVariable int id) {
        logger.debug("get relation with ID : {}", id);
        if(id > 0) {
            Relation r = relationService.byID(id);
            return new RelationJson(r);
        }
        return null;
    }
}
