package net.readybid.app.entities.rfp_hotel;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.entities.Id;
import net.readybid.exceptions.UnableToCompleteRequestException;

public class HotelRfpSupplierContact {

    public Id id;

    public String firstName;

    public String lastName;

    public String emailAddress;

    public String phone;

    public String jobTitle;

    public boolean isUser;

    public String profilePicture;

    public HotelRfpContactAccount company;

    public String getFullName() {
        return String.format("%s %s", firstName, lastName).trim();
    }

    public EntityType getType() {
        if(company == null) throw new UnableToCompleteRequestException("Supplier Contact Account may not be null: " + id);
        return company.type;
    }
}
