package net.readybid.app.persistence.mongodb.app.rfp_hotel.rfp;

import net.readybid.app.interactors.rfp_hotel.specification.gate.HotelRfpSpecificationsLoader;
import net.readybid.app.persistence.mongodb.repository.HotelRfpRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpCollection;
import net.readybid.rfp.core.Rfp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Projections.include;
import static net.readybid.mongodb.RbMongoFilters.byId;

@Service
public class HotelRfpSpecificationsLoaderImpl implements HotelRfpSpecificationsLoader {

    private final HotelRfpRepository repository;

    @Autowired
    public HotelRfpSpecificationsLoaderImpl(HotelRfpRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean getHotelRfpChainSupportStatus(String rfpId) {
        final Rfp rfp  = repository.findOne(byId(rfpId), include(HotelRfpCollection.CHAIN_SUPPORT));
        return null != rfp && rfp.isChainSupported();
    }
}
