# TCP protocol specification

Every message is made of a command and a body, separated by `;;`

Message: `command;;body`

### Legend

`|`: or

## Client

The messages that the client can receive.

#### error

There was an error on the server (probably due to a client message not well formatted).

Body: `errorMessage`

## Server

The messages that the server can receive.

#### hello

Just for testing. The server sends back a generic message `"world"`

Body: `ignored` (but it must not be empty)

#### auth

Used to authenticate the user (independent of HTTP).

Body: `username::passwordHash`

`username` is the name of the user
`passwordHash` is the Sha256 codification of the user password.

### Party commands

`partyId`: the id of a party

`""`: string literal

#### joinParty

Ask to join a party. If there's no party with the id in the body, a new one is created

Body: `partyId` `|` `"new"`

`"new"` is used to create a new party.

#### leaveParty

Asks to leave a party. If the connection is the last one of the party, the party is then deleted. If the connection was
the host, a new host is chosen arbitrarily.

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

#### partyChat

The server broadcasts a user string message to all the other members of the party.

Body: `partyId::message`

`message` is the message to send. It must not contain the sequence `;;` or `::`, or the server will not be able to
interpret it.