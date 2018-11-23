package net.readybid.api.rfp_hotel.bid.supplier.contact;

import net.readybid.api.main.access.BidAccessControlService;
import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.app.use_cases.rfp_hotel.bid.CreateHotelRfpSupplierContactRequestAssert;
import net.readybid.app.use_cases.rfp_hotel.bid.SetHotelRfpSupplierContactHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.test_utils.RbRandom;
import net.readybid.web.RbViewModel;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@DisplayName("HotelRfpSupplierContactController ")
class HotelRfpSupplierContactControllerTest {

    private HotelRfpSupplierContactController sut;

    private BidAccessControlService bidAccessControlService;
    private SetHotelRfpSupplierContactHandler setHotelRfpSupplierContactHandler;

    @BeforeEach
    void setup(){
        bidAccessControlService = mock(BidAccessControlService.class);
        setHotelRfpSupplierContactHandler = mock(SetHotelRfpSupplierContactHandler.class);
        sut = new HotelRfpSupplierContactController(bidAccessControlService, setHotelRfpSupplierContactHandler);
    }

    @Nested
    @DisplayName("createSupplierContact")
    class CreateSupplierContactTest{

        private String bidId;
        private CreateHotelRfpSupplierContactWebRequest request;
        private AuthenticatedUser currentUser;

        private final Supplier<RbViewModel> mut = () -> sut.createSupplierContact(bidId, request, currentUser);

        @BeforeEach
        void setup(){
            bidId = RbRandom.idAsString();
            request = CreateHotelRfpSupplierContactWebRequestFactory.random();
            currentUser = mock(AuthenticatedUser.class);
        }

        @DisplayName("Checks Authorization")
        @Test
        void checksAuthorization(){
            mut.get();

            verify(bidAccessControlService, times(1)).updateAsBuyer(same(bidId));
        }

        @DisplayName("Initiates SetSupplierContact Use Case")
        @Test
        void initiatesSetSupplierContactUseCase(){
            final RbViewModel expectedResponse = mock(RbViewModel.class);
            final ArgumentCaptor<List<String>> bidsIdsCaptor = ArgumentCaptor.forClass(List.class);
            final ArgumentCaptor<CreateHotelRfpSupplierContactRequest> supplierContactCaptor =
                    ArgumentCaptor.forClass(CreateHotelRfpSupplierContactRequest.class);

            doReturn(expectedResponse).when(setHotelRfpSupplierContactHandler)
                    .set(anyList(), any(CreateHotelRfpSupplierContactRequest.class), same(currentUser));

            final RbViewModel response = mut.get();

            verify(setHotelRfpSupplierContactHandler, times(1))
                    .set(bidsIdsCaptor.capture(), supplierContactCaptor.capture(), same(currentUser));

            new ListAssert<>(bidsIdsCaptor.getValue()).containsExactly(bidId);

            CreateHotelRfpSupplierContactRequestAssert.that(supplierContactCaptor.getValue())
                    .hasType(request.accountType)
                    .hasFirstName(request.firstName)
                    .hasLastName(request.lastName)
                    .hasEmailAddress(request.emailAddress.toLowerCase())
                    .hasPhone(request.phone)
                    .hasJobTitle(request.jobTitle);
            assertSame(expectedResponse, response);
        }
    }

    @Nested
    @DisplayName("setSupplierContact")
    class SetSupplierContactTest{

        private String bidId;
        private SetHotelRfpSupplierContactWebRequest request;
        private AuthenticatedUser currentUser;

        private final Supplier<RbViewModel> mut = () -> sut.setSupplierContact(bidId, request, currentUser);

        @BeforeEach
        void setup(){
            bidId = RbRandom.idAsString();
            currentUser = mock(AuthenticatedUser.class);
            request = SetHotelRfpSupplierContactWebRequestFactory.random();
        }

        @DisplayName("Checks Authorization")
        @Test
        void checksAuthorization(){
            mut.get();

            verify(bidAccessControlService, times(1)).updateAsBuyer(same(bidId));
        }

        @DisplayName("Initiates SetSupplierContact Use Case")
        @Test
        void initiatesSetSupplierContactUseCase(){
            final RbViewModel expectedResponse = mock(RbViewModel.class);
            //noinspection unchecked
            final ArgumentCaptor<List<String>> bidsIdsCaptor = ArgumentCaptor.forClass(List.class);
            doReturn(expectedResponse).when(setHotelRfpSupplierContactHandler)
                    .set(anyList(), same(request.userAccountId), same(currentUser));

            final RbViewModel response = mut.get();

            verify(setHotelRfpSupplierContactHandler, times(1))
                    .set(bidsIdsCaptor.capture(), same(request.userAccountId), same(currentUser));
            new ListAssert<>(bidsIdsCaptor.getValue()).containsExactly(bidId);

            assertSame(expectedResponse, response);
        }
    }

    @Nested
    @DisplayName("createSupplierContacts")
    class CreateSupplierContactsTest{

        private CreateHotelRfpSupplierContactsWebRequest request;
        private AuthenticatedUser currentUser;

        private final Supplier<RbViewModel> mut = () -> sut.createSupplierContacts(request, currentUser);

        @BeforeEach
        void setup(){
            request = CreateHotelRfpSupplierContactsWebRequestFactory.random();
            currentUser = mock(AuthenticatedUser.class);
        }

        @DisplayName("Checks Authorization")
        @Test
        void checksAuthorization(){
            mut.get();

            verify(bidAccessControlService, times(1)).updateAsBuyer(same(request.bids));
        }

        @DisplayName("Initiates SetSupplierContact Use Case")
        @Test
        void initiatesSetSupplierContactUseCase(){
            final RbViewModel expectedResponse = mock(RbViewModel.class);
            final ArgumentCaptor<CreateHotelRfpSupplierContactRequest> supplierContactCaptor =
                    ArgumentCaptor.forClass(CreateHotelRfpSupplierContactRequest.class);

            doReturn(expectedResponse).when(setHotelRfpSupplierContactHandler)
                    .set(same(request.bids), any(CreateHotelRfpSupplierContactRequest.class), same(currentUser));

            final RbViewModel response = mut.get();

            verify(setHotelRfpSupplierContactHandler, times(1))
                    .set(same(request.bids), supplierContactCaptor.capture(), same(currentUser));

            CreateHotelRfpSupplierContactRequestAssert.that(supplierContactCaptor.getValue())
                    .hasFirstName(request.firstName)
                    .hasLastName(request.lastName)
                    .hasEmailAddress(request.emailAddress.toLowerCase())
                    .hasPhone(request.phone)
                    .hasJobTitle(request.jobTitle);

            assertSame(expectedResponse, response);
        }
    }

    @Nested
    @DisplayName("setSupplierContacts")
    class SetSupplierContactsTest{

        private SetHotelRfpSupplierContactsWebRequest request;
        private AuthenticatedUser currentUser;

        private final Supplier<RbViewModel> mut = () -> sut.setSupplierContacts(request, currentUser);

        @BeforeEach
        void setup(){
            request = SetHotelRfpSupplierContactsWebRequestFactory.random();
            currentUser = mock(AuthenticatedUser.class);
        }

        @DisplayName("Checks Authorization")
        @Test
        void checksAuthorization(){
            mut.get();

            verify(bidAccessControlService, times(1)).updateAsBuyer(same(request.bids));
        }

        @DisplayName("Initiates SetSupplierContact Use Case")
        @Test
        void initiatesSetSupplierContactUseCase(){
            final RbViewModel expectedResponse = mock(RbViewModel.class);
            doReturn(expectedResponse).when(setHotelRfpSupplierContactHandler)
                    .set(same(request.bids), same(request.userAccountId), same(currentUser));

            final RbViewModel response = mut.get();

            verify(setHotelRfpSupplierContactHandler, times(1))
                    .set(same(request.bids), same(request.userAccountId), same(currentUser));

            assertSame(expectedResponse, response);
        }
    }
}