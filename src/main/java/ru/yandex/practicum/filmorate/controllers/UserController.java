package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@RestController

public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable long id) throws NoSuchUserException {
        if (userService.getUsers().containsKey(id)) {
            log.debug("Информация о пользователе: {}", userService.getUsers().get(id));
            return userService.getById(id);
        } else throw new NoSuchUserException("No such user");
    }
    @GetMapping ("/users/{id}/friends")
    public Set<User> friendsById(@PathVariable long id) {
        log.debug("Друзья пользователя: {}", userService.getUsers().get(id));
        return userService.friendsById(id);
    }
    @GetMapping ("/users/{id}/friends/common/{otherId}")
    public Set<User> commonFriends(@PathVariable long id,@PathVariable long otherId) {
        log.debug("Общие друзья пользователей: {}, {}",
                   userService.getUsers().get(id), userService.getUsers().get(otherId));
        return userService.commonFriends(id, otherId);
    }

    @GetMapping("/users")
    public Collection<User> findAllUsers() {
        log.debug("Текущее количество пользователей: {}", userService.getAll().size());
        return userService.getAll();
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
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            log.warn("Email exception");
            throw new ValidationException("Email can't be empty");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Email exception");
            throw new ValidationException("Email should contain @");
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
        userService.addUser(user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws ValidationException, NoSuchUserException {
        validate(user);
        if (userService.getUsers().containsKey(user.getId())) {
            userService.updateUser(user);
            log.info("Изменение данных о пользователе: {}", user);
            return user;
        } else {
            throw new NoSuchUserException("No such user");
        }
    }
    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) throws NoSuchUserException {
        if (userService.getUsers().containsKey(id) && userService.getUsers().containsKey(friendId)) {
            log.debug("Добавлен новый друг, id: {}", friendId);
            userService.addFriend(id, friendId);
        } else throw new NoSuchUserException("No such user");
    }
    @DeleteMapping (value = "/users/{id}/friends/{friendId}")
    public void deleteFriend( @PathVariable long id, @PathVariable long friendId) {
        log.debug("Удален друг, id: {}", friendId);
        userService.deleteFriend(id, friendId);
    }
}
