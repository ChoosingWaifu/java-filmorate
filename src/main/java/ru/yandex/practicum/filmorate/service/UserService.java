package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    public void addUser(User user) {
        userStorage.addUser(user);
    }

    public void updateUser(User user) {
        userStorage.updateUser(user);
    }

    public Map<Long, User> getUsers() {
        return userStorage.getUsers();
    }

    public void addFriend(long userId, long friendId) {
         if (userStorage.getUsers().containsKey(userId)
         && userStorage.getUsers().containsKey(friendId)) {
             userStorage.addFriend(userId, friendId);
         }
    }
    public Collection<User> friendsById(long id) {
        return userStorage.friendsById(id);
    }

    public void deleteFriend(long userId, long friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> commonFriends(long userId, long friendId) {
       return userStorage.commonFriends(userId, friendId);
    }
    //

}
