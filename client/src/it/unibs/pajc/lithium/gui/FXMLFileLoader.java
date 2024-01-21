package it.unibs.pajc.lithium.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

/**
 * FXML file loader helper class
 */
public class FXMLFileLoader
{
    /**
     * Load an FXML file and returns it root element
     *
     * @param path   The path of the FXML file
     * @param caller The object that calls the method (should be this)
     * @return The root element of the FXML
     */
    public static Parent loadFXML(String path, Object caller)
    {
        FXMLLoader loader = new FXMLLoader();
        URL location = caller.getClass().getResource(path);
        if (location == null)
            System.out.println("Location is null for: " + path);
        loader.setLocation(location);
        try
        {
            return loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get and FXMLLoader from a path
     *
     * @param path The path of the FXML file
     * @return The FXMLLoader, which has the location set to the path
     */
    public static FXMLLoader getLoader(String path)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLFileLoader.class.getResource(path));
        return loader;
    }
}
