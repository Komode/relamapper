package relamapper.relationservice.service;

import relamapper.relationservice.model.Relation;

import java.util.List;

public interface RelationService {

    Relation byID(int id);

    List<Relation> byUUID(String uuid);

    Relation between(String uuid1, String uuid2);

    void register(String uuid1, String uuid2, String type) throws Exception;

    void update(int id, String uuid, String type) throws Exception;

    void delete(int id, String uuid) throws Exception;
}
