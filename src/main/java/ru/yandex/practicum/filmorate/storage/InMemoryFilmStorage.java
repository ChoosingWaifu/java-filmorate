package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    private Long filmId = 1L;

    public Film getById(long id) {
        return films.get(id);
    }

    public Collection<Film> getAll() {
        return films.values();
    }

    public void addFilm(Film film) {
        film.setId(filmId++);
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        films.remove(film.getId());
        films.put(film.getId(), film);
    }

    public Map<Long, Film> getFilms() {
        return films;
    }

    public Mpa getMpaById(int id) {
        return null;
    }

    public Collection<Mpa> getAllMpa() {
        return null;
    }

    public Genre getGenreById(int id) {
        return null;
    }

    public Collection<Genre> getAllGenre() {
        return null;
    }

    public void addLike(long userId, long filmId) {

    }

    public void deleteLike(long userId, long filmId) {

    }

    public Set<Film> topFilms(int count) {
        return null;
    }

    public Collection<Like> getAllLikes() {
        return null;
    }
}
