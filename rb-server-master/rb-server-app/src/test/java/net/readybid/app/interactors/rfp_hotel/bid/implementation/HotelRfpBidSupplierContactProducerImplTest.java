package net.readybid.app.interactors.rfp_hotel.bid.implementation;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.app.interactors.rfp_hotel.HotelRfpSupplierContactFactory;
import net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier.HotelRfpBidSupplierContactProducerImpl;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidSupplierContactLoader;
import net.readybid.app.use_cases.rfp_hotel.bid.CreateHotelRfpSupplierContactRequestFactory;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.account.core.AccountService;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.web.UserAccountService;
import net.readybid.entity_factories.BasicUserDetailsImplFactory;
import net.readybid.test_utils.RbRandom;
import net.readybid.user.BasicUserDetails;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@DisplayName("Hotel Rfp Bid Supplier Contact Producer Impl")
class HotelRfpBidSupplierContactProducerImplTest {

    private HotelRfpBidSupplierContactProducerImpl sut;

    private HotelRfpSupplierContactFactory supplierContactFactory;
    private HotelRfpBidSupplierContactLoader supplierContactLoader;
    private AccountService accountService;
    private UserAccountService userAccountService;

    private List<String> bidsIds = new ArrayList<>();

    @BeforeEach
    void setup (){
        supplierContactFactory = mock(HotelRfpSupplierContactFactory.class);
        supplierContactLoader = mock(HotelRfpBidSupplierContactLoader.class);
        accountService = mock(AccountService.class);
        userAccountService = mock(UserAccountService.class);

        sut = new HotelRfpBidSupplierContactProducerImpl(
                supplierContactFactory,
                supplierContactLoader,
                accountService,
                userAccountService
        );
    }

    @Nested
    @DisplayName("create Contact from Request")
    class CreateContactFromRequestTest{

        private CreateHotelRfpSupplierContactRequest supplierContactRequest;
        private BasicUserDetails currentUser;

        private String entityId;
        private Account account;
        private HotelRfpSupplierContact expectedResult;

        private Supplier<HotelRfpSupplierContact> mut = () -> sut.create(bidsIds, supplierContactRequest,currentUser);

        @BeforeEach
        void setup(){
            bidsIds.add(RbRandom.idAsString());

            supplierContactRequest = CreateHotelRfpSupplierContactRequestFactory.random();
            currentUser = BasicUserDetailsImplFactory.random();
            entityId = RbRandom.idAsString();
            account = mock(Account.class);
            expectedResult = mock(HotelRfpSupplierContact.class);

            doReturn(entityId).when(supplierContactLoader).getEntityId(same(supplierContactRequest.type), same(bidsIds));
            doReturn(account).when(accountService)
                    .getOrCreateAccount(same(supplierContactRequest.type), same(entityId), same(currentUser));
            doReturn(expectedResult).when(supplierContactFactory).create(same(supplierContactRequest), same(account));
        }

        @Test
        @DisplayName("returns HotelRfpSupplierContact")
        void returnsHotelRfpSupplierContact(){
            assertSame(expectedResult, mut.get());
        }

        @Test
        @DisplayName("returns Null if entityId was not found")
        void returnsNullIfEntityIdNotFound(){
            doReturn(null).when(supplierContactLoader).getEntityId(any(), any());
            assertNull(mut.get());
        }
    }

    @Nested
    @DisplayName("create Contact from User Account")
    class CreateContactFromUserAccountTest{

        private String userAccountId;

        private ObjectId entityId;
        private String entityIdString;
        private UserAccount userAccount;
        private EntityType type;
        private HotelRfpSupplierContact expectedResult;

        private Supplier<HotelRfpSupplierContact> mut = () -> sut.create(bidsIds, userAccountId);

        @BeforeEach
        void setup(){
            bidsIds.add(RbRandom.idAsString());
            userAccountId = RbRandom.idAsString();

            entityId = RbRandom.oid();
            entityIdString = entityId.toString();
            userAccount = mock(UserAccount.class);
            type = RbRandom.randomEnum(EntityType.class);
            expectedResult = mock(HotelRfpSupplierContact.class);

            doReturn(userAccount).when(userAccountService).getById(same(userAccountId));
            doReturn(type).when(userAccount).getAccountType();
            doReturn(entityId).when(userAccount).getEntityId();
            doReturn(entityIdString).when(supplierContactLoader).getEntityId(same(type), same(bidsIds));
            doReturn(expectedResult).when(supplierContactFactory).create(same(userAccount));
        }

        @Test
        @DisplayName("returns HotelRfpSupplierContact")
        void returnsHotelRfpSupplierContact(){
            assertSame(expectedResult, mut.get());
        }

        @Test
        @DisplayName("returns Null if UserAccount was not found")
        void returnsNullWhenUserAccountNotFound(){
            doReturn(null).when(userAccountService).getById(any(String.class));
            assertNull(mut.get());
        }

        @Test
        @DisplayName("returns Null if entityId was not found")
        void returnsNullWhenEntityIdNotFound(){
            doReturn(null).when(supplierContactLoader).getEntityId(any(), any());
            assertNull(mut.get());
        }

        @Test
        @DisplayName("returns Null if entityId from Bid and UserAccount do not match")
        void returnsNullWhenEntityIdFromBidAndUserAccountDoNotMatch(){
            doReturn(RbRandom.idAsString()).when(supplierContactLoader).getEntityId(any(), any());
            assertNull(mut.get());
        }
    }
}