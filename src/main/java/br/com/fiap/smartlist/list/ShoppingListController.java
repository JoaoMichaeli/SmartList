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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shopping-lists")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService service;
    private final UserService userService;
    private final MessageHelper messageHelper;

    @GetMapping
    public String index(@AuthenticationPrincipal OAuth2User principal, Model model) {
        User user = userService.registerOrGet(principal);
        model.addAttribute("lists", service.findByUser(user));
        return "lists/index";
    }

    @GetMapping("/form")
    public String form(ShoppingList list) {
        return "lists/form";
    }

    @PostMapping
    public String create(@Valid ShoppingList list, BindingResult result,
                         @AuthenticationPrincipal OAuth2User principal,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "lists/form";
        User user = userService.registerOrGet(principal);
        list.setUser(user);
        service.save(list);
        redirect.addFlashAttribute("message", messageHelper.get("list.create.success"));
        return "redirect:/shopping-lists";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model,
                       @AuthenticationPrincipal OAuth2User principal) {
        User user = userService.registerOrGet(principal);
        model.addAttribute("shoppingList", service.getByIdAndUser(id, user));
        return "lists/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid ShoppingList updatedList,
                         BindingResult result,
                         @AuthenticationPrincipal OAuth2User principal,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "lists/form";
        User user = userService.registerOrGet(principal);
        service.update(id, updatedList, user);
        redirect.addFlashAttribute("message", messageHelper.get("list.update.success"));
        return "redirect:/shopping-lists";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal OAuth2User principal,
                         RedirectAttributes redirect) {
        User user = userService.registerOrGet(principal);
        service.deleteByIdAndUser(id, user);
        redirect.addFlashAttribute("message", messageHelper.get("list.delete.success"));
        return "redirect:/shopping-lists";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id,
                          @AuthenticationPrincipal OAuth2User principal,
                          Model model) {
        User user = userService.registerOrGet(principal);
        model.addAttribute("list", service.getByIdAndUser(id, user));
        return "lists/details";
    }
}
