package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchLikeException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@RestController
public class FilmController {
    private final FilmService filmService;
    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable @Min(1) long id) throws NoSuchFilmException {
        if (filmService.getFilms().containsKey(id)) {
            log.debug("Информация о фильме: {}", filmService.getFilms().get(id));
            return filmService.getById(id);
        } else throw new NoSuchFilmException("No such film");
    }
    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", filmService.getAll().size());//films.size()
        return filmService.getAll();
    }
    @GetMapping ("/films/popular")
    public Set<Film> topFilms(@RequestParam(required = false) Integer count){
        if(count == null || count<= 0) {
            count = 10;
        }
        log.info("Cписок {} популярных фильмов", count);
        return filmService.topFilms(count);
    }
    @PostMapping(value = "/films")
    public Film createFilm (@RequestBody Film film) throws ValidationException {
        validate(film);
        filmService.addFilm(film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }
    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException, NoSuchFilmException {
        validate(film);
        if (filmService.getFilms().containsKey(film.getId())) {
            filmService.updateFilm(film);
            log.info("Обновлена информация о фильме: {}", film);
            return film;
        } else {
            throw new NoSuchFilmException("No such film");
        }
    }
    @PutMapping (value = "/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Добавлен лайк пользователя: {}, фильм: {}", userId, id);
        filmService.addLike(userId, id);
    }
    @DeleteMapping (value = "/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) throws NoSuchLikeException {
        if (filmService.getById(id).getLikes().contains(userId)) {
            log.info("Удален лайк пользователя: {}, фильм: {}", userId, id);
            filmService.deleteLike(userId, id);
        } else throw new NoSuchLikeException("No such like");


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
