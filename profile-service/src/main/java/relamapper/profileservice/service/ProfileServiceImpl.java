package relamapper.profileservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import relamapper.profileservice.model.Profile;
import relamapper.profileservice.repository.ProfileRepository;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ProfileRepository repository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public Profile byUUID(String uuid) {
        return repository.getOne(uuid);
    }

    @Override
    public Profile bySUID(String suid) {
        return repository.getProfileBySuid(suid);
    }

    @Override
    public String register(String uuid) throws Exception {
        logger.debug("register profile for UUID : {}", uuid);
        if(repository.exists(uuid)) {
            logger.warn("profile already registered for : {}", uuid);
        }
        Profile profile = new Profile(uuid);
        String suid = profile.getSuid();
        if(repository.countProfilesBySuid(suid) > 0) {
            do {
                logger.debug("regenerating SUID");
                suid = profile.generateSUID();
            } while (repository.countProfilesBySuid(suid) > 0);
        }
        repository.save(profile);
        return suid;
    }

    @Override
    public void update(Profile profile) throws Exception {
        logger.debug("update profile for UUID : {} and SUID : {}", profile.getUuid(), profile.getSuid());
        Profile stored = repository.getOne(profile.getUuid());
        if(stored == null) {
            logger.warn("could not find profile for UUID : {}", profile.getUuid());
            throw new Exception("profile not found");
        } else {
            logger.debug("updating profile for : {}", profile.getUuid());
            stored.setFirstname(profile.getFirstname());
            stored.setLastname(profile.getLastname());
            stored.setMiddlename(profile.getMiddlename());
            repository.save(stored);
        }
    }

    @Override
    public void delete(String uuid) throws Exception {
        logger.debug("delete profile for UUID : {}", uuid);
        if(!repository.exists(uuid)) {
            logger.debug("profile does not exists");
            throw new Exception("profile not found");
        } else {
            logger.debug("deleting profile");
            repository.delete(uuid);
        }
    }
}
