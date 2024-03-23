package it.unibs.pajc.lithium;

import it.unibs.pajc.lithium.gui.AlertUtil;

import java.io.FileInputStream;
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
            e.printStackTrace();
            AlertUtil.showErrorAlert(e);
        }
    }

    public static String getServerUrl() {
        return properties.getProperty("server_url", "http://localhost:8080");
    }

    private Config() {
    }
}