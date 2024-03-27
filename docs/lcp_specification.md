# LCP specification

Every message is made of a command and a body, separated by `;;`

Message: `command;;body`

### Legend

`|`: or

`""`: string literal

## Client

The messages that the client can receive.

#### partyId

Received after a 'create' request, it communicates the id of the newly created party.

Body: `partyId` (int)

#### partySync

Used to communicate the client the timestamp of the currently playing track in the party.

Body: `timestamp` (double)

#### partyChat

Received when another user sends a chat message.

Body: `userId::message` (string)

#### partyTrack

Received when the party track has changed.

Body: `trackId` (int)

#### pause

Received when the host paused/unpaused the playback.

Body: `pause` `|` `unpause`

#### host

It communicates the host of the party.

Body: `userId` (int)

`userId` is the id of the new host

#### userUpdate

Contains a list of all users in a specific party. The message is sent only to the members of a party.

Body: `userId1::userId2::userid3...` (list of int)

#### allParties

Contains a list of all available parties.

Body: `partyId1,,trackId1,,ownerId1::partyId2,,trackId2,,ownerId2::partyId3,,...|"null"` (list of int)

`"null"` indicates that there are no parties.

#### error

There was an error on the server (probably due to a client message not well formatted).

Body: `errorMessage` (string)

## Server

The messages that the server can receive.

#### hello

Just for testing. The server sends back a generic message `"world"`

Body: `ignored` (but it must not be empty)

#### auth

Used to authenticate the user (independent of HTTP).

Body: `username::passwordHash|"logout"`

`username` is the name of the user

`passwordHash` is the Sha256 codification of the user password.

`"logout"` is used to log out.

### Party commands

`partyId`: the id of a party

#### joinParty

Ask to join a party.
If there's no party with the id in the body, a new one is created.
A userUpdate is sent when a user joins.

Body: `partyId` `|` `"new"`

`"new"` is used to create a new party.

#### leaveParty

Asks to leave a party.
If the connection is the last one of the party, the party is then deleted.
If the connection was the host, a new host is chosen arbitrarily.
A userUpdate is sent when a user leaves.

Body: `partyId`

#### syncParty

Used to set a new timestamp for currently playing track of the party.
Only the host of the party can send this message.

Body: `partyId::timestamp`

`timestamp` is the new timestamp

#### partyTrack

Used to update the currently playing track in the party.
Only the host of the party can send this message.

Body: `partyId::trackId`

`trackId` is the id of the new track

#### pause

Received when the host paused/unpaused the playback.

Body: `partyId::pause|unpause`

#### partyChat

The server broadcasts a user string message to all the other members of the party.

Body: `partyId::message`

`message` is the message to send. It must not contain the sequence `;;` or `::`, or the server will not be able to
interpret it.

#### allParties

A request to get the id of all the parties.

Body: `ignored` (but must not be empty)