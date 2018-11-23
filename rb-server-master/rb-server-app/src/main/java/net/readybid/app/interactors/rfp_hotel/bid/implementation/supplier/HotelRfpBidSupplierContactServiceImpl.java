package net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier;

import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidStateFactory;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidSupplierContactService;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidSupplierContactLoader;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidSupplierContactStorageManager;
import net.readybid.bidmanagerview.BidManagerViewCreationService;
import net.readybid.bidmanagerview.HotelRfpCreateChainRfpBidManagerViewCommand;
import net.readybid.entities.Id;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.user.BasicUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HotelRfpBidSupplierContactServiceImpl implements HotelRfpBidSupplierContactService {

    private final HotelRfpBidSupplierContactLoader bidLoader;
    private final HotelRfpBidSupplierContactStorageManager bidStorageManager;
    private final HotelRfpBidStateFactory stateFactory;
    private final BidManagerViewCreationService bmViewCreationService;

    @Autowired
    public HotelRfpBidSupplierContactServiceImpl(
            HotelRfpBidSupplierContactLoader bidLoader,
            HotelRfpBidSupplierContactStorageManager bidStorageManager,
            HotelRfpBidStateFactory stateFactory,
            BidManagerViewCreationService bmViewCreationService
    ) {
        this.bidLoader = bidLoader;
        this.bidStorageManager = bidStorageManager;
        this.stateFactory = stateFactory;
        this.bmViewCreationService = bmViewCreationService;
    }

    @Override
    public void set(List<String> bidsIds, HotelRfpSupplierContact supplierContact) {
        final List<HotelRfpBidSetSupplierContactCommand> commands = bidLoader.loadSetBidContactData(bidsIds).stream()
                .map(d -> d.generateSetContactCommand(supplierContact))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        bidStorageManager.setContactAndSendBid(commands);
    }

    @Override
    public void setContactAndSendBids(List<String> bidsIds, HotelRfpSupplierContact supplierContact, BasicUserDetails currentUser) {
        final HotelRfpBidState bidState = stateFactory.createSimpleState(HotelRfpBidStateStatus.SENT, currentUser);
        final LocalDate sentDate = LocalDate.now();
        final List<HotelRfpBidSetSupplierContactAndSendBidCommandProducer> hotelRfpBidSetSupplierContactCommandProducers
                = bidLoader.loadSetBidContactData(bidsIds);

        bidStorageManager.setContactAndSendBid(hotelRfpBidSetSupplierContactCommandProducers.stream()
                .map(d -> d.generateSetContactAndSendBidCommand(supplierContact, bidState, sentDate))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        setChainRepRfpBidManagerView(hotelRfpBidSetSupplierContactCommandProducers);
    }

    @Override
    public void sendBids(List<String> bidsIds, BasicUserDetails currentUser) {
        final HotelRfpBidState bidState = stateFactory.createSimpleState(HotelRfpBidStateStatus.SENT, currentUser);
        final LocalDate sentDate = LocalDate.now();
        final List<HotelRfpBidSetSupplierContactAndSendBidCommandProducer> hotelRfpBidSetSupplierContactCommandProducers
                = bidLoader.loadSetBidContactData(bidsIds);

        bidStorageManager.setContactAndSendBid(hotelRfpBidSetSupplierContactCommandProducers.stream()
                .map(d -> d.generateSendBidCommand(bidState, sentDate))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));

        setChainRepRfpBidManagerView(hotelRfpBidSetSupplierContactCommandProducers);
    }

    private void setChainRepRfpBidManagerView(List<HotelRfpBidSetSupplierContactAndSendBidCommandProducer> commandProducers) {
        final Set<Id> seenRfpIds = new HashSet<>();
        final List<HotelRfpCreateChainRfpBidManagerViewCommand> commandList = commandProducers.stream()
                .map(HotelRfpBidSetSupplierContactAndSendBidCommandProducer::generateCreateChainRfpView)
                .filter(c -> c != null && seenRfpIds.add(c.rfpId))
                .collect(Collectors.toList());

        bmViewCreationService.createChainRepRfpViews(commandList);
    }
}
