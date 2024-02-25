package it.unibs.pajc.lithium.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.LinkedList;
import java.util.Optional;

/**
 * Provides methods for showing different types of dialogs and alert windows. Cannot be instantiated.
 */
public final class AlertUtil {
    private static final LinkedList<Alert> alertQueue = new LinkedList<>();

    /**
     * Private constructor to prevent instantiating
     */
    private AlertUtil() {
    }

    private static void showGenericAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alertQueue.add(alert);
        pollAlerts();
    }

    /**
     * Shows a dialog with a text input
     *
     * @param defaultValue The value which is already written when the dialogue opens
     * @param title        The title of the dialogue (in the top-center of the window)
     * @param headerText   The header, the bigger message
     * @param contentText  The text near the input field (at his left)
     * @return An optional string possibly containing the content of the input field
     */
    public static Optional<String> showTextInputDialogue(String defaultValue, String title, String headerText, String contentText) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        return dialog.showAndWait();
    }

    /**
     * Shows a dialog with "Cancel" and "Ok" options
     *
     * @param title       The title of the dialogue (in the top-center of the window)
     * @param headerText  The header, the bigger message
     * @param contentText The content of the message
     * @return The result of the operation
     */
    public static Optional<ButtonType> showConfirmationAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    /**
     * Shows a simple dialog message not interactive (with an ok button to close)
     *
     * @param title   The title of the dialogue (in the top-center of the window)
     * @param header  The header, the bigger message
     * @param content The content of the message
     */
    public static void showInformationAlert(String title, String header, String content) {
        showGenericAlert(Alert.AlertType.INFORMATION, title, header, content);
    }

    /**
     * Shows an error dialog, similar to the information, but with an error sign
     *
     * @param title   The title of the dialogue (in the top-center of the window)
     * @param header  The header, the bigger message
     * @param content The content of the message
     */
    public static void showErrorAlert(String title, String header, String content) {
        showGenericAlert(Alert.AlertType.ERROR, title, header, content);
    }

    /**
     * Show the open alerts and removes them from the queue
     */
    public static void pollAlerts() {
        if (alertQueue.isEmpty()) return;

        Alert lastAlert = alertQueue.peek();

        if (!lastAlert.isShowing()) {
            lastAlert.showAndWait();
            alertQueue.poll();
            pollAlerts();
        }
    }
}