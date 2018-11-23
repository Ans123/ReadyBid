package net.readybid.api.hotelrfp.traveldestination;

import net.readybid.web.RbResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SearchForHotelsController {

    private final TravelDestinationManagerHotelViewModelFactory travelDestinationManagerHotelViewModelFactory;

    @Autowired
    public SearchForHotelsController(
            TravelDestinationManagerHotelViewModelFactory travelDestinationManagerHotelViewModelFactory
    ) {
        this.travelDestinationManagerHotelViewModelFactory = travelDestinationManagerHotelViewModelFactory;
    }

    @RequestMapping(value = "/rfps/{rfpId}/travel-destinations/{destinationId}/geo-search", method = RequestMethod.POST)
    public RbResponse geoSearchTravelDestination(
            @RequestBody @Valid SearchForHotelsRequest searchForHotelsRequest,
            @PathVariable(value = "rfpId") String rfpId,
            @PathVariable(value = "destinationId") String destinationId
    ) {
        return new RbResponse(() -> travelDestinationManagerHotelViewModelFactory.make(rfpId, destinationId, searchForHotelsRequest));
    }
}
