package relamapper.authservice.service;

import relamapper.authservice.model.Credential;

public interface AccountService {

    Credential getCredential(String email);

}
