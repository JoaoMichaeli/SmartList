package br.com.fiap.smartlist.list;

import br.com.fiap.smartlist.config.MessageHelper;
import br.com.fiap.smartlist.user.User;
import br.com.fiap.smartlist.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/list")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final MessageHelper messageHelper;
    private final UserService userService;

    @GetMapping
    public String index(@AuthenticationPrincipal OAuth2User principal,
                        Model model) {
        User currentUser = userService.getOrCreateUser(principal);
        model.addAttribute("lists", shoppingListService.findByUser(currentUser));
        return "lists/index";
    }

    @GetMapping("/form")
    public String form(ShoppingList shoppingList) {
        return "lists/form";
    }

    @PostMapping("/form")
    public String create(@Valid ShoppingList shoppingList,
                         BindingResult result,
                         @AuthenticationPrincipal OAuth2User principal,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "lists/form";

        User currentUser = userService.getOrCreateUser(principal);
        shoppingList.setUser(currentUser);
        shoppingListService.save(shoppingList);

        redirect.addFlashAttribute("message", messageHelper.get("list.create.success"));
        return "redirect:/shopping-lists";
    }

    @GetMapping("/{listId}/edit")
    public String editForm(@PathVariable Long listId,
                           @AuthenticationPrincipal OAuth2User principal,
                           Model model) {
        User currentUser = userService.getOrCreateUser(principal);
        ShoppingList list = shoppingListService.getByIdAndUser(listId, currentUser);

        model.addAttribute("shoppingList", list);
        return "lists/form";
    }

    @PostMapping("/{listId}/edit")
    public String update(@PathVariable Long listId,
                         @Valid ShoppingList updatedList,
                         BindingResult result,
                         @AuthenticationPrincipal OAuth2User principal,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "lists/form";

        User currentUser = userService.getOrCreateUser(principal);
        shoppingListService.update(listId, updatedList, currentUser);

        redirect.addFlashAttribute("message", messageHelper.get("list.update.success"));
        return "redirect:/shopping-lists";
    }

    @PostMapping("/{listId}/delete")
    public String delete(@PathVariable Long listId,
                         @AuthenticationPrincipal OAuth2User principal,
                         RedirectAttributes redirect) {
        User currentUser = userService.getOrCreateUser(principal);
        shoppingListService.deleteByIdAndUser(listId, currentUser);

        redirect.addFlashAttribute("message", messageHelper.get("list.delete.success"));
        return "redirect:/shopping-lists";
    }

    @GetMapping("/{listId}")
    public String details(@PathVariable Long listId,
                          @AuthenticationPrincipal OAuth2User principal,
                          Model model) {
        User currentUser = userService.getOrCreateUser(principal);
        ShoppingList list = shoppingListService.getByIdAndUser(listId, currentUser);

        model.addAttribute("list", list);
        model.addAttribute("items", list.getItems());
        return "lists/details";
    }
}
