package net.readybid.api.main.bid.create;

import net.readybid.app.core.service.defaultresponse.GetDefaultResponseService;
import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;
import net.readybid.app.interactors.rfp_hotel.dirty.HotelRfpBidBuilder;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.account.core.AccountService;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.core.rfp.hotel.bid.BidFactory;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.BidRepository;
import net.readybid.entities.core.EntityService;
import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.mongodb.RbDuplicateKeyException;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfphotel.CreateBidRequest;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 5/19/2017.
 *
 */
@Service
public class CreateHotelRfpBidServiceImpl implements CreateHotelRfpBidService {

    private final EntityService entityService;
    private final HotelRfpBidBuilderRepository hotelRfpBidBuilderRepository;
    private final AccountService accountService;
    private final BidRepository bidRepository;
    private final BidFactory bidFactory;
    private final GetDefaultResponseService getDefaultResponseService;

    @Autowired
    public CreateHotelRfpBidServiceImpl(
            EntityService entityService,
            HotelRfpBidBuilderRepository HotelRfpBidBuilderRepository,
            AccountService accountService,
            BidRepository bidRepository,
            BidFactory bidFactory,
            GetDefaultResponseService getDefaultResponseService
    ) {
        this.entityService = entityService;
        this.hotelRfpBidBuilderRepository = HotelRfpBidBuilderRepository;
        this.accountService = accountService;
        this.bidRepository = bidRepository;
        this.bidFactory = bidFactory;
        this.getDefaultResponseService = getDefaultResponseService;
    }

    @Override
    public HotelRfpBid createBid(Rfp rfp, TravelDestination destination, CreateBidRequest request, AuthenticatedUser currentUser) {
        final HotelRfpBidBuilder hotelRfpBidBuilder = getHotelRfpBidBuilder(request.hotelId, currentUser);

        HotelRfpBid bid = bidRepository.findDeletedBid(rfp.getId(), destination.getId(), request.hotelId);
        if(bid == null){
            bid = bidFactory.create(rfp, destination, hotelRfpBidBuilder, currentUser);
            bidRepository.create(bid);
        } else {
            bid = bidFactory.recreate(bid, rfp, destination, hotelRfpBidBuilder, currentUser);
            bidRepository.replace(bid);
        }
        return bid;
    }

    private HotelRfpBidBuilder getHotelRfpBidBuilder(String hotelId, AuthenticatedUser currentUser) {
        HotelRfpBidBuilder hotelRfpBidBuilder = hotelRfpBidBuilderRepository.getHotelRfpBidBuilder(hotelId);
        final HotelRfpDefaultResponse defaultResponse = getDefaultResponseService.forHotel(hotelId);
        if(hotelRfpBidBuilder == null){
            try {
                final Hotel hotel = entityService.getHotelIncludingUnverified(hotelId);
                final Account account = accountService.createAccountForEntity(hotel, currentUser);
                return new HotelRfpBidBuilder(account, hotel, defaultResponse);
            } catch(RbDuplicateKeyException dke){
                hotelRfpBidBuilder = getHotelRfpBidBuilder(hotelId, currentUser);
            }
        } else {
            hotelRfpBidBuilder.setDefaultResponse(defaultResponse);
        }
        return hotelRfpBidBuilder;
    }
}
