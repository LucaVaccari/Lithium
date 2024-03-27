package it.unibs.pajc.lithium.gui.controllers.tabs;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.gui.controllers.listEntries.PartyEntry;
import it.unibs.pajc.lithium.managers.PartyManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.Set;


public class PartiesTab extends CustomComponent {
    @FXML
    private Parent inPartyView;
    @FXML
    private Parent outOfPartyView;
    @FXML
    private Pane allPartiesPane;
    @FXML
    private Button createPartyBtn;
    @FXML
    private Button leavePartyBtn;
    @FXML
    private Pane usersPane;
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
        leavePartyBtn.setOnAction(e -> {
            PartyManager.leaveParty();
            reset();
        });
        messageTxtField.setOnAction(this::sendMsg);
        sendBtn.setOnAction(this::sendMsg);

        PartyManager.partyJoined.addListener(id -> {
            if (id == -1) reset();
            else {
                inPartyView.setVisible(true);
                outOfPartyView.setVisible(false);
            }
        });
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
        usersPane.getChildren().clear();
    }

    private void fillActiveParties(Set<Integer[]> parties) {
        // [0]: partyId, [1]: trackId, [2]: ownerId
        for (Integer[] party : parties) {
            var partyId = party[0];
            var track = ItemProvider.getItem(party[1], Track.class);
            var owner = ItemProvider.getItem(party[2], User.class);
            Platform.runLater(() -> allPartiesPane.getChildren().add(new PartyEntry(owner, track, partyId)));
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
