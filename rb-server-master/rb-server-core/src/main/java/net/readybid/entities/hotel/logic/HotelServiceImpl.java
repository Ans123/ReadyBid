package net.readybid.entities.hotel.logic;

import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.entities.hotel.web.HotelSearchResultView;
import net.readybid.entities.hotel.web.HotelService;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 12/25/2016.
 *
 */
@Service
public class HotelServiceImpl implements HotelService {

    private final HotelViewRepository hotelViewRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public HotelServiceImpl(HotelViewRepository hotelViewRepository, HotelRepository hotelRepository) {
        this.hotelViewRepository = hotelViewRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public Hotel findHotelById(String id) {
        return hotelRepository.findById(id);
    }

    @Override
    public Hotel findHotelByIdIncludingUnverified(String id) {
        return hotelRepository.findByIdIncludingUnverified(id);
    }

    @Override
    public ListResult<HotelSearchResultView> searchHotels(String query, int page) {
        return hotelViewRepository.search(query, page);
    }
}
