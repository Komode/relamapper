package relamapper.viewservice.service;

import relamapper.viewservice.model.Profile;

public interface ProfileService {

    Profile getUUID(String uuid);

    void updateProfile(String uuid, String firstname, String lastname, String middlename);

    String getFullName(String uuid);

}
