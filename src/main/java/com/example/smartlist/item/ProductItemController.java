package com.example.smartlist.item;

import com.example.smartlist.config.MessageHelper;
import com.example.smartlist.list.ShoppingList;
import com.example.smartlist.list.ShoppingListService;
import com.example.smartlist.user.User;
import com.example.smartlist.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/shopping-lists/{listId}/items")
@RequiredArgsConstructor
public class ProductItemController {

    private final ProductItemService service;
    private final ShoppingListService shoppingListService;
    private final UserRepository userRepository;
    private final MessageHelper messageHelper;

    private User getLoggedUser(Principal principal) {
        if (!(principal instanceof OAuth2AuthenticationToken oauthToken)) {
            throw new RuntimeException("Usuário não autenticado via GitHub");
        }

        OAuth2User oauthUser = oauthToken.getPrincipal();
        Object githubIdObj = oauthUser.getAttribute("id");
        if (githubIdObj == null) {
            throw new RuntimeException("ID do GitHub não encontrado no OAuth2");
        }

        String githubId = githubIdObj.toString();

        return userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no banco"));
    }

    @GetMapping("/form")
    public String form(@PathVariable Long listId, ProductItem item, Model model, Principal principal) {
        User user = getLoggedUser(principal);
        ShoppingList list = shoppingListService.getByIdAndUser(listId, user);
        model.addAttribute("list", list);
        model.addAttribute("item", item);
        return "items/form";
    }

    @PostMapping
    public String create(@PathVariable Long listId,
                         @Valid ProductItem item,
                         BindingResult result,
                         RedirectAttributes redirect,
                         Principal principal) {
        if (result.hasErrors()) return "items/form";

        User user = getLoggedUser(principal);
        ShoppingList list = shoppingListService.getByIdAndUser(listId, user);

        item.setShoppingList(list);
        service.save(item);

        redirect.addFlashAttribute("message", messageHelper.get("item.create.success"));
        return "redirect:/shopping-lists/" + listId;
    }

    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable Long listId,
                       @PathVariable Long itemId,
                       Model model,
                       Principal principal) {
        User user = getLoggedUser(principal);
        ShoppingList list = shoppingListService.getByIdAndUser(listId, user);

        ProductItem item = service.findByShoppingList(list)
                .stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        model.addAttribute("list", list);
        model.addAttribute("item", item);
        return "items/form";
    }

    @PostMapping("/{itemId}")
    public String update(@PathVariable Long listId,
                         @PathVariable Long itemId,
                         @Valid ProductItem updatedItem,
                         BindingResult result,
                         RedirectAttributes redirect,
                         Principal principal) {
        if (result.hasErrors()) return "items/form";

        User user = getLoggedUser(principal);
        ShoppingList list = shoppingListService.getByIdAndUser(listId, user);

        updatedItem.setId(itemId);
        updatedItem.setShoppingList(list);
        service.update(itemId, updatedItem);

        redirect.addFlashAttribute("message", messageHelper.get("item.update.success"));
        return "redirect:/shopping-lists/" + listId;
    }

    @PostMapping("/{itemId}/delete")
    public String delete(@PathVariable Long listId,
                         @PathVariable Long itemId,
                         RedirectAttributes redirect,
                         Principal principal) {
        User user = getLoggedUser(principal);
        ShoppingList list = shoppingListService.getByIdAndUser(listId, user);

        service.delete(itemId);

        redirect.addFlashAttribute("message", messageHelper.get("item.delete.success"));
        return "redirect:/shopping-lists/" + listId;
    }
}
