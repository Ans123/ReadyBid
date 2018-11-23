package net.readybid.app.core.entities.traveldestination;

import net.readybid.test_utils.RbAbstractAssert;

import static org.junit.Assert.assertTrue;

public class TravelDestinationImplAssert extends RbAbstractAssert<TravelDestinationImplAssert, TravelDestinationImpl> {

    public static TravelDestinationImplAssert that(TravelDestinationImpl actual) {
        return new TravelDestinationImplAssert(actual, TravelDestinationImplAssert.class);
    }

    public static TravelDestinationImplAssert that(TravelDestination actual) {
        assertTrue("Actual should be instance of TravelDestinationImpl class", actual instanceof TravelDestinationImpl);
        return new TravelDestinationImplAssert((TravelDestinationImpl) actual, TravelDestinationImplAssert.class);
    }

    private TravelDestinationImplAssert(TravelDestinationImpl actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public TravelDestinationImplAssert hasId(Object expected) {
        assertFieldEquals("id", expected, actual.getId());
        return this;
    }

    public TravelDestinationImplAssert hasRfpId(Object expected) {
        assertFieldEquals("rfpId", expected, actual.getRfpId());
        return this;
    }

    public TravelDestinationImplAssert hasType(Object expected) {
        assertFieldEquals("type", expected, actual.getType());
        return this;
    }

    public TravelDestinationImplAssert hasName(Object expected) {
        assertFieldEquals("name", expected, actual.getName());
        return this;
    }

    public TravelDestinationImplAssert hasEstimatedSpend(Object expected) {
        assertFieldEquals("estimatedSpend", expected, actual.getEstimatedSpend());
        return this;
    }

    public TravelDestinationImplAssert hasEstimatedRoomNights(Object expected) {
        assertFieldEquals("estimatedRoomNights", expected, actual.getEstimatedRoomNights());
        return this;
    }

    public TravelDestinationImplAssert hasLocation(Object expected) {
        assertFieldSame("location", expected, actual.getLocation());
        return this;
    }
}