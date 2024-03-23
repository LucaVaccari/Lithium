package it.unibs.pajc.lithium.gui.controllers.listEntries;

import it.unibs.pajc.lithium.db.om.Item;
import it.unibs.pajc.lithium.gui.CustomComponent;

import java.util.Objects;

public abstract class ItemEntry extends CustomComponent {
    protected Item item;

    protected ItemEntry(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (ItemEntry) o;
        return Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }
}
