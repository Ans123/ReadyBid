package net.readybid.api.rfp_hotel.bid.supplier.contact;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.validators.RbEmail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateHotelRfpSupplierContactWebRequest {

    @NotBlank
    @Pattern(regexp="^CHAIN|HOTEL$")
    public String accountType;

    @NotBlank
    @Size(max = 50)
    public String firstName;

    @NotBlank
    @Size(max = 50)
    public String lastName;

    @NotBlank
    @RbEmail
    public String emailAddress;

    @Size(max = 20)
    public String phone;

    @Size(max = 100)
    public String jobTitle;

    CreateHotelRfpSupplierContactRequest toSupplierContact() {
        final CreateHotelRfpSupplierContactRequest supplierContact = new CreateHotelRfpSupplierContactRequest();

        supplierContact.type = EntityType.valueOf(accountType);
        supplierContact.firstName = firstName;
        supplierContact.lastName = lastName;
        supplierContact.emailAddress = emailAddress.toLowerCase();
        supplierContact.phone = phone;
        supplierContact.jobTitle = jobTitle;

        return supplierContact;
    }
}
