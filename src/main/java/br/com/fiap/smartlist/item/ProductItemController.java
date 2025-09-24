package br.com.fiap.smartlist.item;

import br.com.fiap.smartlist.config.MessageHelper;
import br.com.fiap.smartlist.list.ShoppingList;
import br.com.fiap.smartlist.list.ShoppingListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shopping-lists/{listId}/items")
@RequiredArgsConstructor
public class ProductItemController {

    private final ProductItemService service;
    private final ShoppingListService shoppingListService;
    private final MessageHelper messageHelper;

    @GetMapping("/form")
    public String form(@PathVariable Long listId, ProductItem item, Model model) {
        ShoppingList list = shoppingListService.getByIdAndUser(listId, null);
        model.addAttribute("list", list);
        return "items/form";
    }

    @PostMapping
    public String create(@PathVariable Long listId,
                         @Valid ProductItem item,
                         BindingResult result,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "items/form";
        ShoppingList list = shoppingListService.getByIdAndUser(listId, null);
        item.setShoppingList(list);
        service.save(item);
        redirect.addFlashAttribute("message", messageHelper.get("item.create.success"));
        return "redirect:/shopping-lists/" + listId;
    }

    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable Long listId,
                       @PathVariable Long itemId,
                       Model model) {
        ShoppingList list = shoppingListService.getByIdAndUser(listId, null);
        ProductItem item = service.findByShoppingList(list)
                .stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item não encontrado!"));
        model.addAttribute("item", item);
        model.addAttribute("list", list);
        return "items/form";
    }

    @PostMapping("/{itemId}")
    public String update(@PathVariable Long listId,
                         @PathVariable Long itemId,
                         @Valid ProductItem updatedItem,
                         BindingResult result,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "items/form";
        updatedItem.setId(itemId);
        service.update(itemId, updatedItem);
        redirect.addFlashAttribute("message", messageHelper.get("item.update.success"));
        return "redirect:/shopping-lists/" + listId;
    }

    @PostMapping("/{itemId}/delete")
    public String delete(@PathVariable Long listId,
                         @PathVariable Long itemId,
                         RedirectAttributes redirect) {
        service.delete(itemId);
        redirect.addFlashAttribute("message", messageHelper.get("item.delete.success"));
        return "redirect:/shopping-lists/" + listId;
    }
}
