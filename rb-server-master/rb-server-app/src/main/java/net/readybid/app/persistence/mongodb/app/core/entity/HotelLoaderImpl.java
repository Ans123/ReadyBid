package net.readybid.app.persistence.mongodb.app.core.entity;

import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.app.interactors.core.entity.gate.HotelLoader;
import net.readybid.app.persistence.mongodb.repository.HotelRepository;
import net.readybid.app.persistence.mongodb.repository.implementation.HotelRepositoryImpl;
import net.readybid.entities.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Projections.include;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelCollection.*;
import static net.readybid.mongodb.RbMongoFilters.byId;

 
@Service("HotelLoader")
public class HotelLoaderImpl implements HotelLoader {
	
	@Autowired
    private HotelRepository repository;

	@Autowired
    public HotelLoaderImpl(HotelRepository repository) {
        this.repository = repository;
    }
  
    @Override
    public List<Hotel> getPropertyCodes(List<Id> hotelsIds) {
    	 
        final List<? extends Hotel> hotels = repository.find(byId(hotelsIds), include(PROPCODE, INTERNALHOTELCODE, SABRE_PROPCODE, AMADEUS_PROPCODE, APOLLO_PROPCODE, WRLDSPAN_PROPCODE));
        return new ArrayList<>(hotels);
    }
}
