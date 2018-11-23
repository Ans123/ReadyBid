package net.readybid.app.gate.api.rfps.traveldestinations.create;

import net.readybid.test_utils.RbRandom;
import net.readybid.app.core.action.traveldestination.CreateTravelDestinationRequest;
import net.readybid.user.BasicUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DisplayName("[ CreateTravelDestination ] In Bulk Request:")
class CreateTravelDestinationBulkRequestTest {

    @DisplayName("When Creating Models")
    @Nested
    class GetCreateModelTest {

        @DisplayName("it should convert all Create Travel Destinations Requests to Models")
        @Test
        void getModels() {
            final String rfpId = "rfpId";
            final BasicUserDetails currentUser = mock(BasicUserDetails.class);

            final CreateTravelDestinationBulkRequest request = new CreateTravelDestinationBulkRequest();
            request.travelDestinations = createCreateTravelDestinationBulkItemRequestList();

            final List<CreateTravelDestinationRequest> result = request.getModels(rfpId, currentUser);

            assertEquals(request.travelDestinations.size(), result.size(), "should convert all requests to models");
            for(CreateTravelDestinationBulkItemRequest r : request.travelDestinations){
                verify(r, times(1)).getCreateModel(same(rfpId), same(currentUser));
                assertTrue(result.contains(r.getCreateModel(rfpId, currentUser)));
            }
        }

        private List<CreateTravelDestinationBulkItemRequest> createCreateTravelDestinationBulkItemRequestList(){
            final List<CreateTravelDestinationBulkItemRequest> requests = new ArrayList<>();

            for(int i = RbRandom.randomInt(100); i>=0; i--){
                CreateTravelDestinationBulkItemRequest r = mock(CreateTravelDestinationBulkItemRequest.class);
                doReturn(mock(CreateTravelDestinationRequest.class))
                        .when(r).getCreateModel(anyString(), any(BasicUserDetails.class));
                requests.add(mock(CreateTravelDestinationBulkItemRequest.class));
            }

            return requests;
        }
    }
}