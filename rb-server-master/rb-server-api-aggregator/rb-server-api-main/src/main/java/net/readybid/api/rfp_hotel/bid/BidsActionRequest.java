package net.readybid.api.rfp_hotel.bid;

import net.readybid.validators.Ids;

import javax.validation.constraints.NotNull;
import java.util.List;

public class BidsActionRequest {

    @NotNull
    @Ids
    public List<String> bids;
}
