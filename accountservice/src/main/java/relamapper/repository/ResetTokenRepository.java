package relamapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import relamapper.model.ResetToken;

import java.util.Collection;

public interface ResetTokenRepository extends JpaRepository<ResetToken, String> {

    Collection<ResetToken> getResetTokenByUuidAndValid(String uuid, boolean valid);

}
