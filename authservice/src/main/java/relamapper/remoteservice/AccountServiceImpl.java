package relamapper.remoteservice;

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
import relamapper.model.ActionResponse;

@Service
public class AccountServiceImpl implements AccountService {

    @Value("${relamapper.remote.service.account.url}")
    private String accUrl;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate template = new RestTemplate();

    @Override
    public ActionResponse requestResetToken(String email) {
        logger.debug("sending password reset token request to account server : {}", accUrl);
        return template.getForObject(accUrl + "/password-reset-token/" + email, ActionResponse.class);
    }

    @Override
    public ActionResponse requestPasswordReset(String email, String password, String token) {
        logger.debug("requesting password change for account with email : {} with token : {}", email, token);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("token", token);
        map.add("email", email);
        map.add("password", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        return template.postForObject(accUrl + "/password-reset", request, ActionResponse.class);
    }
}
