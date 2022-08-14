package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Like {
    private long film_id;
    private long user_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return getFilm_id() == like.getFilm_id() && getUser_id() == like.getUser_id();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFilm_id(), getUser_id());
    }
}
