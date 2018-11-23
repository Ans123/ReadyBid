package net.readybid.app.interactors.rfp_hotel.bid.response.implementation;

import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.app.interactors.core.entity.gate.HotelLoader;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.test_utils.RbRandom;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static net.readybid.app.interactors.rfp_hotel.bid.response.implementation.HotelRfpBidResponseToHotelMatcher.CODES_TO_CHECK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class HotelRfpBidResponseToHotelMatcherTest {

    private static final String PROPCODE = "PROPCODE";
    private HotelRfpBidResponseToHotelMatcher sut;

    private HotelLoader hotelLoader;

    @BeforeEach
    void setup(){
        generateSUTDependenciesAsMocks();
        createSUT();
    }

    @Nested
    class MatchBidsToResponsesTest{

        private List<HotelRfpBid> bids;
        private List<Map<String, String>> responses;

        private Map<HotelRfpBid, Map<String, String>> result;
        private Runnable mut = () -> result = sut.matchBidsToResponses(bids, responses);

        @Nested
        class MatchBidsWithResponsesUsingGdsPropCodesTest{

            HotelRfpBid bidWithAlphanumericGdsCode;
            HotelRfpBid bidWithNumericGdsCode;

            Hotel hotelWithAlphanumericGdsCode;
            Hotel hotelWithNumericGdsCode;

            Map<String, String> responseWithAlphanumericGdsCode;
            Map<String, String> responseWithNumericGdsCode;

            @Test
            void matchBidsWithResponsesUsingGdsPropCodesAndAddReadyBidPropCode(){
                setupAlphanumericMatch();
                setupNumericMatch();
                bids = new ArrayList<>(Arrays.asList(bidWithAlphanumericGdsCode, bidWithNumericGdsCode));
                responses = new ArrayList<>(Arrays.asList(responseWithNumericGdsCode, responseWithAlphanumericGdsCode));
                hotelLoaderReturnsHotelsWithGdsCodes();

                mut.run();

                isResponseWithAlphanumericGdsCodeMatchedWithBid();
                isResponseWithNumericGdsCodeMatchedWithBid();
                areResponsesContainingReadyBidPropCode();
            }

            private void setupNumericMatch() {
                hotelWithNumericGdsCode = mock(Hotel.class);
                bidWithNumericGdsCode = mock(HotelRfpBid.class);
                responseWithNumericGdsCode = new HashMap<>();

                setupHotelId(hotelWithNumericGdsCode, bidWithNumericGdsCode);
                setupBidId(bidWithNumericGdsCode);

                final String numericCode = String.valueOf(RbRandom.randomLong(1000000L));
                setupHotelAndResponseAnswers(hotelWithNumericGdsCode, responseWithNumericGdsCode, numericCode, "000"+numericCode);
            }

            private void setupAlphanumericMatch() {
                hotelWithAlphanumericGdsCode = mock(Hotel.class);
                bidWithAlphanumericGdsCode = mock(HotelRfpBid.class);
                responseWithAlphanumericGdsCode = new HashMap<>();

                setupHotelId(hotelWithAlphanumericGdsCode, bidWithAlphanumericGdsCode);
                setupBidId(bidWithAlphanumericGdsCode);

                final String alphaNumericCode = RbRandom.alphanumeric(50);
                setupHotelAndResponseAnswers(hotelWithAlphanumericGdsCode, responseWithAlphanumericGdsCode, alphaNumericCode, alphaNumericCode);
            }

            private void setupBidId(HotelRfpBid bidWithAlphanumericGdsCode) {
                final ObjectId bidId = RbRandom.oid();
                doReturn(bidId).when(bidWithAlphanumericGdsCode).getId();
            }

            private void setupHotelId(Hotel hotelWithAlphanumericGdsCode, HotelRfpBid bidWithAlphanumericGdsCode) {
                final ObjectId hotelId = RbRandom.oid();
                doReturn(hotelId).when(hotelWithAlphanumericGdsCode).getId();
                doReturn(hotelId).when(bidWithAlphanumericGdsCode).getSupplierCompanyEntityId();
            }

            private void hotelLoaderReturnsHotelsWithGdsCodes() {
                doReturn(Arrays.asList(hotelWithAlphanumericGdsCode, hotelWithNumericGdsCode))
                        .when(hotelLoader).getPropertyCodes(anyList());
            }

            private void setupHotelAndResponseAnswers(Hotel hotel, Map<String, String> responseAnswers, String codeInHotelAnswers, String codeInResponseAnswers) {
                final String gdsCodeUsed = RbRandom.randomFromList(CODES_TO_CHECK);
                final Map<String, String> hotelAnswers = new HashMap<>();

                doReturn(hotelAnswers).when(hotel).getAnswers();
                hotelAnswers.put(PROPCODE, RbRandom.alphanumeric());
                hotelAnswers.put(gdsCodeUsed, codeInHotelAnswers);
                responseAnswers.put(gdsCodeUsed, codeInResponseAnswers);
            }

            private void isResponseWithAlphanumericGdsCodeMatchedWithBid() {
                assertSame(responseWithAlphanumericGdsCode, result.get(bidWithAlphanumericGdsCode));
            }

            private void isResponseWithNumericGdsCodeMatchedWithBid() {
                assertSame(responseWithNumericGdsCode, result.get(bidWithNumericGdsCode));
            }

            private void areResponsesContainingReadyBidPropCode(){
                assertEquals( hotelWithAlphanumericGdsCode.getAnswers().get(PROPCODE), responseWithAlphanumericGdsCode.get(PROPCODE));
                assertEquals( hotelWithNumericGdsCode.getAnswers().get(PROPCODE) , responseWithNumericGdsCode.get(PROPCODE));
            }
        }

        @Nested
        class WhenNoMatchesExistTest {

            HotelRfpBid bid_1;
            HotelRfpBid bid_2;

            Hotel hotel_1;
            Hotel hotel_2;

            Map<String, String> response_1;
            Map<String, String> response_2;


            @Test
            void whenNoMatchesExistReturnEmptyMap() {
                setupNoMatches();
                bids = new ArrayList<>(Arrays.asList(bid_1, bid_2));
                responses = new ArrayList<>(Arrays.asList(response_1, response_2));
                hotelLoaderReturnsHotelsWithGdsCodes();

                mut.run();

                assertTrue(result.isEmpty());
            }

            private void setupNoMatches() {
                hotel_1 = mock(Hotel.class);
                hotel_2 = mock(Hotel.class);
                bid_1 = mock(HotelRfpBid.class);
                bid_2 = mock(HotelRfpBid.class);
                response_1 = new HashMap<>();
                response_2 = new HashMap<>();

                setupHotelId(hotel_1, bid_1);
                setupHotelId(hotel_2, bid_2);

                setupBidId(bid_1);
                setupBidId(bid_2);

                setupHotelAndResponseAnswersToNotMatch(hotel_1, response_1, RbRandom.alphanumeric(50, true));
                setupHotelAndResponseAnswersToNotMatch(hotel_2, response_2, RbRandom.alphanumeric(51, true));
            }

            private void setupBidId(HotelRfpBid bid) {
                final ObjectId bidId = RbRandom.oid();
                doReturn(bidId).when(bid).getId();
            }

            private void setupHotelId(Hotel hotel, HotelRfpBid bid) {
                final ObjectId hotelId = RbRandom.oid();
                doReturn(hotelId).when(hotel).getId();
                doReturn(hotelId).when(bid).getSupplierCompanyEntityId();
            }

            private void setupHotelAndResponseAnswersToNotMatch(Hotel hotel, Map<String, String> responseAnswers, String code) {
                final String gdsCodeUsed = RbRandom.randomFromList(CODES_TO_CHECK);
                final Map<String, String> hotelAnswers = new HashMap<>();

                doReturn(hotelAnswers).when(hotel).getAnswers();
                hotelAnswers.put(PROPCODE, RbRandom.alphanumeric());
                hotelAnswers.put(gdsCodeUsed, code);
                responseAnswers.put(gdsCodeUsed, code + RbRandom.alphanumeric(5));
            }

            private void hotelLoaderReturnsHotelsWithGdsCodes() {
                doReturn(Arrays.asList(hotel_1, hotel_2))
                        .when(hotelLoader).getPropertyCodes(anyList());
            }
        }

        @Test
        void whenBidsAreNull(){
            bids = null;
            responses = new ArrayList<>(Collections.singletonList(new HashMap<>()));

            mut.run();

            assertTrue(result.isEmpty());
        }

        @Test
        void whenBidsIdsIsEmpty(){
            bids = new ArrayList<>();
            responses = new ArrayList<>(Collections.singletonList(new HashMap<>()));

            mut.run();

            assertTrue(result.isEmpty());
        }

        @Test
        void whenResponsesIsNull(){
            bids = new ArrayList<>(Collections.singletonList(mock(HotelRfpBid.class)));
            responses = null;

            mut.run();

            assertTrue(result.isEmpty());
        }

        @Test
        void whenResponsesIsEmpty(){
            bids = new ArrayList<>(Collections.singletonList(mock(HotelRfpBid.class)));
            responses = new ArrayList<>();

            mut.run();

            assertTrue(result.isEmpty());
        }
    }

    private void generateSUTDependenciesAsMocks() {
        hotelLoader = mock(HotelLoader .class);
    }

    private void createSUT() {
        sut = new HotelRfpBidResponseToHotelMatcher(hotelLoader);
    }
}