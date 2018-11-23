package net.readybid.app.interactors.rfp_hotel.default_response.implementation;

import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;
import net.readybid.app.interactors.rfp_hotel.default_response.HotelRfpDefaultResponseService;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpDefaultResponseStorageManager;
import net.readybid.entities.Id;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelRfpDefaultResponseServiceImpl implements HotelRfpDefaultResponseService {

    private final HotelRfpDefaultResponseStorageManager defaultResponseStorageManager;

    public HotelRfpDefaultResponseServiceImpl(HotelRfpDefaultResponseStorageManager defaultResponseStorageManager) {
        this.defaultResponseStorageManager = defaultResponseStorageManager;
    }

    @Override
    public void update(List<HotelRfpBid> bids) {
        final List<HotelRfpDefaultResponse> responsesToUpdate = bids.stream()
                .map(this::createHotelRfpDefaultResponse)
                .filter(dr -> dr != null && dr.isValid)
                .collect(Collectors.toList());

        if(!responsesToUpdate.isEmpty()) defaultResponseStorageManager.setResponses(responsesToUpdate);
    }

    private HotelRfpDefaultResponse createHotelRfpDefaultResponse(HotelRfpBid b) {
        return b == null ? null : new HotelRfpDefaultResponse(Id.valueOf(b.getSupplierCompanyEntityId()), clean(b.getResponse()));
    }

    private QuestionnaireResponse clean(QuestionnaireResponse response) {
        return response.isValid() ? RemoveVolatileAnswers.from(response) : null;
    }
}
