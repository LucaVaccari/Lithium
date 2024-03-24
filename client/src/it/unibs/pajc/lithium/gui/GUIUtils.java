package it.unibs.pajc.lithium.gui;

import it.unibs.pajc.lithium.ItemProvider;
import it.unibs.pajc.lithium.db.om.Item;
import it.unibs.pajc.lithium.db.om.Track;
import it.unibs.pajc.lithium.gui.controllers.listEntries.ItemEntry;
import it.unibs.pajc.lithium.gui.controllers.listEntries.TrackEntry;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public final class GUIUtils {
    public static <T extends ItemEntry> void fillEntryList(ListView<T> listView, Item[] list, Class<T> entryClass) {
        try {
            for (var item : list) {
                if (listView.getItems().filtered(a -> a.getItem().equals(item)).isEmpty()) {
                    listView.getItems().add(entryClass.getConstructor(Item.class).newInstance(item));
                }
            }
            listView.getItems().removeIf(albumEntry -> !Arrays.stream(list).toList().contains(albumEntry.getItem()));
            listView.refresh();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            System.err.println("ERROR in entry utility");
            e.printStackTrace();
        }
    }

    public static Track[] fillTrackContainerAndGenreLabel(Integer[] ids, ListView<TrackEntry> trackContainer,
                                                          Label genreLbl) {
        var tracks = ItemProvider.getItems(ids, Track.class);
        trackContainer.getItems().clear();
        for (var track : tracks) {
            trackContainer.getItems().add(new TrackEntry(track));
        }

        var genresIds =
                Arrays.stream(tracks).flatMap(track -> Arrays.stream(track.getGenresIds())).toArray(Integer[]::new);
        genreLbl.setText(ItemProvider.getGenresFormatted(genresIds));
        return tracks;
    }

    private GUIUtils() {
    }
}
