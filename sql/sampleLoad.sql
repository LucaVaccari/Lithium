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

insert into album(album_id, album_title, album_release_date, cover_img_path)
VALUES ('1', 'Fragile', '2022-07-20', 'img/album_cover/zefiro_eternative1.jpg'),
       ('2', 'Eternative', '2023-11-22', 'img/album_cover/zefiro_eternative.jpg'),
       ('3', 'Lambda', '2023-06-03', 'img/album_cover/lit_silver_lambda.jpg'),
       ('4', 'Butterfly', '2023-12-07', 'img/album_cover/lit_silver_butterfly.jpg');

insert into artist(artist_id, artist_name, artist_pic_path)
VALUES (1, 'Lit Silver', 'img/artist_pic/lit_silver.jpg'),
       (2, 'Zēfiro', 'img/artist_pic/zefiro.jpg');

insert into album_by_artist(album_id, artist_id)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (4, 1);

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
       (12, 'Nei suoi occhi', 5, 311, '', 2),
       (13, 'Liberation: ouverture', 1, 256, '', 3),
       (14, 'On paper', 2, 466, '', 3),
       (15, 'Soul breath', 3, 296, '', 3),
       (16, 'Come out', 4, 412, '', 3),
       (17, 'Because of you (part one)', 5, 488, '', 3),
       (18, 'Lies', 6, 197, '', 3),
       (19, 'Because of you (part two)', 7, 147, '', 3),
       (20, 'Everywhere', 8, 464, '', 3),
       (21, 'What we need to know', 1, 235, '', 4),
       (22, 'The sky in a box', 2, 281, '', 4),
       (23, 'Safe and calm', 3, 364, '', 4),
       (24, 'Rediscovery', 4, 346, '', 4),
       (25, 'Thank you', 5, 160, '', 4);

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

insert into user (user_id, username, pwd_hash)
values (1, 'lucavaccari03', '6cd6e51ad74b5a0ad4f6689e62fcfd621573d4df579762168e04a47cf654b5e5');

insert into playlist(playlist_id, playlist_title, playlist_description, user_id, cover_img_path)
VALUES (1, 'Lit Silver: best of', 'The best songs from Lit Silver catalogue', 1,
        'img/playlist_cover/lucavaccari03_lit_silver_best_of.jpg');

insert into track_in_playlist(track_id, playlist_id, added_on)
VALUES (12, 1, '2023-10-22'),
       (4, 1, '2023-06-04'),
       (20, 1, '2023-06-04'),
       (16, 1, '2023-06-04'),
       (3, 1, '2023-06-04'),
       (22, 1, '2023-12-07');

insert into user_saved_playlist(user_id, playlist_id)
VALUES (1, 1)