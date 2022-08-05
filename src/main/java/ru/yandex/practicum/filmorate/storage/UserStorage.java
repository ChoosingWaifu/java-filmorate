package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    User getById(long id);

    Collection<User> getAll();

    void addUser(User user);

    void updateUser(User user);

    Map<Long, User> getUsers();
}
