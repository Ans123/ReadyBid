package net.readybid.api.main.entity;

import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.entities.hotel.web.HotelSearchResultView;
import net.readybid.entities.hotel.web.HotelService;
import net.readybid.entities.hotel.web.HotelViewModel;
import net.readybid.utils.ListResult;
import net.readybid.web.GetResponse;
import net.readybid.web.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by DejanK on 11/10/2016.
 *
 */
@RestController
@RequestMapping(value = "/hotels")
public class HotelController {

    private final HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ListResponse<HotelSearchResultView> searchEntities(
            @RequestParam(value = "query", required = true, defaultValue = "") String query,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        ListResponse<HotelSearchResultView> sr = new ListResponse<>();
        ListResult<HotelSearchResultView> result = hotelService.searchHotels(query, page);
        return sr.finalizeResult(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public GetResponse<Hotel, HotelViewModel> handleGetHotelProfileRequest(@PathVariable String id,
                                                                    @RequestParam(value = "include-unverified", required = false, defaultValue = "") String includeUnverifiedParam) {
        final GetResponse<Hotel, HotelViewModel> getResponse = new GetResponse<>();
        final Hotel hotel = includeUnverifiedParam.equals("true") ? hotelService.findHotelByIdIncludingUnverified(id) : hotelService.findHotelById(id);
        return getResponse.finalizeResult(HotelViewModel.FACTORY.createView(hotel));
    }
}
