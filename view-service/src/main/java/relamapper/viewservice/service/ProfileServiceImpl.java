package relamapper.viewservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import relamapper.viewservice.model.Message;
import relamapper.viewservice.model.Profile;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${relamapper.gw.api.url}")
    private String API_URL;

    @Override
    public Profile getUUID(String uuid) {
        logger.debug("[PROFILE CURRENT] {}", uuid);
        RestTemplate template = new RestTemplate();
        String URL = API_URL + "/profile/get/" + uuid;
        Profile profile = template.getForObject(URL, Profile.class);
        logger.debug("[PROFILE] : {}", profile);
        return profile;
    }

    @Override
    public void updateProfile(String uuid, String firstname, String lastname, String middlename) {
        logger.debug("update profile : {}", uuid);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("uuid", uuid);
        map.add("firstname", firstname);
        map.add("lastname", lastname);
        map.add("middlename", middlename);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);
        RestTemplate template = new RestTemplate();
        String URL = API_URL + "/profile/update";
        Message result = template.postForObject(URL, request, Message.class);
        logger.debug("profile update result : {} {}", result.getStatus(), result.getResult());
    }

    @Override
    public String getFullName(String uuid) {
        logger.debug("get full name of UUID : {}", uuid);
        RestTemplate template = new RestTemplate();
        String URL = API_URL + "/profile/get/name/" + uuid;
        Message result = template.getForObject(URL, Message.class);
        logger.debug("GFN gfo completed");
        if(result.getStatus().equals("success")) {
            return result.getResult();
        } else {
            logger.warn(result.getResult());
            return "";
        }
    }
}
