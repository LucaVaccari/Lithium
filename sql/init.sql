drop table if exists album;
drop table if exists track;
drop table if exists artist;
drop table if exists genre;
drop table if exists user;
drop table if exists playlist;
drop table if exists track_by_artist;
drop table if exists track_in_playlist;
drop table if exists album_by_artist;
drop table if exists album_genre;
drop table if exists artist_follower;
drop table if exists user_saved_album;
drop table if exists user_saved_playlist;

CREATE TABLE "album"
(
    "album_id"           INTEGER PRIMARY KEY,
    "album_title"        TEXT NOT NULL,
    "album_version"      TEXT,
    "album_release_date" TEXT,
    "cover_img_path"     TEXT
);

CREATE TABLE "track"
(
    "track_id"      INTEGER PRIMARY KEY,
    "track_title"   TEXT    NOT NULL,
    "track_version" TEXT,
    "track_number"  INTEGER NOT NULL,
    "duration"      INTEGER,
    "audio_path"    TEXT    NOT NULL,
    "album_id"      INTEGER NOT NULL
);

CREATE TABLE "artist"
(
    "artist_id"   INTEGER PRIMARY KEY,
    "artist_name" TEXT NOT NULL
);

CREATE TABLE "genre"
(
    "genre_id"          INTEGER PRIMARY KEY,
    "genre_name"        TEXT NOT NULL,
    "genre_description" TEXT
);

CREATE TABLE "user"
(
    "user_id"          INTEGER PRIMARY KEY,
    "username"         TEXT NOT NULL,
    "pwd_hash"         TEXT NOT NULL,
    "profile_pic_path" TEXT
);

CREATE TABLE "playlist"
(
    "playlist_id"          INTEGER PRIMARY KEY,
    "playlist_title"       TEXT    NOT NULL,
    "playlist_description" TEXT,
    "user_id"              INTEGER NOT NULL
);

CREATE TABLE "track_by_artist"
(
    "track_id"  INTEGER NOT NULL,
    "artist_id" INTEGER NOT NULL,
    FOREIGN KEY ("artist_id") REFERENCES "artist" ("artist_id"),
    FOREIGN KEY ("track_id") REFERENCES "track",
    PRIMARY KEY ("track_id", "artist_id")
);

CREATE TABLE "track_in_playlist"
(
    "track_id"    INTEGER NOT NULL,
    "playlist_id" INTEGER NOT NULL,
    "added_on"    TEXT,
    PRIMARY KEY ("track_id", "playlist_id"),
    FOREIGN KEY ("playlist_id") REFERENCES "playlist" ("playlist_id"),
    FOREIGN KEY ("track_id") REFERENCES "track" ("track_id")
);

CREATE TABLE "album_by_artist"
(
    "album_id"  INTEGER NOT NULL,
    "artist_id" INTEGER NOT NULL,
    PRIMARY KEY ("album_id", "artist_id"),
    FOREIGN KEY ("album_id") REFERENCES "album" ("album_id"),
    FOREIGN KEY ("artist_id") REFERENCES "artist" ("artist_id")
);

CREATE TABLE "album_genre"
(
    "album_id" INTEGER NOT NULL,
    "genre_id" INTEGER NOT NULL,
    FOREIGN KEY ("album_id") REFERENCES "album" ("album_id"),
    FOREIGN KEY ("genre_id") REFERENCES "genre" ("genre_id"),
    PRIMARY KEY ("album_id", "genre_id")
);

CREATE TABLE "artist_follower"
(
    "artist_id" INTEGER NOT NULL,
    "user_id"   INTEGER NOT NULL,
    FOREIGN KEY ("artist_id") REFERENCES "artist" ("artist_id"),
    FOREIGN KEY ("user_id") REFERENCES "user" ("user_id"),
    PRIMARY KEY ("artist_id", "user_id")
);

CREATE TABLE "user_saved_album"
(
    "user_id"  INTEGER NOT NULL,
    "album_id" INTEGER NOT NULL,
    PRIMARY KEY ("user_id", "album_id"),
    FOREIGN KEY ("user_id") REFERENCES "user" ("user_id"),
    FOREIGN KEY ("album_id") REFERENCES "album" ("album_id")
);

CREATE TABLE "user_saved_playlist"
(
    "user_id"     INTEGER NOT NULL,
    "playlist_id" INTEGER NOT NULL,
    PRIMARY KEY ("user_id", "playlist_id"),
    FOREIGN KEY ("user_id") REFERENCES "user" ("user_id"),
    FOREIGN KEY ("playlist_id") REFERENCES "playlist" ("playlist_id")
)