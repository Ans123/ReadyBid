package net.readybid.app.interactors.rfp_hotel.specification.implementation;

import net.readybid.app.interactors.rfp.gate.LetterTemplateLibrary;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidStorageManager;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpStorageManager;
import net.readybid.app.interactors.rfp_hotel.specification.HotelRfpChainSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelRfpChainSupportServiceImpl implements HotelRfpChainSupportService {

    private final LetterTemplateLibrary letterTemplateLoader;
    private final HotelRfpStorageManager rfpStorageManager;
    private final HotelRfpBidStorageManager bidStorageManager;

    @Autowired
    public HotelRfpChainSupportServiceImpl(
            LetterTemplateLibrary letterTemplateLoader,
            HotelRfpStorageManager rfpStorageManager,
            HotelRfpBidStorageManager bidStorageManager
    ) {
        this.letterTemplateLoader = letterTemplateLoader;
        this.rfpStorageManager = rfpStorageManager;
        this.bidStorageManager = bidStorageManager;
    }

    @Override
    public void enable(String rfpId) {
        final String hotelRfpChainCoverLetterTemplate = letterTemplateLoader.getHotelRfpChainCoverLetter();
        rfpStorageManager.enableChainSupport(rfpId, hotelRfpChainCoverLetterTemplate);
        bidStorageManager.enableChainSupport(rfpId);
    }
}