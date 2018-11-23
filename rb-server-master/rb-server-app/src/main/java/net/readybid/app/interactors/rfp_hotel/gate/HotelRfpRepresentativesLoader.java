package net.readybid.app.interactors.rfp_hotel.gate;

import net.readybid.app.entities.rfp_hotel.HotelRfpRepresentative;

import java.util.List;

public interface HotelRfpRepresentativesLoader {
    List<String> getHotelAndMasterChainAccountIdsForHotel(String hotelEntityId);

    String getAccountIdForEntity(String chainEntityId);

    List<HotelRfpRepresentative> getRepresentativesFromAccounts(List<String> accountsIds);

    List<HotelRfpRepresentative> getSupplierContactsFromBidsWithAccounts(List<String> accountsIds);
}
