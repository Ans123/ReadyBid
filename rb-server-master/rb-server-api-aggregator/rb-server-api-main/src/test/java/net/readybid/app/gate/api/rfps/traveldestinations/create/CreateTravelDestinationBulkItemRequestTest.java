package net.readybid.app.gate.api.rfps.traveldestinations.create;

import net.readybid.app.core.entities.location.Location;
import net.readybid.app.core.entities.location.LocationImpl;
import net.readybid.location.LocationCityTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@DisplayName("[ CreateTravelDestination ] In Bulk Request Item:")
class CreateTravelDestinationBulkItemRequestTest {

    @DisplayName("When Creating Location")
    @Nested
    class GetLocationEntityTest {

        @DisplayName("it should delegate creation to Location transfer object")
        @Test
        void getLocationEntity() {
            final CreateTravelDestinationBulkItemRequest r = new CreateTravelDestinationBulkItemRequest();
            r.location = mock(LocationCityTO.class);
            final LocationImpl l = mock(LocationImpl.class);
            doReturn(l).when(r.location).toEntity();

            final Location result = r.getLocationEntity();

            verify(r.location, times(1)).toEntity();
            assertSame(l, result);
        }
    }
}