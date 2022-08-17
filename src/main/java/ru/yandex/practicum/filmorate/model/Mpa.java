package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Mpa {

    private int id;

    private String name;

    public String toString() {
        return "Mpa{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
