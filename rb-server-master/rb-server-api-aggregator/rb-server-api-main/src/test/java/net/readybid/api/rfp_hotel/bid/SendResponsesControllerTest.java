package net.readybid.api.rfp_hotel.bid;

import net.readybid.api.main.access.BidAccessControlService;
import net.readybid.app.use_cases.rfp_hotel.bid.SetDraftResponsesHandler;
import net.readybid.app.use_cases.rfp_hotel.bid.SetResponsesHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.test_utils.RbRandom;
import net.readybid.web.RbViewModel;
import org.assertj.core.api.MapAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class SendResponsesControllerTest {

    private SendResponsesController sut;

    private BidAccessControlService bidAccessControlService;
    private SetDraftResponsesHandler setDraftResponsesHandler;
    private SetResponsesHandler setResponsesHandler;

    @BeforeEach
    void setUp(){
        bidAccessControlService = mock(BidAccessControlService.class);
        setDraftResponsesHandler = mock(SetDraftResponsesHandler.class);
        setResponsesHandler = mock(SetResponsesHandler.class);

        sut = new SendResponsesController(bidAccessControlService, setDraftResponsesHandler, setResponsesHandler);
    }

    @Nested
    class SetDraftResponsesTest {

        private SendResponsesController.SaveResponsesWebRequest saveAsDraftWebRequest;
        private AuthenticatedUser currentUser;

        private Supplier<RbViewModel> mut = () -> sut.setDraftResponses(saveAsDraftWebRequest, currentUser);

        @BeforeEach
        void setup() {
            saveAsDraftWebRequest = WebRequestFactory.random();
            currentUser = mock(AuthenticatedUser.class);
        }

        @Test
        void shouldVerifyPermissionsForEachBid(){
            mut.get();
            verify(bidAccessControlService, times(1)).updateAsSupplier(same(saveAsDraftWebRequest.bidsIds));
        }

        @Test
        void shouldTransformAnswersToStrings(){
            //noinspection unchecked
            final ArgumentCaptor<List<Map<String,String>>> responsesCaptor = ArgumentCaptor.forClass(List.class);
            final Map<String, Object> requestResponseMap = saveAsDraftWebRequest.responses.get(0);

            mut.get();
            verify(setDraftResponsesHandler, times(1))
                    .setResponses(same(saveAsDraftWebRequest.bidsIds), responsesCaptor.capture(), same(currentUser));

            final Map<String, String> response = responsesCaptor.getValue().get(0);
            new MapAssert<>(response)
                    .containsEntry("string", String.valueOf(requestResponseMap.get("string")))
                    .containsEntry("integer", String.valueOf(requestResponseMap.get("integer")))
                    .containsEntry("long", String.valueOf(requestResponseMap.get("long")))
                    .containsEntry("double", String.valueOf(requestResponseMap.get("double")))
                    .containsEntry("date", String.valueOf(requestResponseMap.get("date")))
                    .containsEntry("boolean", String.valueOf(requestResponseMap.get("boolean")))
                    .doesNotContainKeys("null", "empty");
        }

        @Test
        void shouldUseSetDraftResponseHandler(){
            mut.get();
            verify(setDraftResponsesHandler, times(1))
                    .setResponses(same(saveAsDraftWebRequest.bidsIds), eq(saveAsDraftWebRequest.getResponses()), same(currentUser));
        }

        @Test
        void shouldReturnReportGeneratedByHandler(){
            final RbViewModel expectedViewModel = mock(RbViewModel.class);
            doReturn(expectedViewModel).when(setDraftResponsesHandler)
                    .setResponses(same(saveAsDraftWebRequest.bidsIds), eq(saveAsDraftWebRequest.getResponses()), same(currentUser));

            final RbViewModel result = mut.get();
            assertSame(expectedViewModel, result);
        }
    }

    public static class WebRequestFactory {

        public static SendResponsesController.SaveResponsesWebRequest random(){
            final SendResponsesController.SaveResponsesWebRequest webRequest = new SendResponsesController.SaveResponsesWebRequest();
            webRequest.bidsIds = RbRandom.listOfIdsAsStrings();
            webRequest.responses = Arrays.asList(randomResponse(), randomResponse());
            return webRequest;
        }

        private static Map<String, Object> randomResponse() {
            final Map<String, Object> response = new HashMap<>();

            response.put("string", RbRandom.alphanumeric(100));
            response.put("integer", RbRandom.randomInt(10));
            response.put("long", RbRandom.randomLong(10));
            response.put("double", RbRandom.randomDouble());
            response.put("date", RbRandom.date());
            response.put("boolean", RbRandom.bool());
            response.put("null", null);
            response.put("empty", "");

            return response;
        }
    }
}

