package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.entities.rfp.RateLoadingInformationList;

public interface RateLoadingInformationTableFactory {
    String create(RateLoadingInformationList rateLoadingInformationList);
}
