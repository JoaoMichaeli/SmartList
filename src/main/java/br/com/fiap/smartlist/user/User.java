package br.com.fiap.smartlist.user;

import br.com.fiap.smartlist.list.ShoppingList;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

@Entity
@Data
@Table(name = "smartlist_user")
@NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String avatarUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingList> shoppingLists;

    public User(OAuth2User principal) {
        var attributes = principal.getAttributes();
        this.name = (String) attributes.getOrDefault("name", "Usuário");
        this.email = (String) attributes.get("email");
        this.avatarUrl = (String) attributes.getOrDefault("avatar_url", attributes.get("picture"));
    }
}
