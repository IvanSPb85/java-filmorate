
INSERT INTO users (email, login, name, birthday)
VALUES ('firstEmail', 'firstLogin', 'firstUser', '2001-01-01');

INSERT INTO users (email, login, name, birthday)
VALUES ('secondEmail', 'secondLogin', 'secondUser', '2001-01-01');

INSERT INTO users (email, login, name, birthday)
VALUES ('Email', 'Login', 'User', '2001-01-01');

INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('firstFilm', 'firstDescription', '2001-01-01', '100', '1');

INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('secondFilm', 'secondDescription', '2002-02-02', '200', '2');

INSERT INTO film_rating (film_id, user_id)
VALUES (2L, 1L);