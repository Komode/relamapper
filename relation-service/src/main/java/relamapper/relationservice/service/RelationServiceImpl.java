package relamapper.relationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import relamapper.relationservice.model.Relation;
import relamapper.relationservice.repository.RelationRepository;

import java.util.List;

@Service
public class RelationServiceImpl implements RelationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final RelationRepository repository;

    @Autowired
    public RelationServiceImpl(RelationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Relation byID(int id) {
        logger.debug("get relation with ID : {}", id);
        return repository.getOne(id);
    }

    @Override
    public List<Relation> byUUID(String uuid) {
        logger.debug("get relations of : {}", uuid);
        return repository.getUUIDRelations(uuid);
    }

    @Override
    public Relation between(String uuid1, String uuid2) {
        logger.debug("get relation between : {} and {}", uuid1, uuid2);
        return repository.getRelationBetween(uuid1, uuid2);
    }

    @Override
    public void register(String uuid1, String uuid2, String type) throws Exception {
        logger.debug("register relation between : {} and {}", uuid1, uuid2);
        Relation relation = repository.getRelationBetween(uuid1, uuid2);
        if(relation != null) {
            logger.warn("relation between UUIDs exists");
            throw new Exception("relation exists");
        }
        relation = new Relation();
        relation.register(uuid1, uuid2, type);
        repository.save(relation);
    }

    @Override
    public void update(int id, String uuid, String type) throws Exception {
        logger.debug("update relation type of UUID : {} in relation with ID : {}", uuid, id);
        Relation relation = repository.getOne(id);
        if(relation == null) {
            logger.warn("relation with ID {} not found", id);
            throw new Exception("relation not found");
        }
        relation.updateRelation(uuid, type);
        repository.save(relation);
    }

    @Override
    public void delete(int id, String uuid) throws Exception {
        logger.debug("delete relation with ID : {}", id);
        Relation relation = repository.getOne(id);
        if(relation == null) {
            logger.warn("relation not found");
            throw new Exception("relation not found");
        } else if(relation.partOf(uuid)) {
            logger.info("deleting relation");
            repository.delete(id);
        } else {
            logger.warn("member is not part of relation");
            throw new Exception("unauthorized");
        }
    }
}
