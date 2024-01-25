package it.unibs.pajc.lithium.db;

public abstract class DBInterface {
    public abstract boolean authenticateUser(String username, String passwordHash);
}
