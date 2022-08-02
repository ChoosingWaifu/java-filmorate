package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Film getById(long id);

    Collection<Film> getAll();

    void addFilm(Film film);

    void updateFilm(Film film);
    Map<Long, Film> getFilms();

}
