package net.readybid.api.rfp_hotel.bid.supplier.contact;

import net.readybid.validators.Id;

import javax.validation.constraints.NotBlank;

public class SetHotelRfpSupplierContactWebRequest {

    @NotBlank
    @Id
    public String userAccountId;
}
