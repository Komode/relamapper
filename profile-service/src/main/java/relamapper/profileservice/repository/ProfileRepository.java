package relamapper.profileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import relamapper.profileservice.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, String> {

    Profile getProfileBySuid(String suid);

    int countProfilesBySuid(String suid);

}
