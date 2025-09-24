package com.example.smartlist.list;

import com.example.smartlist.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    List<ShoppingList> findByUser(User user);
}
