package net.readybid.app.interactors.rfp_hotel.implementation;

import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidUserStorageManager;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpStorageManager;
import net.readybid.auth.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class HotelRfpUserModificationService {

    private final HotelRfpStorageManager rfpStorageManager;
    private final HotelRfpBidUserStorageManager bidStorageManager;

    @Autowired
    HotelRfpUserModificationService(
            HotelRfpStorageManager rfpStorageManager,
            HotelRfpBidUserStorageManager bidStorageManager
    ) {
        this.rfpStorageManager = rfpStorageManager;
        this.bidStorageManager = bidStorageManager;
    }

    void handleUserDetailsChange(User user) {
        rfpStorageManager.updateUserBasicDetails(user);
        bidStorageManager.updateUserBasicDetails(user);
    }

    void handleUserEmailAddressChange(User user) {
        rfpStorageManager.updateUserEmailAddress(user);
        bidStorageManager.updateUserEmailAddress(user);
    }

    void handleUserProfilePictureChange(User user) {
        rfpStorageManager.updateUserProfilePicture(user);
        bidStorageManager.updateUserProfilePicture(user);
    }
}
