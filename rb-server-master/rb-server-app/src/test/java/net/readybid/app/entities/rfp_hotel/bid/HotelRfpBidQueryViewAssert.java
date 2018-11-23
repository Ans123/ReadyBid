package net.readybid.app.entities.rfp_hotel.bid;

import net.readybid.test_utils.RbAbstractAssert;
import net.readybid.test_utils.RbMapAssert;

import java.util.function.Consumer;

public class HotelRfpBidQueryViewAssert extends RbAbstractAssert<HotelRfpBidQueryViewAssert, HotelRfpBidQueryView> {

    public static HotelRfpBidQueryViewAssert that(HotelRfpBidQueryView actual) {
        return new HotelRfpBidQueryViewAssert(actual);
    }

    private HotelRfpBidQueryViewAssert(HotelRfpBidQueryView actual) {
        super(actual, HotelRfpBidQueryViewAssert.class);
    }

    public HotelRfpBidQueryViewAssert hasBidId(Object expected) {
        assertFieldEquals("$bidId", actual.$bidId, expected);
        return this;
    }

    public HotelRfpBidQueryViewAssert hasRfpId(Object expected) {
        assertFieldEquals("$rfpId", actual.$rfpId, expected);
        return this;
    }

    public HotelRfpBidQueryViewAssert hasStatus(Object expected) {
        assertFieldEquals("$status", String.valueOf(actual.$status), String.valueOf(expected));
        return this;
    }

    public HotelRfpBidQueryViewAssert hasHotelId(Object expected) {
        assertFieldEquals("$hotelId", actual.$hotelId, expected);
        return this;
    }

    public HotelRfpBidQueryViewAssert hasChainId(Object expected) {
        assertFieldEquals("$chainId", actual.$chainId, expected);
        return this;
    }

    public HotelRfpBidQueryViewAssert assertSupplierContact(Consumer<RbMapAssert<String, Object>> that) {
        assertMap(actual.$supplierContact, that);
        return this;
    }

    public HotelRfpBidQueryViewAssert hasId(Object expected) {
        assertFieldEquals("_Id", actual._id, expected);
        return this;
    }

    public HotelRfpBidQueryViewAssert assertRfp(Consumer<RbMapAssert<String, Object>> that) {
        assertMap(actual.rfp, that);
        return this;
    }

    public HotelRfpBidQueryViewAssert assertSubject(Consumer<RbMapAssert<String, Object>> that) {
        assertMap(actual.subject, that);
        return this;
    }

    public HotelRfpBidQueryViewAssert assertBuyer(Consumer<RbMapAssert<String, Object>> that) {
        assertMap(actual.buyer, that);
        return this;
    }

    public HotelRfpBidQueryViewAssert assertSupplier(Consumer<RbMapAssert<String, Object>> that) {
        assertMap(actual.supplier, that);
        return this;
    }

    public HotelRfpBidQueryViewAssert assertAnalytics(Consumer<RbMapAssert<String, Object>> that) {
        assertMap(actual.analytics, that);
        return this;
    }

    public HotelRfpBidQueryViewAssert assertOffer(Consumer<RbMapAssert<String, Object>> that) {
        assertMap(actual.analytics, that);
        return this;
    }

    public HotelRfpBidQueryViewAssert assertStatus(Consumer<RbMapAssert<String, Object>> that) {
        assertMap(actual.analytics, that);
        return this;
    }
}