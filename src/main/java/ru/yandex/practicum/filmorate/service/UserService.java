package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getById(long id) {
        return userStorage.getById(id);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Map<Long, User> getUsers() {
        return userStorage.getUsers();
    }

    public void addFriend(long userId, long friendId) {
        if (userStorage.getUsers().containsKey(userId)
         && userStorage.getUsers().containsKey(friendId)) {
            userStorage.getUsers().get(userId).getFriends().add(friendId);
            userStorage.getUsers().get(friendId).getFriends().add(userId);
        }
    }
    public Set<User> friendsById(long id) {
        Set <Long> friendsId = getAll().stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .get()
                .getFriends();
        Set<User> friends = new HashSet<>();
        for(Long fId : friendsId) {
            friends.add(getById(fId));
        }
        return friends;
    }

    public void deleteFriend(long userId, long friendId) {
        userStorage.getUsers().get(userId).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(userId);
    }

    public Set<User> commonFriends(long userId, long friendId) {
        Set<User> userFriends = friendsById(userId);
        Set<User> friendFriends = friendsById(friendId);
        return userFriends.stream().filter(friendFriends::contains).collect(Collectors.toSet());
    }

}
