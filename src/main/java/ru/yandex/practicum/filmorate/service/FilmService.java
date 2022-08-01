package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

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

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }
    public Map<Long, Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addLike(long userId, long filmId) {
        filmStorage.getFilms().get(filmId).getLikes().add(userId);
    }

    public void deleteLike(long userId, long filmId) {
        filmStorage.getFilms().get(filmId).getLikes().remove(userId);
    }

    public Set<Film> topFilms(int count) {
        return filmStorage.getAll()
                .stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(o -> o.getLikes().size())))
                .limit(count)
                .collect(Collectors.toSet());
    }
}
