package br.com.fiap.smartlist.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerOrGet(OAuth2User principal) {
        var attributes = principal.getAttributes();
        String rawEmail = (String) attributes.get("email");
        String finalEmail = (rawEmail == null || rawEmail.isBlank())
                ? attributes.get("login") + "@users.noreply.github.com"
                : rawEmail;

        return userRepository.findByEmail(finalEmail)
                .orElseGet(() -> userRepository.save(new User(principal, finalEmail)));
    }
}
