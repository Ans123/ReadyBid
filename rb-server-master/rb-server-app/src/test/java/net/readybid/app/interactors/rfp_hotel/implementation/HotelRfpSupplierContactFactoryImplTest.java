package net.readybid.app.interactors.rfp_hotel.implementation;

import net.readybid.app.core.entities.location.Location;
import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContactAssert;
import net.readybid.app.interactors.core.gate.IdFactory;
import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.app.persistence.IdFactoryImpl;
import net.readybid.app.use_cases.rfp_hotel.bid.CreateHotelRfpSupplierContactRequestFactory;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.user.User;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.entity_factories.AccountTestFactory;
import net.readybid.entity_factories.UserAccountTestFactory;
import net.readybid.entity_factories.UserTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

@DisplayName("HotelRfpSupplierContactFactoryImpl")
class HotelRfpSupplierContactFactoryImplTest {

    private HotelRfpSupplierContactFactoryImpl sut;
    private IdFactory idFactory;

    @BeforeEach
    void setup(){
        idFactory = new IdFactoryImpl();
        sut = new HotelRfpSupplierContactFactoryImpl(idFactory);
    }

    @Nested
    @DisplayName("create from Request")
    class CreateFromRequestTest {

        private CreateHotelRfpSupplierContactRequest request;
        private Account account;

        private Supplier<HotelRfpSupplierContact> mut = () -> sut.create(request, account);

        @Test
        @DisplayName("creates new supplier contact")
        void createsNewSupplierContact(){
            final String markId = idFactory.create();
            account = AccountTestFactory.random();
            request = CreateHotelRfpSupplierContactRequestFactory.random();

            final HotelRfpSupplierContact result = mut.get();

            final Location accountLocation = account.getLocation();
            final Address accountAddress = accountLocation.getAddress();
            HotelRfpSupplierContactAssert.that(result)
                    .hasIdCreatedAfter(markId)
                    .hasFirstName(request.firstName)
                    .hasLastName(request.lastName)
                    .hasEmailAddress(request.emailAddress)
                    .hasPhone(request.phone)
                    .hasProfilePicture(null)
                    .hasJobTitle(request.jobTitle)
                    .hasIsUser(false)
                    .hasCompany( assertCompany -> assertCompany
                            .hasAccountId(account.getId())
                            .hasEntityId(account.getEntityId())
                            .hasType(account.getType())
                            .hasName(account.getName())
                            .hasWebsite(account.getWebsite())
                            .hasEmailAddress(account.getEmailAddress())
                            .hasPhone(account.getPhone())
                            .hasLogo(account.getLogo())
                            .hasLocation(locationAssert -> locationAssert
                                    .hasFullAddress(accountLocation.getFullAddress())
                                    .hasAddress( assertAddress -> assertAddress
                                            .hasAddress1(accountAddress.getAddress1())
                                            .hasAddress2(accountAddress.getAddress2())
                                            .hasCity(accountAddress.getCity())
                                            .hasCounty(accountAddress.getCounty())
                                            .hasState(accountAddress.getState())
                                            .hasRegion(accountAddress.getRegion())
                                            .hasZip(accountAddress.getZip())
                                            .hasCountry(accountAddress.getCountry())
                                    )));
        }
    }

    @Nested
    @DisplayName("create from UserAccount")
    class CreateFromUserAccountTest {

        private UserAccount userAccount;
        private User user;
        private Account account;

        private Supplier<HotelRfpSupplierContact> mut = () -> sut.create(userAccount);

        @Test
        @DisplayName("creates new supplier contact")
        void createsNewSupplierContact(){
            final String markId = idFactory.create();
            user = UserTestFactory.random();
            account = AccountTestFactory.random();
            userAccount = UserAccountTestFactory.random(user, account);

            final HotelRfpSupplierContact result = mut.get();

            final Location accountLocation = account.getLocation();
            final Address accountAddress = accountLocation.getAddress();
            HotelRfpSupplierContactAssert.that(result)
                    .hasIdCreatedAfter(markId)
                    .hasFirstName(user.getFirstName())
                    .hasLastName(user.getLastName())
                    .hasEmailAddress(user.getEmailAddress())
                    .hasPhone(user.getPhone())
                    .hasProfilePicture(user.getProfilePicture())
                    .hasJobTitle(userAccount.getJobTitle())
                    .hasIsUser(true)
                    .hasCompany( assertCompany -> assertCompany
                            .hasAccountId(account.getId())
                            .hasEntityId(account.getEntityId())
                            .hasType(account.getType())
                            .hasName(account.getName())
                            .hasWebsite(account.getWebsite())
                            .hasEmailAddress(account.getEmailAddress())
                            .hasPhone(account.getPhone())
                            .hasLogo(account.getLogo())
                            .hasLocation(locationAssert -> locationAssert
                                    .hasFullAddress(accountLocation.getFullAddress())
                                    .hasAddress( assertAddress -> assertAddress
                                            .hasAddress1(accountAddress.getAddress1())
                                            .hasAddress2(accountAddress.getAddress2())
                                            .hasCity(accountAddress.getCity())
                                            .hasCounty(accountAddress.getCounty())
                                            .hasState(accountAddress.getState())
                                            .hasRegion(accountAddress.getRegion())
                                            .hasZip(accountAddress.getZip())
                                            .hasCountry(accountAddress.getCountry())
                                    )));
        }
    }
}