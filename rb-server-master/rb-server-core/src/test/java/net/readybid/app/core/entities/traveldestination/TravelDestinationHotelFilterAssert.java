package net.readybid.app.core.entities.traveldestination;

import net.readybid.test_utils.RbAbstractAssert;

public class TravelDestinationHotelFilterAssert extends RbAbstractAssert<TravelDestinationHotelFilterAssert, TravelDestinationHotelFilter> {

    public static TravelDestinationHotelFilterAssert that(TravelDestinationHotelFilter actual) {
        return new TravelDestinationHotelFilterAssert(actual, TravelDestinationHotelFilterAssert.class);
    }

    private TravelDestinationHotelFilterAssert(TravelDestinationHotelFilter actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public TravelDestinationHotelFilterAssert hasMaxDistanceValue(Object expected) {
        assertFieldEquals("maxDistance distance", expected, actual.maxDistance.getDistance());
        return this;
    }

    public TravelDestinationHotelFilterAssert hasMaxDistanceUnit(Object expected) {
        assertFieldEquals("maxDistance unit", expected, actual.maxDistance.getDistanceUnit());
        return this;
    }

    public TravelDestinationHotelFilterAssert hasAmenities(Object expected) {
        assertFieldEquals("amenities", expected, actual.amenities);
        return this;
    }

    public TravelDestinationHotelFilterAssert hasChains(Object expected) {
        assertFieldEquals("chains", expected, actual.chains);
        return this;
    }
}