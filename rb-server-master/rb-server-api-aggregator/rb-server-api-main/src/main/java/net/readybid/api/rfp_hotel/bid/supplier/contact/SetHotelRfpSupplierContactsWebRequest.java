package net.readybid.api.rfp_hotel.bid.supplier.contact;

import net.readybid.validators.Id;
import net.readybid.validators.Ids;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class SetHotelRfpSupplierContactsWebRequest {

    @NotBlank
    @Id
    public String userAccountId;

    @NotNull
    @Ids
    public List<String> bids;
}
