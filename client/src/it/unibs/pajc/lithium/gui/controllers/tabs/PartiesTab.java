package it.unibs.pajc.lithium.gui.controllers.tabs;

import it.unibs.pajc.lithium.gui.CustomComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


public class PartiesTab extends CustomComponent {
    @FXML
    private Pane inPartyView;
    @FXML
    private Pane outOfPartyView;
    @FXML
    private Pane userPane;
    @FXML
    private ListView<Label> messagesListView;
    @FXML
    private TextField messageTxtField;
    @FXML
    private Button sendBtn;

    @FXML
    private void initialize() {
        inPartyView.setVisible(false);
        outOfPartyView.setVisible(true);

        messageTxtField.setOnAction(this::sendMsg);
        sendBtn.setOnAction(this::sendMsg);
    }

    private void sendMsg(ActionEvent ignored) {
        // TODO send msg
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/mainTabs/partiesTab.fxml";
    }
}
