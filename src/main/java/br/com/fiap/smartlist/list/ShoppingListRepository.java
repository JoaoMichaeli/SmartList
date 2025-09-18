package br.com.fiap.smartlist.list;

import br.com.fiap.smartlist.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

    List<ShoppingList> findByUser(User user);

    Optional<ShoppingList> findByIdAndUser(Long id, User user);
}
