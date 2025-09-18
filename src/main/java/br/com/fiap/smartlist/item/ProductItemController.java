package br.com.fiap.smartlist.item;

import br.com.fiap.smartlist.config.MessageHelper;
import br.com.fiap.smartlist.list.ShoppingList;
import br.com.fiap.smartlist.list.ShoppingListService;
import br.com.fiap.smartlist.user.User;
import br.com.fiap.smartlist.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ProductItemController {

    private final ProductItemService productItemService;
    private final ShoppingListService shoppingListService;
    private final MessageHelper messageHelper;
    private final UserService userService;

    @GetMapping
    public String index(@PathVariable Long listId,
                        @AuthenticationPrincipal OAuth2User principal,
                        Model model) {
        User currentUser = userService.getOrCreateUser(principal);
        ShoppingList list = shoppingListService.getByIdAndUser(listId, currentUser);

        model.addAttribute("list", list);
        model.addAttribute("items", productItemService.findByShoppingList(list));
        return "items/index";
    }

    @GetMapping("/form")
    public String form(@PathVariable Long listId, ProductItem item, Model model) {
        model.addAttribute("listId", listId);
        return "items/form";
    }

    @PostMapping("/form")
    public String create(@PathVariable Long listId,
                         @Valid ProductItem item,
                         BindingResult result,
                         @AuthenticationPrincipal OAuth2User principal,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "items/form";

        User currentUser = userService.getOrCreateUser(principal);
        ShoppingList list = shoppingListService.getByIdAndUser(listId, currentUser);

        item.setShoppingList(list);
        productItemService.save(item);

        redirect.addFlashAttribute("message", messageHelper.get("item.create.success"));
        return "redirect:/shopping-lists/" + listId + "/items";
    }

    @PostMapping("/{itemId}/edit")
    public String update(@PathVariable Long listId,
                         @PathVariable Long itemId,
                         @Valid ProductItem updatedItem,
                         BindingResult result,
                         @AuthenticationPrincipal OAuth2User principal,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "items/form";

        User currentUser = userService.getOrCreateUser(principal);
        ShoppingList list = shoppingListService.getByIdAndUser(listId, currentUser);

        productItemService.update(itemId, updatedItem, list);

        redirect.addFlashAttribute("message", messageHelper.get("item.update.success"));
        return "redirect:/shopping-lists/" + listId + "/items";
    }

    @PostMapping("/{itemId}/delete")
    public String delete(@PathVariable Long listId,
                         @PathVariable Long itemId,
                         @AuthenticationPrincipal OAuth2User principal,
                         RedirectAttributes redirect) {
        User currentUser = userService.getOrCreateUser(principal);
        ShoppingList list = shoppingListService.getByIdAndUser(listId, currentUser);

        productItemService.deleteByIdAndList(itemId, list);

        redirect.addFlashAttribute("message", messageHelper.get("item.delete.success"));
        return "redirect:/shopping-lists/" + listId + "/items";
    }

    @PostMapping("/{itemId}/check")
    public String toggleCheck(@PathVariable Long listId,
                              @PathVariable Long itemId,
                              @AuthenticationPrincipal OAuth2User principal,
                              RedirectAttributes redirect) {
        User currentUser = userService.getOrCreateUser(principal);
        ShoppingList list = shoppingListService.getByIdAndUser(listId, currentUser);

        productItemService.toggleCheck(itemId, list);

        redirect.addFlashAttribute("message", messageHelper.get("item.check.success"));
        return "redirect:/shopping-lists/" + listId + "/items";
    }

}
