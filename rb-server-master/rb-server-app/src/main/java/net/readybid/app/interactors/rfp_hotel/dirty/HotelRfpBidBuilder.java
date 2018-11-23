package net.readybid.app.interactors.rfp_hotel.dirty;

import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.useraccount.UserAccount;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by DejanK on 4/9/2017.
 *
 */
public class HotelRfpBidBuilder {

    private Account account;
    private Hotel hotel;
    private HotelRfpDefaultResponse defaultResponse;
    private List<? extends UserAccount> userAccounts;

    public HotelRfpBidBuilder() {}

    public HotelRfpBidBuilder(Account account, Hotel hotel, HotelRfpDefaultResponse defaultResponse) {
        this.account = account;
        this.hotel = hotel;
        this.defaultResponse = defaultResponse;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public void setUserAccounts(List<? extends UserAccount> userAccounts) {
        this.userAccounts = userAccounts;
    }

    public UserAccount getSupplierContact(){
        UserAccount bidRecipient = null;

        final ObjectId primaryRepresentativeUserAccountId = account.getPrimaryRepresentativeUserAccountId();
        if(primaryRepresentativeUserAccountId != null){
            for(UserAccount userAccount : userAccounts){
                if(primaryRepresentativeUserAccountId.equals(userAccount.getId())){
                    bidRecipient = userAccount;
                    break;
                }
            }
        } else if(userAccounts != null && !userAccounts.isEmpty()){
            for(UserAccount userAccount : userAccounts){
                if(userAccount.isActive()){
                    bidRecipient = userAccount;
                    break;
                }
                if(userAccount.isUserActive()){
                    bidRecipient = userAccount;
                }
            }
        }

        return bidRecipient;
    }

    public ObjectId getAccountId() {
        return account == null ? null : account.getId();
    }

    public void setDefaultResponse(HotelRfpDefaultResponse defaultResponse) {
        this.defaultResponse = defaultResponse;
    }

    public HotelRfpDefaultResponse getDefaultResponse() {
        return defaultResponse;
    }
}
