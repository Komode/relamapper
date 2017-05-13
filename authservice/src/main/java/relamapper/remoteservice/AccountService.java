package relamapper.remoteservice;

import relamapper.model.ActionResponse;

public interface AccountService {

    ActionResponse requestResetToken(String email);

    ActionResponse requestPasswordReset(String email, String password, String token);

}
