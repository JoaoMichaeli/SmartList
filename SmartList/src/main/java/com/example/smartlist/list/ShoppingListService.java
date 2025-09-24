package com.example.smartlist.list;

import com.example.smartlist.user.User;
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

    public void save(ShoppingList list) {
        repository.save(list);
    }

    public ShoppingList getByIdAndUser(Long id, User user) {
        return repository.findById(id)
                .filter(l -> l.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Lista não encontrada!"));
    }

    public void update(Long id, ShoppingList updatedList, User user) {
        ShoppingList list = getByIdAndUser(id, user);
        list.setTitle(updatedList.getTitle());
        list.setStatus(updatedList.getStatus());
        list.setTotal(updatedList.getTotal());
        repository.save(list);
    }

    public void deleteByIdAndUser(Long id, User user) {
        ShoppingList list = getByIdAndUser(id, user);
        repository.delete(list);
    }
}
