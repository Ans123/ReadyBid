package net.readybid.app.interactors.rfp.implementation;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.interactors.rfp.RfpModificationService;
import net.readybid.app.interactors.rfp.RfpModificationServiceLibrary;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class RfpModificationServiceImpl implements RfpModificationService, RfpModificationServiceLibrary {

    private final List<RfpModificationService> rfpModificationServices;

    @Autowired
    public RfpModificationServiceImpl() {
        rfpModificationServices = new ArrayList<>();
    }

    @Override
    public void register(RfpModificationService rfpModificationService) {
        rfpModificationServices.add(rfpModificationService);
    }

    @Override
    public void updateEntityBasicInformation(Entity entity, Account account) {
        rfpModificationServices.forEach(service -> service.updateEntityBasicInformation(entity, account));
    }

    @Override
    public void updateAccountLogo(Account account) {
        rfpModificationServices.forEach(service -> service.updateAccountLogo(account));
    }

    @Override
    public void handleUserDetailsChange(User user) {
        rfpModificationServices.forEach(service -> service.handleUserDetailsChange(user));
    }

    @Override
    public void handleUserEmailAddressChange(User user) {
        rfpModificationServices.forEach((service -> service.handleUserEmailAddressChange(user)));
    }

    @Override
    public void handleUserProfilePictureChange(User user) {
        rfpModificationServices.forEach((service -> service.handleUserProfilePictureChange(user)));
    }
}
