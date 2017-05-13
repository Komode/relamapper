package relamapper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

@Controller
public class AuthViewController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean isAuthenticated() {
        if(getAuthentication() instanceof AnonymousAuthenticationToken)
            return false;
        return true;
    }

    private String getUsername() {
        return getAuthentication().getName();
    }

    private Collection<GrantedAuthority> getGrantedAuthorities() {
        return (Collection<GrantedAuthority>) getAuthentication().getAuthorities();
    }

    // login page
    @RequestMapping(value = {"/login", "/logout"}, method = RequestMethod.GET)
    public String loginPage(ModelMap model) {
        model.addAttribute("authenticated", isAuthenticated());
        return "login";
    }

    // password reset page
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String resetPage() {
        return "reset";
    }

    // password page
    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String passwordPage() {
        return "password";
    }

    // whoami page
    @RequestMapping(value = "/whoami", method = RequestMethod.GET)
    public String whoamiPage(ModelMap model) {
        model.addAttribute("authenticated", isAuthenticated());
        if(isAuthenticated()) {
            model.addAttribute("uuid", getUsername());
            model.addAttribute("authorities", getGrantedAuthorities());
        }
        return "whoami";
    }

}
