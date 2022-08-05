package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

@SpringBootTest
class FilmControllerTests {

    private FilmController filmController;
    private Film film;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setDuration(1);
        film.setReleaseDate(LocalDate.of(2000,1,1));
    }

    @Test
    void nameValidationTest() {
        film.setName(" ");
        Exception ex = assertThrows(ValidationException.class,() -> filmController.validate(film));
        assertEquals("Name can't be empty", ex.getMessage());
    }

    @Test
    void durationValidationTest() {
        film.setDuration(-1);
        Exception ex = assertThrows(ValidationException.class,() -> filmController.validate(film));
        assertEquals("Duration <= 0", ex.getMessage());
    }

    @Test
    void dateValidationTest() {
        film.setReleaseDate(LocalDate.MIN);
        Exception ex = assertThrows(ValidationException.class,() -> filmController.validate(film));
        assertEquals("The date is too early", ex.getMessage());
    }

    @Test
    void descriptionValidationTest() {
        film.setDescription("These characters are used for bracketed paste mode." +
                " Some terminal-based programs enable this mode so that they could" +
                " distinguish pasted text from directly typed text.\n" +
                "For example, text editors temporarily disable auto-indent" +
                " for pasted text, and CLI shells might allow you to review/confirm" +
                " the pasted commands before running them (even if they end with a newline)." +
                " In zsh, all pasted commands get a reverse highlight and aren't immediately" +
                " run until you press Enter.");
        Exception ex = assertThrows(ValidationException.class,() -> filmController.validate(film));
        assertEquals("This description is too long", ex.getMessage());
    }

}

