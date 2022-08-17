package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Film getById(long id);

    Collection<Film> getAll();

    void addFilm(Film film);

    void updateFilm(Film film);

    Map<Long, Film> getFilms();

    void addLike(long userId, long filmId);

    void deleteLike(long userId, long filmId);

    Mpa getMpaById(int id);

    Collection<Mpa> getAllMpa();

    Genre getGenreById(int id);

    Collection<Genre> getAllGenre();

    Collection<Film> topFilms(int count);

    Collection<Like> getAllLikes();
}
