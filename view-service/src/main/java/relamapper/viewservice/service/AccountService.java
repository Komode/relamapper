package relamapper.viewservice.service;

import relamapper.viewservice.model.Information;

import java.util.List;

public interface AccountService {

    Information getUUID(String uuid);

    void setEmail(String uuid, String email);

    void registerMember(String email, String password);

    void registerOther(String email, String password, String role);

    List<Information> listAllEnabled();
}
