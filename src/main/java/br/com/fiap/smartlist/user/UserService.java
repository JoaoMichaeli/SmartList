package br.com.fiap.smartlist.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User register(OAuth2User principal) {
        var user = userRepository.findByEmail(principal.getAttributes().get("email").toString());
        return user.orElseGet(() -> userRepository.save(new User(principal)));
    }

    public User getOrCreateUser(OAuth2User user) {
        return null;
    }
}
