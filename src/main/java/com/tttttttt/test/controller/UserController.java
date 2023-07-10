package com.tttttttt.test.controller;

import com.tttttttt.test.dto.UserDto;
import com.tttttttt.test.entity.Role;
import com.tttttttt.test.entity.User;
import com.tttttttt.test.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        User user = userService.findUserByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        for (String roleName : userDto.getRoles()) {
            Role role = userService.findRoleByName(roleName);

            if (role != null) {
                user.getRoles().add(role);
            }
        }

        User savedUser = userService.createUser(user);

        UserDto savedUserDto = new UserDto();
        savedUserDto.setId(savedUser.getId());
        savedUserDto.setUsername(savedUser.getUsername());
        savedUserDto.setRoles(savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDto);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = userService.findAllRoles();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role savedRole = userService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    @PutMapping("/users/{username}/roles/{roleName}")
    public ResponseEntity<UserDto> addRoleToUser(@PathVariable String username, @PathVariable String roleName) {
        User user = userService.addRoleToUser(username, roleName);

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));

        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/users/{username}/roles/{roleName}")
    public ResponseEntity<UserDto> removeRoleFromUser(@PathVariable String username, @PathVariable String roleName) {
        User user = userService.removeRoleFromUser(username, roleName);

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));

        return ResponseEntity.ok(userDto);
    }

}