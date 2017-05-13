package relamapper.remoteservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import relamapper.model.AccountCredentials;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Value("${relamapper.remote.service.account.url}")
    private String accUrl;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.debug("fetching account for email : {}", s);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", s);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate template = new RestTemplate();

        logger.debug("performing POST request");
        AccountCredentials cred = template.postForObject(accUrl + "/credentials", request, AccountCredentials.class);
        if(cred != null) {
            logger.debug("account found");
            User user = new User(cred.getUuid(), cred.getPassword(), !cred.isDisabled(), true, true, !cred.isLocked(), getGrantedAuthority(cred));
            return user;
        }
        logger.debug("account not found");
        throw new UsernameNotFoundException("account not found");
    }

    private List<GrantedAuthority> getGrantedAuthority(AccountCredentials cred) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        switch (cred.getType()) {
            case "owner":
                authorities.add(new SimpleGrantedAuthority("ROLE_OWNER"));
            case "administrator":
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            case "moderator":
                authorities.add(new SimpleGrantedAuthority("ROLE_MODERATOR"));
            case "member":
                authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
            default:
                break;
        }

        logger.debug("authorities : {}", authorities);
        return authorities;
    }
}
