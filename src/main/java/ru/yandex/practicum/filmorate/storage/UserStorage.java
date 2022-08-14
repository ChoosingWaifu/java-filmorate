package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    User getById(long id);

    Collection<User> getAll();

    void addUser(User user);

    void updateUser(User user);

    Map<Long, User> getUsers();
    void addFriend(long userId, long friendId);
    void deleteFriend(long userId, long friendId);
    Collection<User> friendsById(long id);
    List<User> commonFriends(long userId, long friendId);
}
