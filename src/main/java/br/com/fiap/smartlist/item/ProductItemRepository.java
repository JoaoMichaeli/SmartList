package br.com.fiap.smartlist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    List<ProductItem> findByShoppingListId(Long shoppingListId);
}
