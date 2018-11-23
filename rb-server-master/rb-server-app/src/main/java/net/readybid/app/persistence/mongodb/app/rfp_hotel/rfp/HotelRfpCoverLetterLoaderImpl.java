package net.readybid.app.persistence.mongodb.app.rfp_hotel.rfp;

import net.readybid.app.persistence.mongodb.repository.HotelRfpRepository;
import net.readybid.app.use_cases.rfp_hotel.rfp.gate.HotelRfpCoverLetterLoader;
import net.readybid.rfp.core.Rfp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Projections.include;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpCollection.NAM_COVER_LETTER;
import static net.readybid.mongodb.RbMongoFilters.byId;

@Service
public class HotelRfpCoverLetterLoaderImpl implements HotelRfpCoverLetterLoader {

    private final HotelRfpRepository rfpRepository;

    @Autowired
    public HotelRfpCoverLetterLoaderImpl(HotelRfpRepository rfpRepository) {
        this.rfpRepository = rfpRepository;
    }

    @Override
    public String getNamCoverLetterTemplate(String rfpId) {
        final Rfp rfp = rfpRepository.findOne(byId(rfpId), include(NAM_COVER_LETTER));
        return rfp == null ? null : rfp.getNamCoverLetter();
    }
}