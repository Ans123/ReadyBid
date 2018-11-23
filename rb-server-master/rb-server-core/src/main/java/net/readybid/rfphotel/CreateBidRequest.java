package net.readybid.rfphotel;


import net.readybid.validators.Id;

import javax.validation.constraints.NotNull;

/**
 * Created by DejanK on 1/17/2017.
 *
 */
public class CreateBidRequest {

    @NotNull
    @Id
    public String hotelId;

    @NotNull
    @Id
    public String travelDestinationId;
}
