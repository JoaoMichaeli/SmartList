package com.example.smartlist.item;

import com.example.smartlist.list.ShoppingList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductItemService {

    private final ProductItemRepository repository;

    public List<ProductItem> findByShoppingList(ShoppingList list) {
        return repository.findByShoppingListId(list.getId());
    }

    public void save(ProductItem item) {
        repository.save(item);
    }

    public void update(Long id, ProductItem updatedItem) {
        ProductItem item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado!"));
        item.setName(updatedItem.getName());
        item.setPrice(updatedItem.getPrice());
        item.setChecked(updatedItem.isChecked());
        repository.save(item);
    }

    public void delete(Long id) {
        ProductItem item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado!"));
        repository.delete(item);
    }
}
