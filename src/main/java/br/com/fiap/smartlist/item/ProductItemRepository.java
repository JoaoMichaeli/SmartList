package br.com.fiap.smartlist.item;

import br.com.fiap.smartlist.list.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

    List<ProductItem> findByShoppingList(ShoppingList shoppingList);

    Optional<ProductItem> findByIdAndShoppingList(Long id, ShoppingList shoppingList);
}
