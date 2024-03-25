drop table if exists album;
drop table if exists track;
drop table if exists artist;
drop table if exists genre;
drop table if exists user;
drop table if exists playlist;
drop table if exists track_by_artist;
drop table if exists track_in_playlist;
drop table if exists track_genre;
drop table if exists album_by_artist;
drop table if exists album_genre;
drop table if exists artist_follower;
drop table if exists user_saved_album;
drop table if exists user_saved_playlist;

CREATE TABLE "album"
(
    "album_id"           INTEGER PRIMARY KEY UNIQUE,
    "album_title"        TEXT NOT NULL,
    "album_version"      TEXT,
    "album_release_date" TEXT,
    "cover_img_path"     TEXT
);

CREATE TABLE "track"
(
    "track_id"      INTEGER PRIMARY KEY UNIQUE,
    "track_title"   TEXT    NOT NULL,
    "track_version" TEXT,
    "track_number"  INTEGER NOT NULL,
    "duration"      INTEGER,
    "audio_path"    TEXT    NOT NULL,
    "album_id"      INTEGER NOT NULL references album
);

CREATE TABLE "artist"
(
    "artist_id"       INTEGER PRIMARY KEY UNIQUE,
    "artist_name"     TEXT NOT NULL,
    "artist_pic_path" TEXT,
    "artist_bio"      TEXT
);

CREATE TABLE "genre"
(
    "genre_id"          INTEGER PRIMARY KEY UNIQUE,
    "genre_name"        TEXT NOT NULL,
    "genre_description" TEXT
);

CREATE TABLE "user"
(
    "user_id"                  INTEGER PRIMARY KEY UNIQUE,
    "username"                 TEXT NOT NULL UNIQUE,
    "pwd_hash"                 TEXT NOT NULL,
    "profile_pic_path"         TEXT
);

CREATE TABLE "playlist"
(
    "playlist_id"          INTEGER PRIMARY KEY UNIQUE,
    "playlist_title"       TEXT    NOT NULL,
    "playlist_description" TEXT,
    "user_id"              INTEGER NOT NULL references user ON DELETE CASCADE,
    "cover_img_path"       TEXT
);

CREATE TABLE "track_by_artist"
(
    "track_id"  INTEGER NOT NULL references artist,
    "artist_id" INTEGER NOT NULL references track,
    PRIMARY KEY ("track_id", "artist_id")
);

CREATE TABLE "track_in_playlist"
(
    "track_id"    INTEGER NOT NULL references track,
    "playlist_id" INTEGER NOT NULL references playlist,
    "added_on"    TEXT,
    PRIMARY KEY ("track_id", "playlist_id")
);

CREATE TABLE "track_genre"
(
    "track_id" INTEGER NOT NULL references track,
    "genre_id" INTEGER NOT NULL references genre,
    primary key ("track_id", "genre_id")
);

CREATE TABLE "album_by_artist"
(
    "album_id"  INTEGER NOT NULL references album,
    "artist_id" INTEGER NOT NULL references artist,
    PRIMARY KEY ("album_id", "artist_id")
);

CREATE TABLE "artist_follower"
(
    "artist_id" INTEGER NOT NULL references artist,
    "user_id"   INTEGER NOT NULL references user,
    PRIMARY KEY ("artist_id", "user_id")
);

CREATE TABLE "user_saved_album"
(
    "user_id"  INTEGER NOT NULL references user,
    "album_id" INTEGER NOT NULL references album,
    PRIMARY KEY ("user_id", "album_id")
);

CREATE TABLE "user_saved_playlist"
(
    "user_id"     INTEGER NOT NULL references user,
    "playlist_id" INTEGER NOT NULL references playlist,
    PRIMARY KEY ("user_id", "playlist_id")
)