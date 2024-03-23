package it.unibs.pajc.lithium;

import it.unibs.pajc.HttpHandler;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.lithium.gui.AlertUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class AccountManager {
    private final static String LOGIN_INFO_PATH = "login.lit";
    private static User user;

    public record LoginInfo(String username, String passwordHash) implements Serializable {
    }

    public static boolean userExists(String username) {
        var response = HttpHandler.get("user/exists?username=" + username);
        return Boolean.parseBoolean(response);
    }

    public static void registerUser(String username, String pswHash) {
        HttpHandler.post("user/register", username + "," + pswHash);
    }

    public static boolean authenticateUser(String username, String pswHash) {
        return Boolean.parseBoolean(HttpHandler.post("user/auth", username + "," + pswHash));
    }

    public static User getUser() {
        // TODO: this is temp
        if (user == null) user = ItemProvider.getItem(1, User.class);
        return user;
    }

    public static void saveLoginInfo(String username, String password) {
        try {
            var file = new File(LOGIN_INFO_PATH);
            var ignored = file.createNewFile();
            var stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(new LoginInfo(username, password));
        } catch (IOException e) {
            AlertUtil.showErrorAlert(e);
            e.printStackTrace();
        }
    }

    public static LoginInfo getLoginInfo() {
        try {
            var file = new File(LOGIN_INFO_PATH);
            if (!file.exists()) return null;
            var stream = new ObjectInputStream(new FileInputStream(file));
            return (LoginInfo) stream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            AlertUtil.showErrorAlert(e);
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteLoginInfo() {
        try {
            Files.deleteIfExists(Path.of(LOGIN_INFO_PATH));
        } catch (IOException e) {
            AlertUtil.showErrorAlert(e);
            e.printStackTrace();
        }
    }
}
