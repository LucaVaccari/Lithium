package it.unibs.pajc.lithium;

import it.unibs.pajc.lithium.gui.AlertUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class Config {
    private final static String CONFIG_URL = "lithium.properties";

    private final static Properties properties = new Properties();

    static {
        try {
            if (Files.exists(Path.of(CONFIG_URL))) properties.load(new FileInputStream(CONFIG_URL));
        } catch (IOException e) {
            AlertUtil.showErrorAlert(e);
        }
    }

    public static String getServerHttpUrl() {
        return properties.getProperty("server_http_url", "localhost");
    }

    public static int getServerHttpPort() {
        return Integer.parseInt(properties.getProperty("server_http_port", "8080"));
    }

    public static String getServerLcpUrl() {
        return properties.getProperty("server_lcp_url", "localhost");
    }

    public static int getServerLcpPort() {
        return Integer.parseInt(properties.getProperty("server_lcp_port", "1234"));
    }

    public static void setServerHttpUrl(String value) {
        properties.setProperty("server_http_url", value);
        save();
    }

    public static void setServerHttpPort(int newValue) {
        if (newValue < 0 || newValue > 65535) return;
        properties.setProperty("server_http_port", String.valueOf(newValue));
        save();
    }

    public static void setServerLcpUrl(String value) {
        properties.setProperty("server_lcp_url", value);
        save();
    }

    public static void setServerLcpPort(int newValue) {
        if (newValue < 0 || newValue > 65535) return;
        properties.setProperty("server_lcp_port", String.valueOf(newValue));
        save();
    }

    private static void save() {
        try {
            properties.store(new FileOutputStream(CONFIG_URL), "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Config() {
    }
}