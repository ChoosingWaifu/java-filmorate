package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long userId = 1L;
    public User getById(long id) {
        return users.get(id);//null pointer
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public User addUser(User user) {
        user.setId(userId++);
        users.put(user.getId(), user);
        return user;
    }
    public User updateUser(User user) {
        users.remove(user.getId());
        users.put(user.getId(),user);
        return user;
    }
    public Map<Long, User> getUsers() {
        return users;
    }
}
