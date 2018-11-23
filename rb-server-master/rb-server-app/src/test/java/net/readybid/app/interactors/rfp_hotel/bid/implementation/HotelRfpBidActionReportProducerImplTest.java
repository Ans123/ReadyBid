package net.readybid.app.interactors.rfp_hotel.bid.implementation;

import net.readybid.app.entities.core.ActionReport;
import net.readybid.app.entities.core.ActionReportAssert;
import net.readybid.app.entities.core.ActionReportBuilder;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.interactors.rfp_hotel.bid.gate.HotelRfpBidQueryViewLoader;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entities.Id;
import net.readybid.rfphotel.bid.core.HotelRfpBidSimpleState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.*;

@DisplayName("Hotel Rfp Bid Action Report Producer Impl")
class HotelRfpBidActionReportProducerImplTest {

    private HotelRfpBidActionReportProducerImpl sut;

    private HotelRfpBidQueryViewLoader bidLoader;

    @BeforeEach
    void setUp(){
        bidLoader = mock(HotelRfpBidQueryViewLoader.class);

        sut = new HotelRfpBidActionReportProducerImpl(bidLoader);
    }

    @Nested
    @DisplayName("create with custom ActionReportBuilder")
    class CreateWithReportBuilderTest {

        private List<Id> bidsIds;
        private AuthenticatedUser currentUser;
        private ActionReportBuilder<HotelRfpBidQueryView> actionReportBuilder;

        private List<ActionReport<HotelRfpBidQueryView>> result;
        private Runnable mut = () -> result = sut.create(bidsIds, currentUser, actionReportBuilder);

        @Test
        void whenBidsIdsExist(){
            setBidsIds();
            setCurrentUser();
            setReportBuilder();

            mut.run();

            areBidsLoadedFromDatabase();
            isReportBuiltForEachBidId();
            isResultContainingAllReports();
        }

        private void isResultContainingAllReports() {
            assertEquals(bidsIds.size(), result.size());
        }

        private void isReportBuiltForEachBidId() {
            verify(actionReportBuilder, times(bidsIds.size())).build(any(Id.class), any());
        }

        private void areBidsLoadedFromDatabase() {
            verify(bidLoader, times(1)).find(same(bidsIds), same(currentUser));
        }

        private void setReportBuilder() {
            //noinspection unchecked
            actionReportBuilder = mock(ActionReportBuilder.class);
        }

        private void setCurrentUser() {
            currentUser = mock(AuthenticatedUser.class);
        }

        private void setBidsIds() {
            bidsIds = RbRandom.listOfIds();
        }

    }

    @Nested
    @DisplayName("create")
    class CreateTest {

        private Id bidId;
        private AuthenticatedUser currentUser;
        private HotelRfpBidQueryView bid;

        private List<ActionReport<HotelRfpBidQueryView>> result;
        private Runnable mut = () -> result = sut.create(bidId, currentUser);

        @Test
        void whenBidsIdsExist(){
            setBidId();
            setCurrentUser();
            setBidLoaderToLoadBid();

            mut.run();

            isBidLoadedFromDatabase();
            isResultContainingSuccessReport();
        }

        private void setBidId() {
            bidId = RbRandom.id();
        }

        private void setCurrentUser() {
            currentUser = mock(AuthenticatedUser.class);
        }

        private void setBidLoaderToLoadBid() {
            final HotelRfpBidSimpleState bidState = new HotelRfpBidSimpleState();
            bidState.setStatus(HotelRfpBidStateStatus.CREATED);

            bid = new HotelRfpBidQueryView.Builder()
            .setId(bidId.value)
            .setState(bidState)
            .buildBuyerView();

            doReturn(Collections.singletonList(bid)).when(bidLoader).find(anyList(),same(currentUser));
        }

        private void isBidLoadedFromDatabase() {
            verify(bidLoader, times(1)).find(anyList(), same(currentUser));
        }

        private void isResultContainingSuccessReport() {
            assertEquals(1, result.size());
            ActionReportAssert.that(result.get(0))
                    .hasStatus(ActionReport.Status.OK)
                    .hasMessage(null)
                    .hasObject(bid);
        }

    }
}