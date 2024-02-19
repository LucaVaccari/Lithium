# Lithium

A simple unencrypted music streaming service made in Java for a university assignment.

## Functionalities

### Listening parties

Any user can see what their friends are listening to and can join the 'party' to listen to the same song in sync.

During a listening party an ephemeral chat is available for communication between the members of the party.
The chat is erased at the end of the party.

## Technical details

The project uses the following technologies:

- JavaFX for client GUI
- HTTP and websockets for general communication
- HTTP live-streaming (HLS) for audio streaming
- SQLite for storing server data (not the actual audio files)

### Client

The client uses JavaFX for its GUI, made of a login screen, a search tab, a library tab and a settings tab.
From the search tab the user can select albums/artists/playlists and see a special page for each.

The user login data is cached, with the option to log out to use a different account.

The search tab is dynamically constructed and allows searching artists, songs, albums and playlists.

The library tab allows the user to see followed artists, playlists and albums and to manage their own playlists.

The settings tab will allow managing user settings (change user info, password or delete account), audio settings
(equalization), server settings (server URL and port).

An active client connects to the websocket, which allows for realtime updates, listening parties and live chat.
Information is fetched via the HTTP protocol, using JSON for potential message bodies.
The actual streaming protocol is HLS and is already implemented by JavaFX's Media class.

The current view is periodically updated to reflect changes in the database (like the number of followers of a playlist
or an arist or the number of saves of a track in the favourites).

### Server

The server is made of two components: the HTTP Server and the websocket.

The HTTP server acts as a medium between the client and the database and has direct access to the audio and image files
required by the client application.
A config file should specify the database and the file folder locations.

The websocket keeps track of active users (what they're listening to), managing listening parties and live chats.

### Database

The database stores information about music, artists and users, as well as references to audio and image files present
on the server.

It's SQLite database, since it's for a small university project.

A special tool is used to query the database and sync it with the audio/image files on the server.

<!-- TODO: insert database schema -->