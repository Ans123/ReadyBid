package net.readybid.bidmanagerview;

import net.readybid.entities.Id;

public class HotelRfpCreateChainRfpBidManagerViewCommand {
    public final String name;
    public final Id rfpId;
    public final Id accountId;

    public HotelRfpCreateChainRfpBidManagerViewCommand(String name, Id rfpId, Id accountId) {
        this.name = name;
        this.rfpId = rfpId;
        this.accountId = accountId;
    }
}
