package ru.yandex.practicum.filmorate.errorHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.*;

import java.io.IOException;
import java.util.Map;
@RestControllerAdvice(assignableTypes = {UserController.class, FilmController.class})
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        return Map.of("validation exception", e.getMessage());
    }

    @ExceptionHandler({NoSuchUserException.class, NoSuchFilmException.class, NoSuchLikeException.class,
    NoSuchMpaException.class, NoSuchGenreException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoFoundExceptions(final Exception e) {
        return Map.of("not found", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleServerErrorExceptions(final IOException e) {
        return Map.of("IO exception", e.getMessage());
    }
}
