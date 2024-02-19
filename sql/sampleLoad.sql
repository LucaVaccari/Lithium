delete
from album
where album_title = 'Fragile'
  and album_release_date = date(2022, 7, 20);

delete
from album
where album_title = 'Lambda'
  and album_release_date = date(2023, 1, 3);

delete
from album
where album_title = 'Butterfly'
  and album_release_date = date(2023, 12, 7);

insert into album(album_title, album_version, album_release_date)
VALUES ('Fragile', '', date(2022, 7, 20)),
       ('Lambda', '', date(2023, 1, 3)),
       ('Butterfly', '', date(2023, 12, 7));