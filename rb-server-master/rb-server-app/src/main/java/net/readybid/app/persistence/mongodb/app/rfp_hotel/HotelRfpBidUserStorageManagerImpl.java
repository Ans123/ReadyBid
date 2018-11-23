package net.readybid.app.persistence.mongodb.app.rfp_hotel;

import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.UpdateManyModel;
import com.mongodb.client.model.WriteModel;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidImpl;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidUserStorageManager;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidRepository;
import net.readybid.app.persistence.mongodb.repository.UserAccountRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.UserAccountCollection;
import net.readybid.auth.user.User;
import net.readybid.auth.useraccount.UserAccount;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.*;

@Service
class HotelRfpBidUserStorageManagerImpl implements HotelRfpBidUserStorageManager {

    private final HotelRfpBidRepository repository;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    HotelRfpBidUserStorageManagerImpl(
            HotelRfpBidRepository repository,
            @Qualifier("NewUserAccountRepository") UserAccountRepository userAccountRepository
    ) {
        this.repository = repository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public void updateUserBasicDetails(User user) {
        updateUser(user,
                () -> combine(
                        set(BUYER_CONTACT_FIRST_NAME, user.getFirstName()),
                        set(BUYER_CONTACT_LAST_NAME, user.getLastName()),
                        set(BUYER_CONTACT_FULL_NAME, user.getFullName()),
                        set(BUYER_CONTACT_PHONE, user.getPhone())
                ),
                () -> combine(
                        set(SUPPLIER_CONTACT_FIRST_NAME, user.getFirstName()),
                        set(SUPPLIER_CONTACT_LAST_NAME, user.getLastName()),
                        set(SUPPLIER_CONTACT_FULL_NAME, user.getFullName()),
                        set(SUPPLIER_CONTACT_PHONE, user.getPhone())
                )
        );
    }

    @Override
    public void updateUserEmailAddress(User user) {
        updateUser(user,
                () -> set(BUYER_CONTACT_EMAIL_ADDRESS, user.getEmailAddress()),
                () -> set(SUPPLIER_CONTACT_EMAIL_ADDRESS, user.getEmailAddress())
        );
    }

    @Override
    public void updateUserProfilePicture(User user) {
        updateUser(user,
                () -> set(BUYER_CONTACT_PROFILE_PICTURE, user.getProfilePicture()),
                () -> set(SUPPLIER_CONTACT_PROFILE_PICTURE, user.getProfilePicture())
        );
    }

    private void updateUser(User user, Supplier<Bson> buyerUpdate, Supplier<Bson> supplierUpdate ) {
        final List<? extends UserAccount> userAccounts = userAccountRepository.find(
                eq(UserAccountCollection.USER_ID, user.getId()),
                include(UserAccountCollection.USER_ID, UserAccountCollection.ACCOUNT_ID));

        if(!userAccounts.isEmpty()){
            final List<WriteModel<HotelRfpBidImpl>> writes = new ArrayList<>();

            for(UserAccount ua : userAccounts){
                writes.add(new UpdateManyModel<>(eq(BUYER_CONTACT_ID, ua.getId()),buyerUpdate.get()));
                writes.add(new UpdateManyModel<>(eq(SUPPLIER_CONTACT_ID, ua.getId()),supplierUpdate.get()));
            }

            repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));
        }
    }
}
