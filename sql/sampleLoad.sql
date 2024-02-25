delete
from album
where true;
delete
from album_by_artist
where true;
delete
from album_genre
where true;
delete
from artist
where true;
delete
from artist_follower
where true;
delete
from genre
where true;
delete
from playlist
where true;
delete
from track
where true;
delete
from track_by_artist
where true;
delete
from track_in_playlist
where true;
delete
from user
where true;
delete
from user_saved_album
where true;
delete
from user_saved_playlist
where true;

insert into album(album_id, album_title, album_release_date)
VALUES ('1', 'Fragile', '2022-07-20'),
       (2, 'Eternative', '2023-11-22');

insert into artist(artist_id, artist_name)
VALUES (1, 'Lit Silver'),
       (2, 'Zēfiro');

insert into album_by_artist(album_id, artist_id)
VALUES (1, 1),
       (2, 2);

insert into track(track_id, track_title, track_number, duration, audio_path, album_id)
VALUES (1, 'In Solitudine', 1, 430, '', 1),
       (2, 'Irrealizzabile', 2, 387, '', 1),
       (3, 'Frutti della Terra', 3, 426, '', 1),
       (4, 'Sospiro d''Animo', 4, 297, '', 1),
       (5, 'Il Crollo', 5, 297, '', 1),
       (6, 'Follia: I. La stoffa dell''essere / II. Fragile e lieve / III. Parole di bimbo', 6, 488, '', 1),
       (7, 'Follia: IV. Terribile tarantella / V. Color viola / VI. Eppure vola', 7, 562, '', 1),
       (8, 'Suck your lollipop', 1, 174, '', 2),
       (9, 'Flowers', 2, 254, '', 2),
       (10, 'Beautiful', 3, 199, '', 2),
       (11, 'Waves', 4, 250, '', 2),
       (12, 'Nei suoi occhi', 5, 311, '', 2);

insert into track_by_artist(track_id, artist_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 2),
       (9, 2),
       (10, 2),
       (11, 2),
       (12, 2);