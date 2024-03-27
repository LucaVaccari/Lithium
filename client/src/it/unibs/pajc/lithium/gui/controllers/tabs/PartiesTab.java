package it.unibs.pajc.lithium.gui.controllers.tabs;

import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.managers.PartyManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.Set;


public class PartiesTab extends CustomComponent {
    @FXML
    private Pane inPartyView;
    @FXML
    private Pane outOfPartyView;
    @FXML
    private Pane allPartiesPane;
    @FXML
    private Button createPartyBtn;
    @FXML
    private Button leavePartyBtn;
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
        reset();
        PartyManager.requestAllParties();

        createPartyBtn.setOnAction(e -> PartyManager.createParty());
        messageTxtField.setOnAction(this::sendMsg);
        sendBtn.setOnAction(this::sendMsg);

        PartyManager.messageReceived.addListener(this::receiveMessage);
        PartyManager.partiesUpdate.addListener(this::fillActiveParties);
        // todo join party btn
        // todo show users in party
    }

    private void reset() {
        inPartyView.setVisible(false);
        outOfPartyView.setVisible(true);
        messagesListView.getItems().clear();
        messageTxtField.setText("");
        userPane.getChildren().clear();
    }

    private void fillActiveParties(Set<Integer[]> parties) {
        // todo show active parties
        for (Integer[] party : parties) {
            System.out.println(Arrays.toString(party));
        }
    }

    private void sendMsg(ActionEvent ignored) {
        var msg = messageTxtField.getText();
        PartyManager.sendPartyChat(msg);
        messageTxtField.setText("");
        receiveMessage(msg);
    }

    private void receiveMessage(String msg) {
        var lbl = new Label(msg);
        messagesListView.getItems().add(lbl);
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/mainTabs/partiesTab.fxml";
    }
}
