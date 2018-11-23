package net.readybid.app.core.transaction;

import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.app.core.service.LoadHotelRepository;
import net.readybid.app.core.service.defaultresponse.GetDefaultResponseService;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpDefaultResponseLoader;
import net.readybid.entities.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetDefaultResponseTransaction implements GetDefaultResponseService {

    private final LoadHotelRepository loadHotelRepository;
    private final HotelRfpDefaultResponseLoader defaultResponseLoader;

    @Autowired
    public GetDefaultResponseTransaction(
            LoadHotelRepository loadHotelRepository,
            HotelRfpDefaultResponseLoader defaultResponseLoader
    ) {
        this.defaultResponseLoader = defaultResponseLoader;
        this.loadHotelRepository = loadHotelRepository;
    }

    @Override
    public HotelRfpDefaultResponse forHotel(String hotelId) {
        final Id id = Id.valueOf(hotelId);
        HotelRfpDefaultResponse defaultResponse = defaultResponseLoader.get(id);
        if(null == defaultResponse) {
            final Hotel hotel = loadHotelRepository.getAnswersById(hotelId);
            if(null != hotel){
                defaultResponse = new HotelRfpDefaultResponse(id, hotel.getAnswers());
            }
        }
        if(null == defaultResponse) throw new NotFoundException();
        return defaultResponse;
    }
}
