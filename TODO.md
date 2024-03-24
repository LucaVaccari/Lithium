# TODOs

## Client

- [ ] Client interface
    - [x] Login screen
    - [ ] Music play view
        - [x] Currently playing track
        - [ ] Queue
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
        - [ ] track details
            -  [ ] add to playlist btn
        - [x] album details
        - [ ] artist details
        - [ ] playlist details
            - [ ] manage playlist btn
        - [ ] playlist management
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
- [ ] Server database integration
    - [x] Write object mappers
    - [ ] Implement CRUD methods
        - [x] Create
        - [x] Read
        - [ ] Update
        - [x] Delete
    - [ ] create entries in relationship tables
- [ ] Interface for managing the db
- [ ] Define and implement HTTP contexts
    - [x] Client authentication
    - [x] Search queries
    - [x] Streaming request
    - [x] User save item
    - [ ] Add to playlist

## Database

- [x] define db structure
- [x] write sql file for db creation
- [x] write queries for filling the db with test samples
