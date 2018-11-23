package net.readybid.api.hotelrfp.traveldestination;

import java.util.List;

public interface FindHotelsNearCoordinatesRepository {
    List<TravelDestinationManagerHotelViewModel> search(List coordinates, double maxDistance, List<String> chains);
}
