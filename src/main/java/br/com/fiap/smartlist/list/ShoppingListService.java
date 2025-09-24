package br.com.fiap.smartlist.list;

import br.com.fiap.smartlist.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final ShoppingListRepository repository;

    public List<ShoppingList> findByUser(User user) {
        return repository.findByUser(user);
    }

    public ShoppingList save(ShoppingList list) {
        return repository.save(list);
    }

    public ShoppingList getByIdAndUser(Long id, User user) {
        return repository.findById(id)
                .filter(l -> l.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Lista não encontrada!"));
    }

    public ShoppingList update(Long id, ShoppingList updatedList, User user) {
        ShoppingList list = getByIdAndUser(id, user);
        list.setTitle(updatedList.getTitle());
        list.setStatus(updatedList.getStatus());
        list.setTotal(updatedList.getTotal());
        return repository.save(list);
    }

    public void deleteByIdAndUser(Long id, User user) {
        ShoppingList list = getByIdAndUser(id, user);
        repository.delete(list);
    }
}
