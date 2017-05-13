package relamapper.service;

import relamapper.model.ResetToken;

import java.util.Optional;

public interface ResetTokenService {

    Optional<ResetToken> getResetToken(String token);

    boolean validToken(String token, String uuid);

    String createToken(String uuid);

    void tokenUsed(String token);

}
