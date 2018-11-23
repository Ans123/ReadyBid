package net.readybid.app.core.transaction;

import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.RfpRepository;
import net.readybid.app.interactors.core.gate.IdFactory;
import net.readybid.app.core.action.traveldestination.CreateTravelDestinationAction;
import net.readybid.app.core.action.traveldestination.CreateTravelDestinationRequest;
import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import net.readybid.app.core.entities.traveldestination.TravelDestinationImpl;
import net.readybid.app.core.service.traveldestination.SaveTravelDestinationRepository;
import net.readybid.app.persistence.IdFactoryImpl;
import net.readybid.exceptions.NotAllowedException;
import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("[ CreateTravelDestination ] Transaction: ")
class CreateTravelDestinationTransactionTest {

    @Nested
    @DisplayName("When Creating in Bulk ")
    class CreateTravelDestinationTransactionInBulkTest {

        private RfpAccessControlService rfpAccessControl;
        private CreateTravelDestinationAction createTravelDestination;
        private SaveTravelDestinationRepository saveDestination;
        private RfpRepository rfpRepository;

        private String rfpId = "rfpId";
        private List<CreateTravelDestinationRequest> requests = new ArrayList<>();
        private TravelDestinationHotelFilter rfpDefaultFilter = new TravelDestinationHotelFilter();

        private CreateTravelDestinationTransaction createDestinationTransaction;
        private List<String> createdDestinations;
        private final Executable createAll = () -> createdDestinations = createDestinationTransaction.createAll(rfpId, requests);

        @BeforeEach
        void setUp(){
            rfpAccessControl = mock(RfpAccessControlService.class);
            createTravelDestination = mock(CreateTravelDestinationAction.class);
            saveDestination = mock(SaveTravelDestinationRepository.class);
            rfpRepository = mock(RfpRepository.class);

            createDestinationTransaction = new CreateTravelDestinationTransaction(rfpAccessControl, createTravelDestination, saveDestination, rfpRepository);

            rfpId = RbRandom.alphanumeric(50, false);
            requests = new ArrayList<>();
        }

        @Test
        @DisplayName("it should Create Travel Destinations and save them in Repository")
        void test() throws Throwable {
            final List<TravelDestinationImpl> destinations = createCreatedDestinationsMock();
            doReturn(rfpDefaultFilter)
                    .when(rfpRepository).getRfpDefaultFilter(eq(rfpId));
            doReturn(destinations)
                    .when(createTravelDestination).createAll(anyList(), eq(rfpDefaultFilter));

            createAll.execute();

            verify(saveDestination, times(1)).createAll(same(destinations));
            destinations.stream().map(td -> createdDestinations.contains(td.getId())).forEach(Assertions::assertTrue);
        }

        private List<TravelDestinationImpl> createCreatedDestinationsMock() {
            final List<TravelDestinationImpl> createdDestinationsMock = new ArrayList<>();
            final IdFactory idFactory = new IdFactoryImpl();

            for(int i = RbRandom.randomInt(10); i>=0; i--){
                final TravelDestinationImpl td = new TravelDestinationImpl();
                td.setId(idFactory.create());
                createdDestinationsMock.add(td);
            }

            return createdDestinationsMock;
        }

        @Test
        @DisplayName("it should Deny Access if User does not have Update access permission on RFP")
        void updateRfpAccessDeniedTest(){
            doThrow(NotAllowedException.class).when(rfpAccessControl).update(anyString());

            assertThrows(NotAllowedException.class, createAll);
        }
    }
}