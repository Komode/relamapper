package relamapper.viewservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import relamapper.viewservice.model.Information;
import relamapper.viewservice.model.Message;

import java.util.Arrays;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${relamapper.gw.api.url}")
    private String API_URL;

    @Override
    public Information getUUID(String uuid) {
        logger.debug("[ACCOUNT CURRENT] {}", uuid);
        RestTemplate template = new RestTemplate();
        String URL = API_URL + "/accounts/get/" + uuid;
        Information account = template.getForObject(URL, Information.class);
        logger.debug("[ACCOUNT] : {}", account);
        return account;
    }

    @Override
    public void setEmail(String uuid, String email) {
        logger.debug("account set email for : {}", uuid);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("uuid", uuid);
        map.add("email", email);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);

        RestTemplate template = new RestTemplate();

        String URL = API_URL + "/accounts/set/email";
        Message result = template.postForObject(URL, request, Message.class);
        logger.debug("set email result : {} {}", result.getStatus(), result.getResult());
    }

    @Override
    public void registerMember(String email, String password) {
        logger.debug("register member");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("email", email);
        map.add("password", password);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);

        RestTemplate template = new RestTemplate();

        String URL = API_URL + "/accounts/register";
        Message result = template.postForObject(URL, request, Message.class);
        logger.debug("register result : {} {}", result.getStatus(), result.getResult());
    }

    @Override
    public void registerOther(String email, String password, String role) {
        logger.debug("register other : {}", role);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("email", email);
        map.add("password", password);
        map.add("role", role);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);

        RestTemplate template = new RestTemplate();

        String URL = API_URL + "/accounts/register/other";
        Message result = template.postForObject(URL, request, Message.class);
        logger.debug("register result : {} {}", result.getStatus(), result.getResult());
    }

    @Override
    public List<Information> listAllEnabled() {
        RestTemplate template = new RestTemplate();
        String URL = API_URL + "/accounts/list/enabled";
        ResponseEntity<Information[]> responseEntity = template.getForEntity(URL, Information[].class);
        Information[] tmp = responseEntity.getBody();
        return Arrays.asList(tmp);
    }
}
