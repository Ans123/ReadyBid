package net.readybid.app.entities.rfp_hotel;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.Location;
import net.readybid.entities.Id;

public class HotelRfpContactAccount {

    public Id accountId;

    public Id entityId;

    public EntityType type;

    public String name;

    public String website;

    public String emailAddress;

    public String phone;

    public String logo;

    public Location location;
}
