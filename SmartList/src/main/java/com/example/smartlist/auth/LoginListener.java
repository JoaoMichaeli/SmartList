package com.example.smartlist.auth;

import com.example.smartlist.user.User;
import com.example.smartlist.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

@Component
@RequiredArgsConstructor
public class LoginListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof OAuth2User oauth2User) {
            User user = userService.registerOrGet(oauth2User);

            System.out.println("Usuário logado: " + user.getName());
        }
    }
}
