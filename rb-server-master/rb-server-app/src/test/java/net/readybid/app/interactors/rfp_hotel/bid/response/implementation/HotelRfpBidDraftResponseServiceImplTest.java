package net.readybid.app.interactors.rfp_hotel.bid.response.implementation;

import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidStateFactory;
import net.readybid.app.interactors.rfp_hotel.bid.gate.HotelRfpBidResponseStorageManager;
import net.readybid.app.interactors.rfp_hotel.bid.implementation.HotelRfpBidStateFactoryImpl;
import net.readybid.app.interactors.rfp_hotel.bid.response.gate.HotelRfpBidResponseLoader;
import net.readybid.entity_factories.BasicUserDetailsImplFactory;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.rfphotel.bid.core.HotelRfpBidStatusDetails;
import net.readybid.test_utils.RbRandom;
import net.readybid.user.BasicUserDetails;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

class HotelRfpBidDraftResponseServiceImplTest {

    private HotelRfpBidDraftResponseServiceImpl sut;

    private HotelRfpBidResponseStorageManager responseStorageManager;
    private HotelRfpBidResponseLoader bidLoader;
    private HotelRfpBidResponseToHotelMatcher bidMatcher;
    private HotelRfpBidQuestionnaireResponseProducerImpl responseProducer;
    private HotelRfpBidStateFactory bidStateFactory;

    @BeforeEach
    void setup(){
        generateSUTDependenciesAsMocks();
        createSUT();
    }

    @Nested
    class SaveDraftResponsesTest{

        private List<String> bidsIds;
        private List<Map<String, String>> responses;
        private Date responsesAt;
        private BasicUserDetails currentUser;

        private List<HotelRfpBid> result;
        private Runnable mut = () -> result = sut.saveDraftResponses(bidsIds, responses, responsesAt, currentUser);

        private ObjectId bidId_1 = new ObjectId();
        private HotelRfpBid bid_1 = mock(HotelRfpBid.class);
        private Map<String, String> bidAnswers_1 = new HashMap<>();
        private QuestionnaireResponse bidResponse_1 = mock(QuestionnaireResponse.class);

        private ObjectId bidId_2 = new ObjectId();
        private HotelRfpBid bid_2 = mock(HotelRfpBid.class);
        private Map<String, String> bidAnswers_2 = new HashMap<>();
        private QuestionnaireResponse bidResponse_2 = mock(QuestionnaireResponse.class);

        private List<HotelRfpBid> bids = Arrays.asList(bid_1, bid_2);

        @BeforeEach
        void setup(){
            generateDefaultMUTDependencies();
        }

        @Test
        void whenBidsAreMatchedAndResponsesProduced(){
            bidLoaderLoadsBids();
            matcherMatchesBidsAndResponses();
            producerProducesQuestionnaireResponses();

            mut.run();

            haveResponsesBeenSaved();
            isResultContainingAllBidsAndResponsesWithReceivedState();
        }

        @Test
        void whenBidsAreNotMatched(){
            mut.run();

            haveResponsesNotBeingSaved();
            isResultEmpty();
        }

        @Test
        void whenBidsAreMatchedAndResponsesAreNotProduced(){
            bidLoaderLoadsBids();
            matcherMatchesBidsAndResponses();

            mut.run();

            haveResponsesNotBeingSaved();
            isResultEmpty();
        }

        @Test
        void whenBidsIdsIsNull(){
            bidsIds = null;
            mut.run();
            haveResponsesNotBeingSaved();
            isResultEmpty();
        }

        @Test
        void whenBidsIdsIsEmpty(){
            bidsIds = new ArrayList<>();
            mut.run();
            haveResponsesNotBeingSaved();
            isResultEmpty();
        }

        @Test
        void whenResponsesIsNull(){
            responses = null;
            mut.run();
            haveResponsesNotBeingSaved();
            isResultEmpty();
        }

        @Test
        void whenResponsesIsEmpty(){
            responses = new ArrayList<>();
            mut.run();
            haveResponsesNotBeingSaved();
            isResultEmpty();
        }

        @Test
        void whenResponseAtIsNull(){
            responsesAt = null;
            mut.run();
            haveResponsesNotBeingSaved();
            isResultEmpty();
        }

        @Test
        void whenCurrentUserIsNull(){
            currentUser = null;
            mut.run();
            haveResponsesNotBeingSaved();
            isResultEmpty();
        }

        private void isResultEmpty() {
            assertTrue(result.isEmpty());
        }

        private void haveResponsesNotBeingSaved() {
            verify(responseStorageManager, times(0)).saveDraftResponses(anyList());
        }

        private void haveResponsesBeenSaved() {
            verify(responseStorageManager, times(1)).saveDraftResponses(same(result));
        }

        private void isResultContainingAllBidsAndResponsesWithReceivedState() {
            isResultContainingBidAndResponse(bidId_1, bidResponse_1);
            isResultContainingBidAndResponse(bidId_2, bidResponse_2);
        }

        private void isResultContainingBidAndResponse(ObjectId bidId, QuestionnaireResponse bidResponse) {
            final Optional<HotelRfpBid> optionalBid = result.stream().filter(b -> bidId.equals(b.getId())).findFirst();
            assertTrue(optionalBid.isPresent());
            final HotelRfpBid bid = optionalBid.get();
            assertSame(bidResponse, bid.getResponseDraft());

            final HotelRfpBidStatusDetails state = bid.getState().getSupplierStatusDetails();
            assertEquals(HotelRfpBidStateStatus.RECEIVED, state.status);
            assertSame(responsesAt, state.at);
            assertSame(currentUser, state.by);
        }

        private void generateDefaultMUTDependencies() {
            bidsIds = RbRandom.listOfIdsAsStrings();
            responses = Arrays.asList(new HashMap<>(), new HashMap<>());
            responsesAt = new Date();
            currentUser = BasicUserDetailsImplFactory.random();
        }

        private void producerProducesQuestionnaireResponses() {
            producerProducesQuestionnaireResponses(bidResponse_1, bidAnswers_1, bid_1);
            producerProducesQuestionnaireResponses(bidResponse_2, bidAnswers_2, bid_2);
        }

        private void producerProducesQuestionnaireResponses(QuestionnaireResponse response, Map<String, String> answers, HotelRfpBid bid) {
            doReturn(response).when(responseProducer).prepareDraftResponse(same(answers), same(bid));
        }

        private void bidLoaderLoadsBids(){
            doReturn(bids).when(bidLoader).getBidsWithQuestionnaireHotelIdResponseContextFields(same(bidsIds));
        }

        private void matcherMatchesBidsAndResponses() {
            final Map<HotelRfpBid, Map<String, String>> matchedResponsesMap = new HashMap<>();

            addToMatched(matchedResponsesMap, bidId_1, bid_1, bidAnswers_1);
            addToMatched(matchedResponsesMap, bidId_2, bid_2, bidAnswers_2);

            doReturn(matchedResponsesMap).when(bidMatcher).matchBidsToResponses(same(bids), same(responses));
        }

        private void addToMatched(Map<HotelRfpBid, Map<String, String>> matchedResponsesMap, ObjectId bidId, HotelRfpBid bid, Map<String, String> bidAnswers) {
            doReturn(bidId).when(bid).getId();
            matchedResponsesMap.put(bid, bidAnswers);
        }
    }

    private void createSUT() {
        sut = new HotelRfpBidDraftResponseServiceImpl(responseStorageManager, bidLoader, bidMatcher, responseProducer, bidStateFactory);
    }

    private void generateSUTDependenciesAsMocks() {
        responseStorageManager = mock(HotelRfpBidResponseStorageManager .class);
        bidLoader = mock(HotelRfpBidResponseLoader.class);
        bidMatcher = mock(HotelRfpBidResponseToHotelMatcher.class);
        responseProducer = mock(HotelRfpBidQuestionnaireResponseProducerImpl.class);
        bidStateFactory = new HotelRfpBidStateFactoryImpl();
    }
}