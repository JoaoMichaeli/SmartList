package br.com.fiap.smartlist.item;

import br.com.fiap.smartlist.config.MessageHelper;
import br.com.fiap.smartlist.list.ShoppingList;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductItemService {

    private final ProductItemRepository productItemRepository;
    private final MessageHelper messageHelper;

    public List<ProductItem> getAllItems(){
        return productItemRepository.findAll();
    }

    @Transactional
    public ProductItem save(ProductItem item) {
        return productItemRepository.save(item);
    }

    @Transactional
    public void deleteById(Long id) {
        productItemRepository.delete(getItem(id));
    }

    private ProductItem getItem(Long id) {
        return productItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(messageHelper.get("item.notfound"))
        );
    }

    @Transactional(readOnly = true)
    public List<ProductItem> findByShoppingList(ShoppingList list) {
        return productItemRepository.findByShoppingList(list);
    }

    @Transactional
    public void update(Long itemId, @Valid ProductItem updatedItem, ShoppingList list) {
        ProductItem existing = productItemRepository.findByIdAndShoppingList(itemId, list)
                .orElseThrow(() -> new EntityNotFoundException(messageHelper.get("item.notfound")));

        existing.setName(updatedItem.getName());
        existing.setQuantity(updatedItem.getQuantity());
        existing.setPrice(updatedItem.getPrice());
        existing.setChecked(updatedItem.getChecked());

        productItemRepository.save(existing);
    }

    @Transactional
    public void deleteByIdAndList(Long itemId, ShoppingList list) {
        ProductItem item = productItemRepository.findByIdAndShoppingList(itemId, list)
                .orElseThrow(() -> new EntityNotFoundException(messageHelper.get("item.notfound")));
        productItemRepository.delete(item);
    }

    @Transactional
    public void toggleCheck(Long itemId, ShoppingList list) {
        ProductItem item = productItemRepository.findByIdAndShoppingList(itemId, list)
                .orElseThrow(() -> new EntityNotFoundException(messageHelper.get("item.notfound")));
        item.setChecked(!item.getChecked());
        productItemRepository.save(item);
    }
}
