package com.example.smartlist.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User registerOrGet(OAuth2User oauth2User) {
        if (oauth2User == null || oauth2User.getAttribute("id") == null) {
            throw new IllegalStateException("Usuário não autenticado via GitHub");
        }

        String githubId = Optional.ofNullable(oauth2User.getAttribute("id"))
                .map(String::valueOf)
                .orElseThrow(() -> new IllegalStateException("GitHub id não encontrado"));

        String name = oauth2User.getAttribute("name");
        String login = oauth2User.getAttribute("login");
        String email = oauth2User.getAttribute("email");
        String avatarUrl = oauth2User.getAttribute("avatar_url");

        Optional<User> existing = repository.findByGithubId(githubId);
        if (existing.isPresent()) {
            return existing.get();
        }

        User user = new User();
        user.setGithubId(githubId);
        user.setName(name != null ? name : login);
        user.setGithubLogin(login);
        user.setEmail(email);
        user.setAvatarUrl(avatarUrl);

        return repository.save(user);
    }
}
