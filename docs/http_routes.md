# HTTP routes

## GET

### `/user/exists`

Returns `true` or `false` in the body if the user exists or not.

#### parameters

- `username`

### `/user`

Gets users encoded as JSON.

### `/track`

Gets users encoded as JSON.

### `/artist`

Gets artists encoded as JSON.

### `/album`

Gets albums encoded as JSON.

### `/playlist`

Gets playlists encoded as JSON.

### `/genre`

Gets genres encoded as JSON.

### `/img/:path`

Gets an image.

### `/audio/:path`

Gets an audio m3u8 playlist for streaming or aac files directly, depending on the path specified..

## POST

### `/user/auth`

Tries to authenticate a user.

#### body

`username,pswHash`

`pswHash` is encoded with sha256.

### `/user/register`

Registers a new user and returns its id in the response body.

#### body

`username,pswHash`

`pswHash` is encoded with sha256.

### `/user/save/album`

Saves an album for a user.

#### params

- `userId`
- `albumId`

### `/user/save/artist`

Saves an artist for a user.

#### params

- `userId`
- `artistId`

### `/user/save/playlist`

Saves a playlist for a user.

#### params

- `userId`
- `playlistId`

### `/playlist`

Creates a playlist.

### `/playlist/add`

Adds a track to a playlist.

## PUT

### `/playlist`

Updates playlist information.

## DELETE

### `/user/save/album`

Removes an album from the ones saved by a user.

### `/user/save/artist`

Removes an artist from the ones followed by a user.

### `/user/save/playlist`

Removes a playlist from the ones saved by a user.

### `/playlist/add`

Removes a track from a playlist.