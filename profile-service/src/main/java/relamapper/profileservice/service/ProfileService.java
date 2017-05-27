package relamapper.profileservice.service;

import relamapper.profileservice.model.Profile;

public interface ProfileService {

    Profile byUUID(String uuid);

    Profile bySUID(String suid);

    String register(String uuid) throws Exception;

    void update(Profile profile) throws Exception;

    void delete(String uuid) throws Exception;
}
