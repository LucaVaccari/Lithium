# TODOs

## Client

- [ ] Client interface
    - [x] Login screen
    - [ ] Music play view
        - [ ] Currently playing track
        - [ ] Queue
    - [ ] Library tab
    - [ ] Search tab
        - [x] Show default items
        - [x] Search query
        - [ ] Right click context menus
    - [ ] Settings tab
        - [ ] Server address
        - [ ] Change theme
        - [ ] Equalizer
        - [ ] Log out
        - [ ] Save settings in file
    - [ ] detail views
        - [ ] track details
        - [ ] album details
        - [ ] artist details
        - [ ] playlist details
- [ ] Theme (AtlantaFX)
- [ ] Save login info

## Server

- [ ] Server config file (server.properties)
    - [ ] Server info (port)
    - [ ] Db info (db url, root directory for files)
- [ ] Server database integration
    - [x] Write object mappers
    - [ ] Implement CRUD methods
        - [ ] Create
        - [ ] Read
        - [ ] Update
        - [ ] Delete
- [x] Interface for managing the db
- [ ] Define and implement HTTP contexts
    - [x] Client authentication
    - [ ] Search queries
    - [ ] Streaming request

## Database

- [x] define db structure
- [x] write sql file for db creation
- [x] write queries for filling the db with test samples
