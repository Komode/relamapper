package relamapper.authservice.service;

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
import relamapper.authservice.model.Credential;

@Service
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${relamapper.api.accounts.url}")
    private String API_URL;

    @Override
    public Credential getCredential(String email) {
        logger.debug("requesting credentials for email : {}", email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", email);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);

        RestTemplate template = new RestTemplate();
        String URL = API_URL + "/credentials";
        logger.debug("API url: {}", URL);

        return template.postForObject(URL, request, Credential.class);
    }
}
