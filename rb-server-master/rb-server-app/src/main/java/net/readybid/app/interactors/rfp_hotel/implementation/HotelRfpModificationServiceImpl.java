package net.readybid.app.interactors.rfp_hotel.implementation;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.interactors.rfp.RfpModificationServiceLibrary;
import net.readybid.app.interactors.rfp_hotel.HotelRfpModificationService;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class HotelRfpModificationServiceImpl implements HotelRfpModificationService {

    private final HotelRfpUserModificationService hotelRfpUserModificationService;
    private final HotelRfpAccountModificationService hotelRfpAccountModificationService;

    @Autowired
    public HotelRfpModificationServiceImpl(
            RfpModificationServiceLibrary rfpModificationServiceLibrary,
            HotelRfpUserModificationService hotelRfpUserModificationService,
            HotelRfpAccountModificationService hotelRfpAccountModificationService
    ){
        this.hotelRfpUserModificationService = hotelRfpUserModificationService;
        this.hotelRfpAccountModificationService = hotelRfpAccountModificationService;

        rfpModificationServiceLibrary.register(this);
    }

    @Override
    public void updateEntityBasicInformation(Entity entity, Account account) {
        hotelRfpAccountModificationService.updateEntityBasicInformation(entity, account);
    }

    @Override
    public void updateAccountLogo(Account account) {
        hotelRfpAccountModificationService.updateAccountLogo(account);
    }

    @Override
    public void handleUserDetailsChange(User user) {
        hotelRfpUserModificationService.handleUserDetailsChange(user);
    }

    @Override
    public void handleUserEmailAddressChange(User user) {
        hotelRfpUserModificationService.handleUserEmailAddressChange(user);
    }

    @Override
    public void handleUserProfilePictureChange(User user) {
        hotelRfpUserModificationService.handleUserProfilePictureChange(user);
    }
}
