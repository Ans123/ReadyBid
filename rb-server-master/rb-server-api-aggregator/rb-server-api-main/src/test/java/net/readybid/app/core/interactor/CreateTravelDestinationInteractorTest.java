package net.readybid.app.core.interactor;

import net.readybid.app.interactors.core.gate.IdFactory;
import net.readybid.app.core.action.traveldestination.CreateTravelDestinationRequest;
import net.readybid.app.core.entities.location.Location;
import net.readybid.app.core.entities.location.distance.DistanceUnit;
import net.readybid.app.core.entities.traveldestination.*;
import net.readybid.test_utils.RbRandom;
import net.readybid.user.BasicUserDetails;
import net.readybid.utils.CreationDetailsAssert;
import net.readybid.utils.StatusDetailsAssert;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("[ CreateTravelDestination ] Interactor: ")
class CreateTravelDestinationInteractorTest {

    private CreateTravelDestinationInteractor interactor;
    private IdFactory idFactory;
    private String idFactoryCreateResult;

    @BeforeEach
    void setUp(){
        idFactoryCreateResult = RbRandom.alphanumeric(100, false) ;
        idFactory = mock(IdFactory.class);
        doReturn(idFactoryCreateResult).when(idFactory).create();

        interactor = new CreateTravelDestinationInteractor(idFactory);
    }

    @Nested
    @DisplayName("When Creating new TravelDestination ")
    class CreateTest {
        CreateTravelDestinationRequest request;
        TravelDestination result;
        TravelDestinationHotelFilter rfpDefaultFilter;

        private final Executable create = () -> result = interactor.create(request, rfpDefaultFilter);

        @Test
        @DisplayName("it should create Travel Destination with data from Request")
        void createTravelDestinationTest() throws Throwable {
            request = createTravelDestinationRequest();

            create.execute();

            assertTravelDestination(result, request);
            verify(idFactory, times(1)).create();
        }

        @Test
        @DisplayName("it should create Office Travel Destination with Filter")
        void createOfficeTravelDestinationTest() throws Throwable {
            request = createTravelDestinationRequest(TravelDestinationType.OFFICE);

            create.execute();

            assertTravelDestination(result, request);
            assertFilterValues(result, 5.0D);
        }

        @Test
        @DisplayName("it should create Office Travel Destination with Rfp Default Filter when set")
        void createOfficeTravelDestinationWithRfpDefaultFilterTest() throws Throwable {
            request = createTravelDestinationRequest(TravelDestinationType.OFFICE);
            rfpDefaultFilter = new TravelDestinationHotelFilter();

            create.execute();

            assertTravelDestination(result, request);
            assertFilter(result, rfpDefaultFilter);
        }


        @Test
        @DisplayName("it should create City Travel Destination with Filter")
        void createCityTravelDestinationTest() throws Throwable {
            request = createTravelDestinationRequest(TravelDestinationType.CITY);

            create.execute();

            assertTravelDestination(result, request);
            assertFilterValues(result,10.0D);
        }

        @Test
        @DisplayName("it should create City Travel Destination with Rfp Default Filter when set")
        void createCityTravelDestinationWithRfpDefaultFilterTest() throws Throwable {
            request = createTravelDestinationRequest(TravelDestinationType.CITY);
            rfpDefaultFilter = new TravelDestinationHotelFilter();

            create.execute();

            assertTravelDestination(result, request);
            assertFilter(result, rfpDefaultFilter);
        }
    }


    @Nested
    @DisplayName("When Creating in Bulk ")
    class CreateAllTest {
        List<CreateTravelDestinationRequest> requests;
        List<TravelDestinationImpl> result;
        TravelDestinationHotelFilter rfpDefaultFilter;

        private final Executable createAll = () -> result = interactor.createAll(requests, rfpDefaultFilter);

        @Test
        @DisplayName("it should create Travel Destinations from all requests")
        void createAllTest() throws Throwable {
            requests = createRequests();

            createAll.execute();

            for (int i = 0; i < requests.size(); i++) {
                final CreateTravelDestinationRequest r = requests.get(i);
                final TravelDestination td = result.get(i);

                assertTravelDestination(td, r);
                switch (r.type){
                    case CITY:
                        assertFilterValues(td, 10.0D);
                        break;
                    case OFFICE:
                        assertFilterValues(td, 5.0D);
                        break;
                }
            }
            verify(idFactory, times(requests.size())).create();
        }

        @Test
        @DisplayName("it should create Travel Destinations from all requests with RFP default filter when set")
        void createAllWithRfpDefaultFilterTest() throws Throwable {
            requests = createRequests();
            rfpDefaultFilter = new TravelDestinationHotelFilter();

            createAll.execute();

            for (int i = 0; i < requests.size(); i++) {
                final CreateTravelDestinationRequest r = requests.get(i);
                final TravelDestination td = result.get(i);

                assertTravelDestination(td, r);
                assertFilter(td, rfpDefaultFilter);
            }
            verify(idFactory, times(requests.size())).create();
        }

        private List<CreateTravelDestinationRequest> createRequests() {
            final List<CreateTravelDestinationRequest> requestList = new ArrayList<>();
            for(int i = RbRandom.randomInt(100); i>=0; i--){
                requestList.add(createTravelDestinationRequest());
            }
            return requestList;
        }
    }

    private CreateTravelDestinationRequest createTravelDestinationRequest() {
        return createTravelDestinationRequest(RbRandom.randomFromArray(TravelDestinationType.values()));
    }

    private CreateTravelDestinationRequest createTravelDestinationRequest(TravelDestinationType type) {
        final CreateTravelDestinationRequest request = new CreateTravelDestinationRequest();
        request.rfpId = RbRandom.alphanumeric(100, false);
        request.type = type;
        request.name = RbRandom.alphanumeric(100, false);
        request.estimatedRoomNights = RbRandom.randomInt(10000);
        request.estimatedSpend = RbRandom.randomLong(1000000L);
        request.location = mock(Location.class);
        request.creator = mock(BasicUserDetails.class);
        return request;
    }

    private void assertTravelDestination(TravelDestination result, CreateTravelDestinationRequest request){
        TravelDestinationImplAssert.that(result)
                .hasId(idFactoryCreateResult)
                .hasRfpId(request.rfpId)
                .hasType(request.type)
                .hasName(request.name)
                .hasEstimatedSpend(request.estimatedSpend)
                .hasEstimatedRoomNights(request.estimatedRoomNights)
                .hasLocation(request.location);

        CreationDetailsAssert.that(result.getCreated())
                .hasBy(request.creator)
                .hasAt();

        StatusDetailsAssert.that(result.getStatus())
                .hasBy(request.creator)
                .hasAt(result.getCreated().getAt())
                .hasValue(TravelDestinationStatus.CREATED);
    }

    private void assertFilterValues(TravelDestination result, double distanceValue) {
        TravelDestinationHotelFilterAssert.that(result.getFilter())
                .hasMaxDistanceValue(distanceValue)
                .hasMaxDistanceUnit(DistanceUnit.MI)
                .hasAmenities(null)
                .hasChains(null);
    }

    private void assertFilter(TravelDestination result, TravelDestinationHotelFilter filter) {
        Assert.assertSame(filter, result.getFilter());
    }
}