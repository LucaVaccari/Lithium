# LIThium

A simple unencrypted music streaming service made in Java.

## Technical details

The project uses the following technologies:

- JavaFX for client GUI
- HTTP for general communication
- HTTP live-streaming (HLS) for audio streaming
- SQLite for storing server data (not the actual audio files)

### Client

The client uses JavaFX for its GUI, made of a login screen, a search tab, a library tab and a settings tab.

The user login data is cached, with the option to log out to use a different account.

The search tab is dynamically constructed and allows searching artists, songs, albums and playlists.

The library tab allows the user to see followed artists, playlists and albums and to manage their own playlists.

The settings tab will allow managing user settings (change user info, password or delete account), audio settings
(equalization), server settings (server URL and port).

Every information is fetched via the HTTP protocol, using JSON for potential message bodies.
The actual streaming protocol is HLS and is already implemented by JavaFX's Media class.

### HTTP Server

The HTTP server acts as a medium between the client and the database and has direct access to the audio and image files
required by the client application.

A config file should specify the database and the file folder locations.

### Database

The database stores information about music, artists and users, as well as references to audio and image files present
on the server.

It's SQLite database, since it's for a small university project.

<!-- TODO: insert database schema -->