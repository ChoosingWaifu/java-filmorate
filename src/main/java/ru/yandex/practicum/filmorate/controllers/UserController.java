package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController

public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer userId = 1;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public Collection<User> findAllUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    public void validate(User user) throws ValidationException {
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            log.warn("Login exception");
            throw new ValidationException("Login can't be empty");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Login exception");
            throw new ValidationException("Login can't contain spaces");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Email exception");
            throw new ValidationException("Email should contain @");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            log.warn("Email exception");
            throw new ValidationException("Email can't be empty");
        }
        if (user.getBirthday().isAfter(LocalDateTime.now().toLocalDate())) {
            log.warn("Birthday exception");
            throw new ValidationException("Birthday can't be in the future");
        }
        if (user.getName().isBlank() || user.getName().isEmpty()) {
            log.warn("Empty name");
            user.setName(user.getLogin());
        }
    }

    @PostMapping(value = "/users")
    public User createUser (@RequestBody User user) throws ValidationException {
        validate(user);
        user.setId(userId++);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws ValidationException, NoSuchUserException {
        validate(user);
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            users.put(user.getId(), user);
            log.info("Изменение данных о пользователе: {}", user);
            return user;
        } else {
            throw new NoSuchUserException("No such user");
        }
    }
}
