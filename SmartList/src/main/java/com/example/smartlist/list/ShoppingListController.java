package com.example.smartlist.list;

import com.example.smartlist.config.MessageHelper;
import com.example.smartlist.item.ProductItem;
import com.example.smartlist.user.User;
import com.example.smartlist.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/shopping-lists")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService service;
    private final UserService userService;
    private final MessageHelper messageHelper;

    @GetMapping
    public String index(@AuthenticationPrincipal OAuth2User principal, Model model) {
        User user = getCurrentUser(principal);
        List<ShoppingList> lists = service.findByUser(user);

        lists.forEach(list -> {
            BigDecimal total = list.getItems().stream()
                    .map(ProductItem::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            long checkedCount = list.getItems().stream()
                    .filter(ProductItem::isChecked)
                    .count();

            list.setTotal(total);
            list.setCheckedCount(checkedCount);
        });

        model.addAttribute("lists", lists);
        return "lists/index";
    }


    @GetMapping("/form")
    public String form(ShoppingList list) {
        return "lists/form";
    }

    @PostMapping
    public String create(@Valid ShoppingList list,
                         BindingResult result,
                         @AuthenticationPrincipal OAuth2User principal,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "lists/form";
        User user = getCurrentUser(principal);
        list.setUser(user);
        service.save(list);
        redirect.addFlashAttribute("message", messageHelper.get("list.create.success"));
        return "redirect:/shopping-lists";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model,
                       @AuthenticationPrincipal OAuth2User principal) {
        User user = getCurrentUser(principal);
        model.addAttribute("shoppingList", service.getByIdAndUser(id, user));
        return "lists/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid ShoppingList updatedList,
                         BindingResult result,
                         @AuthenticationPrincipal OAuth2User principal,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "lists/form";
        User user = getCurrentUser(principal);
        service.update(id, updatedList, user);
        redirect.addFlashAttribute("message", messageHelper.get("list.update.success"));
        return "redirect:/shopping-lists";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal OAuth2User principal,
                         RedirectAttributes redirect) {
        User user = getCurrentUser(principal);
        service.deleteByIdAndUser(id, user);
        redirect.addFlashAttribute("message", messageHelper.get("list.delete.success"));
        return "redirect:/shopping-lists";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id,
                          @AuthenticationPrincipal OAuth2User principal,
                          Model model) {
        User user = getCurrentUser(principal);
        ShoppingList list = service.getByIdAndUser(id, user);

        BigDecimal total = list.getItems().stream()
                .map(ProductItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long checkedCount = list.getItems().stream()
                .filter(ProductItem::isChecked)
                .count();

        model.addAttribute("list", list);
        model.addAttribute("total", total);
        model.addAttribute("checkedCount", checkedCount);

        return "lists/details";
    }


    private User getCurrentUser(OAuth2User principal) {
        return userService.registerOrGet(principal);
    }
}
