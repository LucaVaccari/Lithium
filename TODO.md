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
    - [x] detail views
        - [x] track details
        - [x] album details
        - [ ] artist details
        - [x] playlist details
- [ ] Save login info
- [x] Cache items
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
        - [ ] Create
        - [x] Read
        - [ ] Update
        - [ ] Delete
    - [ ] create entries in relationship tables
- [ ] Interface for managing the db
- [ ] Define and implement HTTP contexts
    - [x] Client authentication
    - [x] Search queries
    - [x] Streaming request
    - [ ] User save item

## Database

- [x] define db structure
- [x] write sql file for db creation
- [x] write queries for filling the db with test samples
