package net.readybid.api.rfp_hotel.bid.supplier.contact;

import net.readybid.app.use_cases.rfp_hotel.ListHotelRfpRepresentatives;
import net.readybid.web.RbResponseView;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/rfps/hotel/representatives/")
public class HotelRfpRepresentativesController {

    private final ListHotelRfpRepresentatives listHotelRfpRepresentatives;

    @Autowired
    public HotelRfpRepresentativesController(ListHotelRfpRepresentatives listHotelRfpRepresentatives) {
        this.listHotelRfpRepresentatives = listHotelRfpRepresentatives;
    }

    @RbResponseView
    @GetMapping(value = "chain/{chainId}")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel forChain(
            @PathVariable(value = "chainId") String chainId
    ) {
        return listHotelRfpRepresentatives.forChain(chainId);
    }

    @RbResponseView
    @GetMapping(value = "hotel/{hotelId}")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel forHotel(
            @PathVariable(value = "hotelId") String hotelId
    ) {
        return listHotelRfpRepresentatives.forHotel(hotelId);
    }
}
