package net.readybid.entities.hotel.core;

import net.readybid.app.core.entities.entity.hotel.HotelCategory;

/**
 * Created by DejanK on 1/12/2017.
 *
 */
public class HotelCategoryImpl implements HotelCategory {

    private int id;
    private String label;

    public String getLabel() {
        return label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLabel(String name) {
        this.label = name;
    }
}
