package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();

   public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
