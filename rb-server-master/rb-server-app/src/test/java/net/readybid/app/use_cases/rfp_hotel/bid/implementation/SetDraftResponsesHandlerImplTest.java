package net.readybid.app.use_cases.rfp_hotel.bid.implementation;

import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidActionReportProducer;
import net.readybid.app.interactors.rfp_hotel.bid.response.HotelRfpBidDraftResponseService;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.test_utils.RbRandom;
import net.readybid.web.RbViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

class SetDraftResponsesHandlerImplTest {

    private SetDraftResponsesHandlerImpl sut;

    private HotelRfpBidDraftResponseService responseService;
    private HotelRfpBidActionReportProducer actionReportProducer;

    @BeforeEach
    void setup(){
        responseService = mock(HotelRfpBidDraftResponseService.class);
        actionReportProducer = mock(HotelRfpBidActionReportProducer.class);

        sut = new SetDraftResponsesHandlerImpl(responseService, actionReportProducer);
    }

    @Nested
    class SetResponsesTest{

        private List<String> bidsIds;
        private List<Map<String, String>> responses;
        private AuthenticatedUser currentUser;

        private Supplier<RbViewModel> mut = () -> sut.setResponses(bidsIds, responses, currentUser);

        @BeforeEach
        void setup(){
            bidsIds = RbRandom.listOfIdsAsStrings();
            //noinspection unchecked
            responses = mock(List.class);
            currentUser = mock(AuthenticatedUser.class);
        }

        @Test
        void shouldSaveResponsesWithDateTimestamp(){
            final Date dateMark = new Date();
            final ArgumentCaptor<Date> dateTimestampCaptor = ArgumentCaptor.forClass(Date.class);

            mut.get();

            verify(responseService, times(1))
                    .saveDraftResponses(same(bidsIds), same(responses), dateTimestampCaptor.capture(), same(currentUser));

            assertTrue( ! dateMark.after(dateTimestampCaptor.getValue()));
        }
    }
}