package relamapper.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import relamapper.repository.ResetTokenRepository;
import relamapper.model.ResetToken;

import java.util.Collection;
import java.util.Optional;

@Service
public class ResetTokenServiceImpl implements ResetTokenService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ResetTokenRepository repository;

    @Autowired
    public ResetTokenServiceImpl(ResetTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ResetToken> getResetToken(String token) {
        logger.debug("retrieving token with id : {}", token);
        return Optional.ofNullable(repository.getOne(token));
    }

    @Override
    public boolean validToken(String token, String uuid) {
        logger.debug("validating token : {}", token);
        Optional<ResetToken> t = Optional.ofNullable(repository.getOne(token));
        if(t.isPresent()) {
            ResetToken rt = t.get();
            logger.debug("token : {}", token);
            logger.debug("found : {}", rt.getToken());
            logger.debug("other : {}", rt.isValid());
            return rt.checkValidUUID(uuid);
        }
        logger.debug("Token not found...");
        return false;
    }

    @Override
    public String createToken(String uuid) {
        logger.info("creating reset token for UUID : {}", uuid);
        Collection<ResetToken> test = repository.getResetTokenByUuidAndValid(uuid, true);
        for(ResetToken token : test) {
            token.setInvalid();
        }
        ResetToken rt = new ResetToken(uuid);
        if(repository.exists(rt.getToken())) {
            String t;
            do {
                logger.info("token string collision, regenerating");
                t = rt.generateToken();
            } while (repository.exists(t));
        }
        repository.save(rt);
        return rt.getToken();
    }

    @Override
    public void tokenUsed(String token) {
        Optional<ResetToken> t = Optional.ofNullable(repository.getOne(token));
        if(t.isPresent()) {
            ResetToken rt = t.get();
            rt.setUsed(true);
            logger.debug("token has been set to used : {}", rt.getToken());
            repository.save(rt);
        }
    }
}
