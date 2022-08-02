package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
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
}
