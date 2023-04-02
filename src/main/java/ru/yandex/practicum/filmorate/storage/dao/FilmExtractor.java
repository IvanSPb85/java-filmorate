package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmExtractor implements ResultSetExtractor<Map<Film, List<Genre>>> {

    @Override
    public Map<Film, List<Genre>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Film, List<Genre>> films = new HashMap<>();

        while (rs.next()) {
            Film film = Film.builder()
                    .id(rs.getLong(1))
                    .name(rs.getString(2))
                    .description(rs.getString(3))
                    .releaseDate(rs.getDate(4).toLocalDate())
                    .duration(rs.getInt(5))
                    .mpa(Mpa.builder()
                            .id(rs.getInt(6))
                            .name(rs.getString(7)).build())
                    .rating(rs.getInt(10)).build();

            films.putIfAbsent(film, new ArrayList<>());
            int genreId = rs.getInt(8);
            if (genreId != 0) {
                Genre genre = Genre.builder()
                    .id(genreId)
                    .name(rs.getString(9)).build();
                films.get(film).add(genre);
            }
        }
        return films;
    }
}