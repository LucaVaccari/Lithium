package it.unibs.pajc.lithium.gui.controllers.listEntries;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Item;
import it.unibs.pajc.lithium.db.om.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class UserEntry extends ItemEntry {
    @FXML
    private ImageView userProPicImg;
    @FXML
    private Label usernameLbl;

    protected UserEntry(Item item) {
        super(item);

        userProPicImg.setImage(ItemProvider.getImage(getUser().getProfilePicPath()));
        usernameLbl.setText(getUser().getUsername());
    }

    private User getUser() {
        return (User) item;
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/userEntry.fxml";
    }
}
