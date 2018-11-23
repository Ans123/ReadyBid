package net.readybid.app.interactors.rfp_hotel.gate;

import net.readybid.auth.user.User;

public interface HotelRfpBidUserStorageManager {
    void updateUserBasicDetails(User user);

    void updateUserEmailAddress(User user);

    void updateUserProfilePicture(User user);
}
