package relamapper.authservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import relamapper.authservice.model.Credential;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private AccountService accountService;

    public AccountDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    private List<GrantedAuthority> getAuthorities(String role) {
        role = role.toLowerCase();
        List<GrantedAuthority> authorities = new ArrayList<>();
        switch (role) {
            case "owner":
                authorities.add(new SimpleGrantedAuthority("ROLE_OWNER"));
            case "admin":
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            case "moderator":
                authorities.add(new SimpleGrantedAuthority("ROLE_MODERATOR"));
            case "member":
                authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
        }
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.debug("[LOAD BY USERNAME] fetching credentials for : {}", s);
        Credential cred = accountService.getCredential(s);
        logger.debug("Credentials : {}", cred);
        if(cred != null) {
            return new User(cred.getUuid(), cred.getPassword(), cred.isEnabled(), true, true, true, getAuthorities(cred.getRole()));
        } else {
            throw new UsernameNotFoundException("email not found");
        }
    }
}
