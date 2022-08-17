package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Map;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film getById(long id) {
        return filmStorage.getById(id);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addFilm(Film film) {
        filmStorage.addFilm(film);
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }
    public Map<Long, Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addLike(long userId, long filmId) {
        filmStorage.addLike(userId, filmId);
    }

    public void deleteLike(long userId, long filmId) {
        filmStorage.deleteLike(userId,filmId);
    }

    public Collection<Film> topFilms(int count) {
        return filmStorage.topFilms(count);
    }
    public Mpa getMpaById(int id) {
        return filmStorage.getMpaById(id);
    }
    public Genre getGenreById(int id) {
        return filmStorage.getGenreById(id);
    }
    public Collection<Mpa> getAllMpa() {
        return filmStorage.getAllMpa();
    }
    public Collection<Genre> getAllGenres() {
        return filmStorage.getAllGenre();
    }
    public Collection<Like> getAllLikes(){
        return filmStorage.getAllLikes();
    }
}
