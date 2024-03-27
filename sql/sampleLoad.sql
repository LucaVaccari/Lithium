delete
from album
where true;
delete
from album_by_artist
where true;
delete
from track_genre
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

insert into genre(genre_id, genre_name)
VALUES (1, 'Progressive rock'),
       (2, 'Rock'),
       (3, 'Pop'),
       (4, 'Acoustic'),
       (5, 'Post-rock'),
       (6, 'Alternative rock'),
       (7, 'Metal');

insert into album(album_id, album_title, album_release_date, cover_img_path)
VALUES ('1', 'Fragile', '2022-07-20', 'img/album_cover/lit_silver_fragile.jpg'),
       ('2', 'Eternative', '2023-11-22', 'img/album_cover/zefiro_eternative.jpg'),
       ('3', 'Lambda', '2023-06-03', 'img/album_cover/lit_silver_lambda.jpg'),
       ('4', 'Butterfly', '2023-12-07', 'img/album_cover/lit_silver_butterfly.jpg'),
       ('5', 'Lament', '2024-03-08', 'img/album_cover/lit_silver_lament.jpg');

insert into artist(artist_id, artist_name, artist_pic_path)
VALUES (1, 'Lit Silver', 'img/artist_pic/lit_silver.jpg'),
       (2, 'Zēfiro', 'img/artist_pic/zefiro.jpg');

insert into album_by_artist(album_id, artist_id)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (4, 1),
       (5, 1);

insert into track(track_id, track_title, track_number, duration, audio_path, album_id)
VALUES (1, 'In Solitudine', 1, 430, 'Solitudine/Solitudine.m3u8', 1),
       (2, 'Irrealizzabile', 2, 387, 'Irrealizzabile/Irrealizzabile.m3u8', 1),
       (3, 'Frutti della Terra', 3, 426, 'FruttiDellaTerra/FruttiDellaTerra.m3u8', 1),
       (4, 'Sospiro d''Animo', 4, 297, 'SospiroDAnimo/SospiroDAnimo.m3u8', 1),
       (5, 'Il Crollo', 5, 297, 'IlCrollo/IlCrollo.m3u8', 1),
       (6,
        'Follia: I. La stoffa dell''essere / II. Fragile e lieve / III. Parole di bimbo / IV. Terribile tarantella / V. Color viola / VI. Eppure vola',
        6, 1050, 'Follia/Follia.m3u8', 1),
       (8, 'Suck your lollipop', 1, 174, 'SuckYourLollipop/syl.m3u8', 2),
       (9, 'Flowers', 2, 254, 'Flowers/Flowers.m3u8', 2),
       (10, 'Beautiful', 3, 199, 'Beautiful/Beautiful.m3u8', 2),
       (11, 'Waves', 4, 250, 'Waves/Waves.m3u8', 2),
       (12, 'Nei suoi occhi', 5, 311, 'NeiSuoiOcchi/NeiSuoiOcchi.m3u8', 2),
       (13, 'Liberation: ouverture', 1, 256, 'LiberationOuverture/LiberationOuverture.m3u8', 3),
       (14, 'On paper', 2, 466, 'OnPaper/OnPaper.m3u8', 3),
       (15, 'Soul breath', 3, 296, 'SoulBreath/SoulBreath.m3u8', 3),
       (16, 'Come out', 4, 412, 'ComeOut/ComeOut.m3u8', 3),
       (17, 'Because of you (part one)', 5, 488, 'BecauseOfYou1/BecauseOfYou1.m3u8', 3),
       (18, 'Lies', 6, 197, 'Lies/Lies.m3u8', 3),
       (19, 'Because of you (part two)', 7, 147, 'BecauseOfYou2/BecauseOfYou2.m3u8', 3),
       (20, 'Everywhere', 8, 464, 'Everywhere/Everywhere.m3u8', 3),
       (21, 'What we need to know', 1, 235, 'WhatWeNeedToKnow/WhatWeNeedToKnow.m3u8', 4),
       (22, 'The sky in a box', 2, 281, 'TheSkyInABox/TheSkyInABox.m3u8', 4),
       (23, 'Safe and calm', 3, 364, 'SafeAndCalm/SafeAndCalm.m3u8', 4),
       (24, 'Rediscovery', 4, 346, 'Rediscovery/Rediscovery.m3u8', 4),
       (25, 'Thank you', 5, 160, 'ThankYou/ThankYou.m3u8', 4);

insert into track(track_id, track_title, track_version, track_number, duration, audio_path, album_id)
VALUES (26, 'Lament', 'Single edit', 1, 297, 'Lament(singleEdit)/Lament(singleEdit).m3u8', 5);

insert into track_genre(track_id, genre_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 6),
       (8, 2),
       (9, 6),
       (10, 6),
       (11, 6),
       (12, 1),
       (13, 5),
       (14, 1),
       (15, 1),
       (16, 5),
       (17, 7),
       (18, 5),
       (19, 7),
       (20, 5),
       (21, 4),
       (21, 3),
       (22, 2),
       (22, 3),
       (23, 4),
       (23, 3),
       (24, 2),
       (24, 3),
       (25, 4),
       (25, 3),
       (26, 1),
       (26, 2),
       (26, 3);

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
       (12, 2),
       (13, 1),
       (14, 1),
       (15, 1),
       (16, 1),
       (17, 1),
       (18, 1),
       (19, 1),
       (20, 1),
       (21, 1),
       (22, 1),
       (23, 1),
       (24, 1),
       (25, 1),
       (26, 1);

insert into user (user_id, username, pwd_hash, profile_pic_path)
values (1, 'lucavaccari03', '6cd6e51ad74b5a0ad4f6689e62fcfd621573d4df579762168e04a47cf654b5e5',
        'img/user_pro_pic/default_user_pro_pic.jpg');

insert into playlist(playlist_id, playlist_title, playlist_description, user_id, cover_img_path)
VALUES (0, 'Saved tracks', 'The favourite tracks of lucavaccari03', 1,
        'img/playlist_cover/saved_tracks_cover.jpg'),
       (1, 'Lit Silver: best of', 'The best songs from Lit Silver catalogue', 1,
        'img/playlist_cover/lucavaccari03_lit_silver_best_of.jpg');

insert into track_in_playlist(track_id, playlist_id, added_on)
VALUES (12, 1, '2023-10-22'),
       (4, 1, '2023-06-04'),
       (20, 1, '2023-06-04'),
       (16, 1, '2023-06-04'),
       (3, 1, '2023-06-04'),
       (22, 1, '2023-12-07');

insert into user_saved_playlist(user_id, playlist_id)
VALUES (1, 0),
       (1, 1);