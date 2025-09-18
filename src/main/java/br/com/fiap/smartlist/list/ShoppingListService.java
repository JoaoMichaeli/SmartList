package br.com.fiap.smartlist.list;

import br.com.fiap.smartlist.config.MessageHelper;
import br.com.fiap.smartlist.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final MessageHelper messageHelper;

    @Transactional(readOnly = true)
    public ShoppingList getByIdAndUser(long listId, User currentUser) {
        return (ShoppingList) shoppingListRepository.findByIdAndUser(listId, currentUser)
                .orElseThrow(() -> new RuntimeException(messageHelper.get("list.notfound")));
    }

    @Transactional(readOnly = true)
    public List<ShoppingList> findByUser(User currentUser) {
        return shoppingListRepository.findByUser(currentUser);
    }

    @Transactional
    public void save(@Valid ShoppingList shoppingList) {
        shoppingListRepository.save(shoppingList);
    }

    @Transactional
    public void update(Long listId, @Valid ShoppingList updatedList, User currentUser) {
        ShoppingList existing = getByIdAndUser(listId, currentUser);
        existing.setTitle(updatedList.getTitle());
        existing.setStatus(updatedList.getStatus());
        shoppingListRepository.save(existing);
    }

    @Transactional
    public void deleteByIdAndUser(Long listId, User currentUser) {
        ShoppingList existing = getByIdAndUser(listId, currentUser);
        shoppingListRepository.delete(existing);
    }
}
