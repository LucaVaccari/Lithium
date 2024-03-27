package it.unibs.pajc.lithium.managers;

import it.unibs.pajc.lithium.ClientMain;
import it.unibs.pajc.lithium.HttpHandler;
import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.lithium.gui.AlertUtil;
import it.unibs.pajc.util.Observer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class AccountManager {
    private final static String LOGIN_INFO_PATH = "login.lit";
    private static User user;
    public final static Observer<User> userUpdated = new Observer<>();

    public record LoginInfo(String username, String passwordHash) implements Serializable {
    }

    public static boolean userExists(String username) {
        var response = HttpHandler.get("user/exists?username=" + username);
        return Boolean.parseBoolean(response);
    }

    public static void registerUser(String username, String pswHash) {
        System.out.println(HttpHandler.post("user/register", username + "," + pswHash));
    }

    public static boolean authenticateUser(String username, String pswHash) {
        int userId = Integer.parseInt(HttpHandler.post("user/auth", username + "," + pswHash));
        ClientMain.getConnectionManager().writeMessage("auth;;%s::%s".formatted(username, pswHash));
        if (userId != -1) user = ItemProvider.getItem(userId, User.class);
        return userId != -1;
    }

    public static boolean authenticateUserFromSavedInfo() {
        var info = getLoginInfo();
        if (info == null) return false;
        return authenticateUser(info.username, info.passwordHash);
    }

    public static void updateUser() {
        if (user == null) return;
        user = ItemProvider.getItem(user.getId(), User.class, true);
        userUpdated.invoke(user);
    }

    public static void saveLoginInfo(String username, String password) {
        try {
            var file = new File(LOGIN_INFO_PATH);
            var ignored = file.createNewFile();
            var stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(new LoginInfo(username, password));
        } catch (IOException e) {
            AlertUtil.showErrorAlert(e);
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
            return null;
        }
    }

    public static void deleteLoginInfo() {
        try {
            Files.deleteIfExists(Path.of(LOGIN_INFO_PATH));
            ClientMain.getConnectionManager().writeMessage("auth;;logout");
        } catch (IOException e) {
            AlertUtil.showErrorAlert(e);
        }
    }

    public static User getUser() {
        return user;
    }
}
