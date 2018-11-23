package net.readybid.app.interactors.rfp_hotel.implementation;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.core.entity.gate.HotelChainLoader;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidAccountStorageManager;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpDefaultResponseStorageManager;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpStorageManager;
import net.readybid.auth.account.core.Account;
import net.readybid.entities.chain.HotelChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class HotelRfpAccountModificationService {

    private final HotelChainLoader chainLoader;
    private final HotelRfpStorageManager rfpStorageManager;
    private final HotelRfpBidAccountStorageManager bidStorageManager;
    private final HotelRfpDefaultResponseStorageManager responseStorageManager;

    @Autowired
    HotelRfpAccountModificationService(
            HotelChainLoader chainLoader,
            HotelRfpStorageManager rfpStorageManager,
            HotelRfpBidAccountStorageManager bidStorageManager,
            HotelRfpDefaultResponseStorageManager responseStorageManager) {
        this.chainLoader = chainLoader;
        this.rfpStorageManager = rfpStorageManager;
        this.bidStorageManager = bidStorageManager;
        this.responseStorageManager = responseStorageManager;
    }

    void updateAccountLogo(Account account) {
        final String accountId = String.valueOf(account.getId());
        final EntityType type= account.getType();
        final String logo = account.getLogo();

        switch (type){
            case HOTEL:
            case CHAIN:
            case HMC:
                break;

            case TRAVEL_AGENCY:
            case TRAVEL_CONSULTANCY:
            case COMPANY:
            default:
                rfpStorageManager.updateBuyerLogo(accountId, logo);
                bidStorageManager.updateBuyerLogo(accountId, logo);
                break;
        }
    }

    void updateEntityBasicInformation(Entity entity, Account account) {
        switch (entity.getType()){
            case HOTEL:
                responseStorageManager.updateHotelData(entity);
                bidStorageManager.updateHotelBasicInformation(entity, account);
                break;
            case CHAIN:
                final HotelChain chain = chainLoader.load(String.valueOf(entity.getId()));
                if(chain.isMasterChain())
                    bidStorageManager.updateHotelMasterChainBasicInformation(chain, account);
                else
                    bidStorageManager.updateHotelBrandChainBasicInformation(chain, account);
                break;
            case HMC:
                bidStorageManager.updateHotelManagementCompanyBasicInformation(account);
                break;
            case TRAVEL_AGENCY:
            case TRAVEL_CONSULTANCY:
            case COMPANY:
            default:
                rfpStorageManager.updateBuyerBasicInformation(account);
                bidStorageManager.updateBuyerBasicInformation(account);
                break;
        }
    }
}
