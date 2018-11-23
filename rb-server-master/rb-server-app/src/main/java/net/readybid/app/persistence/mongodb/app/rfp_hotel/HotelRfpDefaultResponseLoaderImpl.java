package net.readybid.app.persistence.mongodb.app.rfp_hotel;

import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpDefaultResponseLoader;
import net.readybid.app.persistence.mongodb.repository.HotelRfpDefaultResponseRepository;
import net.readybid.entities.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static net.readybid.mongodb.RbMongoFilters.byId;

@Service
public class HotelRfpDefaultResponseLoaderImpl implements HotelRfpDefaultResponseLoader {

    private final HotelRfpDefaultResponseRepository repository;

    @Autowired
    public HotelRfpDefaultResponseLoaderImpl(HotelRfpDefaultResponseRepository repository) {
        this.repository = repository;
    }

    @Override
    public HotelRfpDefaultResponse get(Id hotelId) {
        return repository.findOne(byId(hotelId));
    }
}
