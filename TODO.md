# TODOs

## Client

- [ ] Client interface
    - [x] Login screen
    - [x] Music play view
        - [x] Currently playing track
        - [x] Queue
    - [x] Library tab
    - [ ] Search tab
        - [x] Show default items
        - [x] Search query
        - [ ] Right click context menus
    - [ ] Settings tab
        - [ ] Server address
        - [x] Log out
        - [ ] Save settings in file
    - [ ] detail views
        - [x] track details
            -  [x] add to playlist btn
        - [x] album details
            - [ ] track number
        - [ ] artist details
        - [x] playlist details
            - [x] manage playlist btn
        - [x] playlist management
    - [ ] Playback controls
        - [x] song title
        - [x] back, pause, forward
        - [x] slider
        - [x] timestamp
        - [ ] volume slider
- [x] Save login info
- [x] Cache items and images
- [ ] Tooltips
- [ ] Async loading
- [ ] Theme (AtlantaFX)

## Server

- [ ] Server config file (server.properties)
    - [ ] Server info (port)
    - [ ] Db info (db url, root directory for files)
- [x] Server database integration
    - [x] Write object mappers
    - [x] Implement CRUD methods
        - [x] Create
        - [x] Read
        - [x] Update
        - [x] Delete
- [ ] Interface for managing the db
- [x] Define and implement HTTP contexts
    - [x] Client authentication
    - [x] Search queries
    - [x] Streaming request
    - [x] User save item
    - [x] Add to playlist
- [ ] online user management
    - [ ] create websocket

## Database

- [x] define db structure
- [x] write sql file for db creation
- [x] write queries for filling the db with test samples
