package net.readybid.app.interactors.rfp_hotel.gate;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.auth.account.core.Account;
import net.readybid.entities.chain.HotelChain;

public interface HotelRfpBidAccountStorageManager {
    void updateBuyerLogo(String accountId, String logo);

    void updateHotelBasicInformation(Entity entity, Account account);

    void updateHotelMasterChainBasicInformation(HotelChain chain, Account account);

    void updateHotelBrandChainBasicInformation(HotelChain chain, Account account);

    void updateHotelManagementCompanyBasicInformation(Account account);

    void updateBuyerBasicInformation(Account account);
}
