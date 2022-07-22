package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
class FilmControllerTests {

    FilmController filmController;
    Film film;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setDuration(1);
        film.setReleaseDate(LocalDate.of(2000,1,1));
    }

    @Test
    void nameValidationTest() {
        film.setName(" ");
        assertThrows(ValidationException.class,() -> filmController.validate(film));
    }

    @Test
    void durationValidationTest() {
        film.setDuration(-1);
        assertThrows(ValidationException.class,() -> filmController.validate(film));
    }

    @Test
    void dateValidationTest() {
        film.setReleaseDate(LocalDate.MIN);
        assertThrows(ValidationException.class,() -> filmController.validate(film));
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
        assertThrows(ValidationException.class,() -> filmController.validate(film));
    }

}

