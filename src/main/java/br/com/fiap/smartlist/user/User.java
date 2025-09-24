package br.com.fiap.smartlist.user;

import br.com.fiap.smartlist.list.ShoppingList;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "smartlistuser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String avatarUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingList> shoppingLists;

    public User(OAuth2User principal, String email) {
        var attributes = principal.getAttributes();
        String login = (String) attributes.get("login");
        this.name = (String) attributes.getOrDefault("name", login != null ? login : "Usuário");
        this.email = email;
        this.avatarUrl = (String) attributes.getOrDefault("avatar_url", attributes.get("picture"));
    }
}
