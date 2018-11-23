package net.readybid.app.use_cases.rfp_hotel.rfp;

import net.readybid.web.RbViewModel;

public interface HotelRfpCoverLetterHandler {
    RbViewModel getNamCoverLetterTemplate(String rfpId);

    RbViewModel setNamCoverLetterTemplate(String rfpId, String template);
}