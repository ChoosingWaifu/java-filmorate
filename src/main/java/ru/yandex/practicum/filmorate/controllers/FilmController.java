package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {
        private final Map<Integer, Film> films = new HashMap<>();
        private Integer filmId = 1;
        private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping("/films")
        public Collection<Film> findAll() {
            log.debug("Текущее количество фильмов: {}", films.size());
            return films.values();
    }

    @PostMapping(value = "/films")
    public Film createFilm (@RequestBody Film film) throws ValidationException {
        validate(film);
        film.setId(filmId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException, NoSuchFilmException {
        validate(film);
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            films.put(film.getId(), film);
            log.info("Обновлена информация о фильме: {}", film);
            return film;
        } else {
            throw new NoSuchFilmException("No such film");
        }

    }

    public void validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.warn("Name Exception");
            throw new ValidationException("Name can't be empty");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Description Exception");
            throw new ValidationException("This description is too long");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.warn("Date Exception");
            throw new ValidationException("The date is too early");
        }
        if (film.getDuration() <= 0) {
            log.warn("Duration Exception");
            throw new ValidationException("Duration <= 0");
        }
    }
}
