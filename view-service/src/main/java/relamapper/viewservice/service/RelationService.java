package relamapper.viewservice.service;

import relamapper.viewservice.model.Message;
import relamapper.viewservice.model.RelationContainer;
import relamapper.viewservice.model.RelationResponse;

public interface RelationService {

    boolean relationEstablished(String uuid, String uuid2);

    RelationResponse getByID(int id);

    RelationContainer getRelation(String uuid1, String uuid2);

    RelationContainer getRelations(String uuid);

    Message add(String uuid1, String uuid2, String type);

    Message edit(String uuid, int id, String type);

    Message remove(String uuid, int id);

}
