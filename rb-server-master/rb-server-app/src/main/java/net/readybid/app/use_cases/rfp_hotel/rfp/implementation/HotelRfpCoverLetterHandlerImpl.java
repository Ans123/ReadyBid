package net.readybid.app.use_cases.rfp_hotel.rfp.implementation;

import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.app.use_cases.rfp_hotel.bid.gate.HotelRfpBidCoverLetterStorageManager;
import net.readybid.app.use_cases.rfp_hotel.rfp.HotelRfpCoverLetterHandler;
import net.readybid.app.use_cases.rfp_hotel.rfp.gate.HotelRfpCoverLetterLoader;
import net.readybid.app.use_cases.rfp_hotel.rfp.gate.HotelRfpCoverLetterStorageManager;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class HotelRfpCoverLetterHandlerImpl implements HotelRfpCoverLetterHandler {

    private final HotelRfpCoverLetterLoader loader;
    private final HotelRfpCoverLetterStorageManager rfpStorageManager;
    private final HotelRfpBidCoverLetterStorageManager bidStorageManager;

    @Autowired
    public HotelRfpCoverLetterHandlerImpl(
            HotelRfpCoverLetterLoader loader,
            HotelRfpCoverLetterStorageManager rfpStorageManager,
            HotelRfpBidCoverLetterStorageManager bidStorageManager
    ) {
        this.loader = loader;
        this.rfpStorageManager = rfpStorageManager;
        this.bidStorageManager = bidStorageManager;
    }

    @Override
    public RbViewModel getNamCoverLetterTemplate(String rfpId) {
        final String template = loader.getNamCoverLetterTemplate(rfpId);
        return new RbViewModelImpl(template == null ? "" : template);
    }

    @Override
    public RbViewModel setNamCoverLetterTemplate(String rfpId, String template) {
        final boolean isFound = rfpStorageManager.setNamCoverLetterTemplate(rfpId, template);
        if(!isFound) throw new NotFoundException();
        bidStorageManager.updateNamCoverLetterTemplateAsBidCoverLetter(rfpId, template);
        return new RbViewModelImpl(template);
    }

    private static class RbViewModelImpl implements RbViewModel {
        private final String template;

        RbViewModelImpl(String template) {
            this.template = template;
        }

        @Override
        public Object getData() {
            return Collections.singletonMap("template", template);
        }
    }
}