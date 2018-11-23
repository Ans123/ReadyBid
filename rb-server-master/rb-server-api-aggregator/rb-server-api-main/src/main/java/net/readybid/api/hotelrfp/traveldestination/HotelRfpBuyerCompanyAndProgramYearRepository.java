package net.readybid.api.hotelrfp.traveldestination;

import net.readybid.rfp.core.HotelRfpBuyerCompanyAndProgramYear;

public interface HotelRfpBuyerCompanyAndProgramYearRepository {
    HotelRfpBuyerCompanyAndProgramYear findHotelRfpBuyerCompanyAndProgramYear(String rfpId);
}
