package it.unibs.pajc.lithium.gui.controllers.listEntries;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Item;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.lithium.managers.PartyManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class UserEntry extends ItemEntry {
    @FXML
    private ImageView userProPicImg;
    @FXML
    private Label usernameLbl;
    @FXML
    private Label roleLbl;

    public UserEntry(Item item) {
        super(item);

        userProPicImg.setImage(ItemProvider.getImage(getUser().getProfilePicPath()));
        usernameLbl.setText(getUser().getUsername());
        roleLbl.setText("");

        PartyManager.hostUpdate.addListener(id -> Platform.runLater(() -> update(id)));
    }

    public void update(int hostId) {
        roleLbl.setText(Objects.equals(hostId, getUser().getId()) ? "Host" : "Member");
    }

    private User getUser() {
        return (User) item;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/userEntry.fxml";
    }
}
