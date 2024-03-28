package it.unibs.pajc.lithium.gui.controllers.listEntries;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Album;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.db.om.User;
import it.unibs.pajc.lithium.gui.CustomComponent;
import it.unibs.pajc.lithium.managers.PartyManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PartyEntry extends CustomComponent {
    @FXML
    private ImageView userProPicImg;
    @FXML
    private Label usernameLbl;
    @FXML
    private Label artistLbl;
    @FXML
    private Label trackLbl;
    @FXML
    private ImageView albumCoverImg;
    @FXML
    private Button joinBtn;

    public PartyEntry(User user, Track track, int partyId) {
        super();
        userProPicImg.setImage(ItemProvider.getImage(user.getProfilePicPath()));
        usernameLbl.setText(user.getUsername());
        if (track != null) {
            artistLbl.setText(ItemProvider.getArtistNamesFormatted(track.getArtistIds()));
            trackLbl.setText(track.getTitle());
            albumCoverImg.setImage(
                    ItemProvider.getImage(ItemProvider.getItem(track.getAlbumId(), Album.class).getImgPath()));
        }
        joinBtn.setOnAction(e -> PartyManager.sendJoin(partyId));
    }

    @Override
    protected String fxmlPath() {
        return "/FXMLs/listComponents/partyEntry.fxml";
    }
}
