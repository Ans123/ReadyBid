package net.readybid.app.gate.api.rfps.traveldestinations.create;

import net.readybid.test_utils.RbRandom;
import net.readybid.app.core.action.traveldestination.CreateTravelDestinationRequest;
import net.readybid.app.interactors.core.gate.IdFactory;
import net.readybid.app.core.service.traveldestination.CreateTravelDestinationService;
import net.readybid.app.persistence.IdFactoryImpl;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.exceptions.NotAllowedException;
import net.readybid.web.ActionResponse;
import net.readybid.web.ActionResponseAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("[ CreateTravelDestination ] Controller: ")
class CreateTravelDestinationControllerTest {

    @Nested
    @DisplayName("When Creating in Bulk, ")
    class CreateTravelDestinationsInBulkTest {

        private CreateTravelDestinationService createTravelDestinationService;

        private AuthenticatedUser authenticatedUser;
        private CreateTravelDestinationBulkRequest request;
        private String rfpId;

        private CreateTravelDestinationController controller;
        private ActionResponse creationResult;
        private Executable createDestinationsInBulk =
                () -> creationResult = controller.createTravelDestinationsInBulk(
                        request, rfpId, authenticatedUser);

        @BeforeEach
        void setUp() {
            createTravelDestinationService = mock(CreateTravelDestinationService.class);
            controller = new CreateTravelDestinationController(createTravelDestinationService);

            authenticatedUser = mock(AuthenticatedUser.class);
            request = mock(CreateTravelDestinationBulkRequest.class);
            rfpId = RbRandom.alphanumeric(50, false);
        }


        @Test
        @DisplayName("it should Create all Travel Destinations from Request for RFP")
        void createTravelDestinationsTest() throws Throwable {
            final List<CreateTravelDestinationRequest> creationModels = new ArrayList<>();
            doReturn(creationModels)
                    .when(request).getModels(same(rfpId), same(authenticatedUser));

            createDestinationsInBulk.execute();

            verify(createTravelDestinationService, times(1))
                    .createAll(same(rfpId), same(creationModels));
        }

        @Test
        @DisplayName("it should list IDs for all created Travel Destinations in response")
        void listIdsInResponseTest() throws Throwable {
            final List<String> createdDestinationsIds = createCreatedDestinationsIdsMock();

            doReturn(createdDestinationsIds)
                    .when(createTravelDestinationService).createAll(anyString(), any());

            createDestinationsInBulk.execute();

            ActionResponseAssert.that(creationResult)
                    .hasOkStatus()
                    .hasTime();

            //noinspection unchecked
            final List<String> responseIds = (List<String>) creationResult.data.get("ids");
            assertEquals(createdDestinationsIds.size(), responseIds.size());
            assertTrue(responseIds.containsAll(createdDestinationsIds));
        }

        private List<String> createCreatedDestinationsIdsMock() {
            final List<String> createdDestinationsMock = new ArrayList<>();
            final IdFactory idFactory = new IdFactoryImpl();

            for(int i = RbRandom.randomInt(10); i>=0; i--){
                createdDestinationsMock.add(idFactory.create());
            }

            return createdDestinationsMock;
        }

        @Test
        @DisplayName("it should Deny Access if User does not have Update Access Permission on RFP")
        void accessDeniedTest(){
            doThrow(NotAllowedException.class)
                    .when(createTravelDestinationService)
                    .createAll(anyString(), anyList());

            assertThrows(NotAllowedException.class, createDestinationsInBulk);
        }
    }
}