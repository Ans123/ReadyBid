package net.readybid.api.hotelrfp.traveldestination;

import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.rfp.core.HotelRfpBuyerCompanyAndProgramYear;
import net.readybid.rfphotel.bid.core.HotelRfpBidSupplierCompanyEntityAndSubject;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelDestinationManagerHotelViewModelFactory {

    private final RfpAccessControlService rfpAccessControlService;
    private final HotelRfpBidSupplierCompanyEntityAndSubjectRepository bidRepository;
    private final HotelRfpBuyerCompanyAndProgramYearRepository rfpRepository;
    private final FindHotelsNearCoordinatesRepository hotelsNearCoordinates;
    private final boolean isDevelopmentEnvironment;

    @Autowired
    public TravelDestinationManagerHotelViewModelFactory(
            RfpAccessControlService rfpAccessControlService,
            HotelRfpBidSupplierCompanyEntityAndSubjectRepository bidRepository,
            HotelRfpBuyerCompanyAndProgramYearRepository rfpRepository,
            FindHotelsNearCoordinatesRepository hotelsNearCoordinates,
            Environment environment
    ) {
        this.rfpAccessControlService = rfpAccessControlService;
        this.bidRepository = bidRepository;
        this.rfpRepository = rfpRepository;
        this.hotelsNearCoordinates = hotelsNearCoordinates;
        isDevelopmentEnvironment =  environment.getRequiredProperty("config.version").startsWith("dev");
    }

    public RbViewModel make(String rfpId, String destinationId, SearchForHotelsRequest searchForHotelsRequest) {
        rfpAccessControlService.read(rfpId);

        final List<TravelDestinationManagerHotelViewModel> hotels =
                hotelsNearCoordinates.search(searchForHotelsRequest.getCoordinatesAsList(), searchForHotelsRequest.getMaxDistanceInMeters(), searchForHotelsRequest.getChains());
        final HotelRfpBuyerCompanyAndProgramYear rfp = rfpRepository.findHotelRfpBuyerCompanyAndProgramYear(rfpId);
        final List<HotelRfpBidSupplierCompanyEntityAndSubject> bids = bidRepository.getBuyerBidsFromLastYearOrInDestination(rfp.getBuyerCompanyAccountId(), rfp.getProgramYear()-1, destinationId);

        return new TravelDestinationManagerHotelsViewModel(hotels, bids, destinationId, isDevelopmentEnvironment);
    }
}
