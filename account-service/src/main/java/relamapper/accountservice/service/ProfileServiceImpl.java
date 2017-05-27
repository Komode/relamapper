package relamapper.accountservice.service;

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
import relamapper.accountservice.model.Message;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Value("${relamapper.api.profile.url}")
    private String API_URL;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean registerProfile(String uuid) {
        logger.debug("creating POST request for register profile");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("uuid", uuid);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);

        RestTemplate template = new RestTemplate();
        String URL = API_URL + "/register";
        logger.debug("URL : {}", URL);

        Message message = template.postForObject(URL, request, Message.class);
        return (message != null && message.getStatus().equals("success"));
    }
}
