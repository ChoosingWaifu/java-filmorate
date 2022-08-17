package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa Mpa;
    private final Set<Long> likes = new HashSet<>();
    private final List<Genre> genres = new ArrayList<>();

    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genres=" + genres +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return getId() == film.getId() && getName().equals(film.getName());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
