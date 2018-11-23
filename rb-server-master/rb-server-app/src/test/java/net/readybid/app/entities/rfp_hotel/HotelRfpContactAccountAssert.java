package net.readybid.app.entities.rfp_hotel;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.entities.core.LocationAssert;
import net.readybid.entities.Id;
import net.readybid.test_utils.RbAbstractAssert;

import java.util.function.Consumer;

public class HotelRfpContactAccountAssert extends RbAbstractAssert<HotelRfpContactAccountAssert, HotelRfpContactAccount> {

    public static HotelRfpContactAccountAssert that(HotelRfpContactAccount actual) {
        return new HotelRfpContactAccountAssert(actual);
    }

    private HotelRfpContactAccountAssert(HotelRfpContactAccount actual) {
        super(actual, HotelRfpContactAccountAssert.class);
    }

    public HotelRfpContactAccountAssert hasAccountId(Object expected) {
        assertFieldEquals("account id", actual.accountId, Id.valueOf(expected));
        return this;
    }

    public HotelRfpContactAccountAssert hasEntityId(Object expected) {
        assertFieldEquals("entity id", actual.entityId, Id.valueOf(expected));
        return this;
    }

    public HotelRfpContactAccountAssert hasType(Object expected) {
        assertFieldEquals("type", actual.type, EntityType.valueOf(String.valueOf(expected)));
        return this;
    }

    public HotelRfpContactAccountAssert hasName(Object expected) {
        assertFieldEquals("name", actual.name, expected);
        return this;
    }

    public HotelRfpContactAccountAssert hasWebsite(Object expected) {
        assertFieldEquals("website", actual.website, expected);
        return this;
    }

    public HotelRfpContactAccountAssert hasEmailAddress(Object expected) {
        assertFieldEquals("email address", actual.emailAddress, expected);
        return this;
    }

    public HotelRfpContactAccountAssert hasPhone(Object expected) {
        assertFieldEquals("phone", actual.phone, expected);
        return this;
    }

    public HotelRfpContactAccountAssert hasLogo(Object expected) {
        assertFieldEquals("logo", actual.logo, expected);
        return this;
    }

    public HotelRfpContactAccountAssert hasLocation(Consumer<LocationAssert> consumer) {
        consumer.accept(LocationAssert.that(actual.location));
        return this;
    }
}