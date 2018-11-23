package net.readybid.app.persistence.mongodb.app.rfp_hotel;

import com.mongodb.client.model.UpdateManyModel;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.UpdateResult;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpStorageManager;
import net.readybid.app.persistence.mongodb.repository.HotelRfpRepository;
import net.readybid.app.persistence.mongodb.repository.UserAccountRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.UserAccountCollection;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.user.User;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.rfp.core.RfpImpl;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.*;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpCollection.*;
import static net.readybid.mongodb.RbMongoFilters.byId;
import static net.readybid.mongodb.RbMongoFilters.oid;

@Service
class HotelRfpStorageManagerImpl implements HotelRfpStorageManager {


    private final HotelRfpRepository repository;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    HotelRfpStorageManagerImpl(
            HotelRfpRepository repository,
            @Qualifier("NewUserAccountRepository") UserAccountRepository userAccountRepository
    ) {
        this.repository = repository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public void updateUserBasicDetails(User user) {
        updateUser(user, () -> combine(
                set(BUYER_CONTACT_FIRST_NAME, user.getFirstName()),
                set(BUYER_CONTACT_LAST_NAME, user.getLastName()),
                set(BUYER_CONTACT_FULL_NAME, user.getFullName()),
                set(BUYER_CONTACT_PHONE, user.getPhone())
        ));
    }

    @Override
    public void updateUserEmailAddress(User user) {
        updateUser(user, () -> set(BUYER_EMAIL_ADDRESS, user.getEmailAddress()));
    }

    @Override
    public void updateUserProfilePicture(User user) {
        updateUser(user, () -> set(BUYER_CONTACT_PROFILE_PICTURE, user.getProfilePicture()));
    }

    @Override
    public void updateBuyerLogo(String accountId, String logo) {
        final List<WriteModel<RfpImpl>> writes = new ArrayList<>();

        writes.add(new UpdateManyModel<>(
                eq(HotelRfpCollection.BUYER_COMPANY_ACCOUNT_ID, oid(accountId)),
                set(BUYER_COMPANY_LOGO, logo)
        ));

        writes.add(new UpdateManyModel<>(
                eq(HotelRfpCollection.BUYER_CONTACT_COMPANY_ACCOUNT_ID, oid(accountId)),
                set(BUYER_CONTACT_COMPANY_LOGO, logo)
        ));

        repository.bulkWrite(writes);
    }

    @Override
    public void updateBuyerBasicInformation(Account account) {
        final List<WriteModel<RfpImpl>> writes = new ArrayList<>();

        writes.add(updateAccountModel(account, BUYER_COMPANY_ACCOUNT_ID, BUYER_COMPANY_NAME,
                BUYER_COMPANY_INDUSTRY, BUYER_COMPANY_LOCATION, BUYER_COMPANY_WEBSITE));

        writes.add(new UpdateManyModel<>(
                and(Arrays.asList(
                        eq(BUYER_COMPANY_ACCOUNT_ID, oid(String.valueOf(account.getId()))),
                        gt(PROGRAM_END_DATE, LocalDate.now())
                )),
                set(QUESTIONNAIRE_RESPONSE_ANSWERS_CLIENT_NAME, account.getName()))
        );

        writes.add(updateAccountModel(account, BUYER_CONTACT_COMPANY_ACCOUNT_ID, BUYER_CONTACT_COMPANY_NAME,
                BUYER_CONTACT_COMPANY_INDUSTRY, BUYER_CONTACT_COMPANY_LOCATION, BUYER_CONTACT_COMPANY_WEBSITE));

        repository.bulkWrite(writes);
    }

    @Override
    public void enableChainSupport(String rfpId, String namCoverLetter) {
        final UpdateResult result = repository.updateOne(byId(rfpId),
                combine(
                        set(CHAIN_SUPPORT, true),
                        set(NAM_COVER_LETTER, namCoverLetter)
                ));
        if(!(result.wasAcknowledged() && result.getMatchedCount() == 1))
            throw new UnableToCompleteRequestException(String.format("EnableChainSupport failed for RFP %s", rfpId));
    }

    private void updateUser(User user, Supplier<Bson> update) {
        final List<? extends UserAccount> userAccounts = userAccountRepository.find(
                eq(UserAccountCollection.USER_ID, user.getId()),
                include(UserAccountCollection.USER_ID, UserAccountCollection.ACCOUNT_ID));

        if(!userAccounts.isEmpty()){
            final List<WriteModel<RfpImpl>> writes = new ArrayList<>();

            for(UserAccount ua : userAccounts){
                writes.add(new UpdateManyModel<>(eq(BUYER_CONTACT_ID, ua.getId()),update.get()));
            }

            repository.bulkWrite(writes);
        }
    }

    private WriteModel<RfpImpl> updateAccountModel(
            Account account,
            String idField, String nameField, String industryField, String locationField, String websiteField
    ) {
        return new UpdateManyModel<>(
                eq(idField, account.getId()),
                combine(
                        set(nameField, account.getName()),
                        set(industryField, account.getIndustry()),
                        set(locationField, account.getLocation()),
                        account.getWebsite() == null || account.getWebsite().isEmpty()
                                ? unset(websiteField) : set(websiteField, account.getWebsite())
                )
        );
    }
}
