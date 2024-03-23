package it.unibs.pajc.lithium.gui.controllers.listEntries;

import it.unibs.pajc.lithium.db.om.Item;
import javafx.scene.control.ListView;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public final class EntryUtility {
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

    private EntryUtility() {
    }
}
