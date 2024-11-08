package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/main")
    public String showMainUser(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", userService.findUserById(user.getId()));
        return "main";
    }

    @GetMapping("/main/edit")
    public String editMainUser(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "edit-main-user";
    }

    @PostMapping("/main/edit")
    public String saveMainEdit(@Valid User editedUser, BindingResult bindingResult,
                               @AuthenticationPrincipal User user, Model model) {
        if (userService.existsUserByUsername(editedUser.getUsername())) {
            if (!editedUser.getUsername().equals(user.getUsername())) {
                model.addAttribute("error_message", "Пользователь с таким логином уже существует");
                return "edit-main-user";
            }
        }
        if (bindingResult.hasErrors()) {
            return "edit-main-user";
        } else {
            userService.saveEditedUser(user, editedUser);
            return "redirect:/users/main";
        }
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("user_products", userService.findUserById(id).getProducts());
        return "user";
    }
}

