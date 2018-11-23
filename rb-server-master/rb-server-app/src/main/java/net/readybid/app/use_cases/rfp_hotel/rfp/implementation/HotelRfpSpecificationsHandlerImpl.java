package net.readybid.app.use_cases.rfp_hotel.rfp.implementation;

import net.readybid.app.interactors.rfp_hotel.specification.HotelRfpChainSupportService;
import net.readybid.app.interactors.rfp_hotel.specification.gate.HotelRfpSpecificationsLoader;
import net.readybid.app.use_cases.rfp_hotel.rfp.HotelRfpSpecificationsHandler;
import net.readybid.web.RbIdentifiableSingletonViewModel;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelRfpSpecificationsHandlerImpl implements HotelRfpSpecificationsHandler {

    private final HotelRfpSpecificationsLoader specificationsLoader;
    private final HotelRfpChainSupportService hotelRfpChainSupportService;

    @Autowired
    public HotelRfpSpecificationsHandlerImpl(
            HotelRfpSpecificationsLoader specificationsLoader,
            HotelRfpChainSupportService hotelRfpChainSupportService
    ){
        this.specificationsLoader = specificationsLoader;
        this.hotelRfpChainSupportService = hotelRfpChainSupportService;
    }

    @Override
    public RbViewModel enableChainSupport(String rfpId) {
        final boolean isChainSupportEnabled = specificationsLoader.getHotelRfpChainSupportStatus(rfpId);
        if(!isChainSupportEnabled)
            hotelRfpChainSupportService.enable(rfpId);
        return new RbViewModel(){
            @Override
            public Object getData() {
                return new RbIdentifiableSingletonViewModel(rfpId, "chainSupport", true);
            }
        };
    }
}
