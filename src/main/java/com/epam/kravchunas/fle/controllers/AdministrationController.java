package com.epam.kravchunas.fle.controllers;

import com.epam.kravchunas.fle.entities.user.Role;
import com.epam.kravchunas.fle.entities.user.User;
import com.epam.kravchunas.fle.services.user.RoleService;
import com.epam.kravchunas.fle.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/administration")
public class AdministrationController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    public String mainAdministration(
            @RequestParam(value = "size", required = false, defaultValue = "5") Integer size,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            Model model) {
        Role role = new Role();
        model.addAttribute("role", role);
        Page<User> usersPage = userService.findAllUser(PageRequest.of(page, size));
        model.addAttribute("usersPage", usersPage);
        model.addAttribute("numbers", IntStream.range(0, usersPage.getTotalPages()).toArray());
        return "administrationPages/administration";
    }

    @GetMapping("/blockUser")
    public String blockUser(@RequestParam(value = "userId") Long userId) {
        User user = userService.findUserById(userId);
        user.setActive(false);
        userService.saveUser(user);
        return "redirect:/administration";
    }

    @GetMapping("/unblockUser")
    public String unblockUser(@RequestParam(value = "userId") Long userId) {
        User user = userService.findUserById(userId);
        user.setActive(true);
        userService.saveUser(user);
        return "redirect:/administration";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam(value = "userId") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/administration";
    }

    @GetMapping("/choiceRoleForAdd")
    public String choiceRoleForAdd(@RequestParam(value = "userId") Long userId,
                          Model model) {
        User user = userService.findUserById(userId);
        List<Role> groupedData = Stream.concat(user.getRoles().stream(), roleService.getAllRoles().stream())
                .collect(Collectors.groupingBy(Role::getName)).values().stream()
                .filter(s -> s.size() == 1)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        model.addAttribute("userId", userId);
        model.addAttribute("roles", groupedData);
        return "administrationPages/choiceRoleForAdd";
    }

    @GetMapping("/addRole")
    public String addRole(@RequestParam(value = "userId") Long userId,
                          @RequestParam(value = "roleId") Long roleId) {
        User user = userService.findUserById(userId);
        user.getRoles().add(roleService.findRoleById(roleId));
        userService.saveUser(user);
        return "redirect:/administration";
    }

    @GetMapping("/choiceRoleForDelete")
    public String choiceRoleForDelete(@RequestParam(value = "userId") Long userId,
                             Model model) {
        User user = userService.findUserById(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("roles", user.getRoles());
        return "administrationPages/choiceRoleForDelete";
    }

    @GetMapping("/deleteRole")
    public String deleteRole(@RequestParam(value = "userId") Long userId,
                          @RequestParam(value = "roleId") Long roleId) {
        User user = userService.findUserById(userId);
        user.getRoles().remove(roleService.findRoleById(roleId));
        userService.saveUser(user);
        return "redirect:/administration";
    }

    @PostMapping("/addRoleToSite")
    public String addRoleToSite(@Valid @ModelAttribute("role") Role role,
                                BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            roleService.save(role);
        }
        return "redirect:/administration";
    }
}
